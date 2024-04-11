package jp.co.sony.ppogah.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jp.co.sony.ppogah.common.PgCrowdConstants;
import jp.co.sony.ppogah.dto.EmployeeDto;
import jp.co.sony.ppogah.entity.Authority;
import jp.co.sony.ppogah.entity.Employee;
import jp.co.sony.ppogah.entity.EmployeeRole;
import jp.co.sony.ppogah.entity.Role;
import jp.co.sony.ppogah.entity.RoleAuth;
import jp.co.sony.ppogah.exception.PgCrowdException;
import jp.co.sony.ppogah.repository.AuthorityRepository;
import jp.co.sony.ppogah.repository.EmployeeRepository;
import jp.co.sony.ppogah.repository.EmployeeRoleRepository;
import jp.co.sony.ppogah.repository.RoleAuthRepository;
import jp.co.sony.ppogah.repository.RoleRepository;
import jp.co.sony.ppogah.service.IEmployeeService;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import jp.co.sony.ppogah.utils.SecondBeanUtils;
import jp.co.sony.ppogah.utils.SnowflakeUtils;
import jp.co.sony.ppogah.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 社員サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeServiceImpl implements IEmployeeService {

	/**
	 * Randomナンバー
	 */
	private static final Random RANDOM = new Random();

	/**
	 * 社員管理リポジトリ
	 */
	private final EmployeeRepository employeeRepository;

	/**
	 * 社員役割連携リポジトリ
	 */
	private final EmployeeRoleRepository employeeExRepository;

	/**
	 * 役割管理リポジトリ
	 */
	private final RoleRepository roleRepository;

	/**
	 * 役割権限連携リポジトリ
	 */
	private final RoleAuthRepository roleExRepository;

	/**
	 * 権限管理リポジトリ
	 */
	private final AuthorityRepository pgAuthRepository;

	/**
	 * 日時フォマーター
	 */
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public ResultDto<String> check(final String loginAccount) {
		final Employee employee = new Employee();
		employee.setLoginAccount(loginAccount);
		final Example<Employee> example = Example.of(employee, ExampleMatcher.matching());
		return this.employeeRepository.findOne(example).isPresent()
				? ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public Boolean checkEdition(final Long id) {
		final EmployeeRole employeeRole = this.employeeExRepository.findById(id).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final Role role = this.roleRepository.findById(employeeRole.getRoleId()).orElseGet(Role::new);
		final Specification<RoleAuth> where = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("roleId"), role.getId());
		final Specification<RoleAuth> specification = Specification.where(where);
		final List<Long> authIds = this.roleExRepository.findAll(specification).stream().map(RoleAuth::getAuthId)
				.collect(Collectors.toList());
		final List<String> authList = this.pgAuthRepository.findAllById(authIds).stream().map(Authority::getName)
				.collect(Collectors.toList());
		if (!authList.contains("employee%edition") && !authList.contains("employee%delete")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public EmployeeDto getEmployeeById(final Long id) {
		final Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final EmployeeRole employeeRole = this.employeeExRepository.findById(id).orElseGet(EmployeeRole::new);
		final EmployeeDto employeeDto = new EmployeeDto();
		SecondBeanUtils.copyNullableProperties(employee, employeeDto);
		employeeDto.setPassword(PgCrowdConstants.DEFAULT_ROLE_NAME);
		employeeDto.setDateOfBirth(this.formatter.format(employee.getDateOfBirth()));
		employeeDto.setRoleId(employeeRole.getRoleId());
		return employeeDto;
	}

	@Override
	public Pagination<EmployeeDto> getEmployeesByKeyword(final Integer pageNum, final String keyword,
			final Long userId) {
		if (Boolean.FALSE.equals(this.checkEdition(userId))) {
			final List<EmployeeDto> employeeDtos = new ArrayList<>();
			final Employee employee = this.employeeRepository.findById(userId).orElseThrow(() -> {
				throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
			});
			final EmployeeDto employeeDto = new EmployeeDto();
			SecondBeanUtils.copyNullableProperties(employee, employeeDto);
			employeeDto.setDateOfBirth(this.formatter.format(employee.getDateOfBirth()));
			employeeDtos.add(employeeDto);
			return Pagination.of(employeeDtos, employeeDtos.size(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final Specification<Employee> status = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		if (StringUtils.isEmpty(keyword)) {
			final Specification<Employee> specification = Specification.where(status);
			final Page<Employee> pages = this.employeeRepository.findAll(specification, pageRequest);
			final List<EmployeeDto> employeeDtos = pages.stream().map(item -> {
				final EmployeeDto employeeDto = new EmployeeDto();
				SecondBeanUtils.copyNullableProperties(item, employeeDto);
				employeeDto.setDateOfBirth(this.formatter.format(item.getDateOfBirth()));
				return employeeDto;
			}).collect(Collectors.toList());
			return Pagination.of(employeeDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		final Specification<Employee> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
				.like(root.get("loginAccount"), searchStr);
		final Specification<Employee> where2 = (root, query, criteriaBuilder) -> criteriaBuilder
				.like(root.get("username"), searchStr);
		final Specification<Employee> where3 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"),
				searchStr);
		final Specification<Employee> specification = Specification.where(status)
				.and(Specification.where(where1).or(where2).or(where3));
		final Page<Employee> pages = this.employeeRepository.findAll(specification, pageRequest);
		final List<EmployeeDto> employeeDtos = pages.stream().map(item -> {
			final EmployeeDto employeeDto = new EmployeeDto();
			SecondBeanUtils.copyNullableProperties(item, employeeDto);
			employeeDto.setDateOfBirth(this.formatter.format(item.getDateOfBirth()));
			return employeeDto;
		}).collect(Collectors.toList());
		return Pagination.of(employeeDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	private String getRandomStr() {
		final String stry = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final char[] cr1 = stry.toCharArray();
		final char[] cr2 = stry.toLowerCase().toCharArray();
		final StringBuilder builder = new StringBuilder();
		builder.append(cr1[EmployeeServiceImpl.RANDOM.nextInt(cr1.length)]);
		for (int i = 0; i < 7; i++) {
			builder.append(cr2[EmployeeServiceImpl.RANDOM.nextInt(cr2.length)]);
		}
		return builder.toString();
	}

	@Override
	public Boolean login(final String account, final String password) {
		final Specification<Employee> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("password"), password);
		final Specification<Employee> where2 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("email"), account);
		final Specification<Employee> where3 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("loginAccount"), account);
		final Specification<Employee> specification = Specification.where(where1)
				.and(Specification.where(where2).or(where3));
		final Optional<Employee> optional = this.employeeRepository.findOne(specification);
		if (optional.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean register(final EmployeeDto employeeDto) {
		final Specification<Employee> where = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"),
				employeeDto.getEmail());
		final Specification<Employee> specification = Specification.where(where);
		final Optional<Employee> findOne = this.employeeRepository.findOne(specification);
		if (findOne.isPresent()) {
			return Boolean.FALSE;
		}
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(SnowflakeUtils.snowflakeId());
		employee.setLoginAccount(this.getRandomStr());
		employee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		this.employeeRepository.saveAndFlush(employee);
		return Boolean.TRUE;
	}

	@Override
	public void removeById(final Long userId) {
		final Employee employee = this.employeeRepository.findById(userId).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_PROHIBITED);
		});
		employee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_FLG);
		this.employeeRepository.saveAndFlush(employee);
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(SnowflakeUtils.snowflakeId());
		employee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		this.employeeRepository.saveAndFlush(employee);
		if ((employeeDto.getRoleId() != null) && !Objects.equals(Long.valueOf(0L), employeeDto.getRoleId())) {
			final EmployeeRole employeeEx = new EmployeeRole();
			employeeEx.setEmployeeId(employee.getId());
			employeeEx.setRoleId(employeeDto.getRoleId());
			this.employeeExRepository.saveAndFlush(employeeEx);
		}
	}

	@Override
	public ResultDto<String> update(final EmployeeDto employeeDto) {
		final Employee employee = this.employeeRepository.findById(employeeDto.getId()).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final Employee originalEntity = new Employee();
		SecondBeanUtils.copyNullableProperties(employee, originalEntity);
		final EmployeeRole employeeRole = this.employeeExRepository.findById(employeeDto.getId())
				.orElseGet(EmployeeRole::new);
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		if (originalEntity.equals(employee) && Objects.equals(employeeRole.getRoleId(), employeeDto.getRoleId())) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		this.employeeRepository.saveAndFlush(employee);
		return ResultDto.successWithoutData();
	}
}
