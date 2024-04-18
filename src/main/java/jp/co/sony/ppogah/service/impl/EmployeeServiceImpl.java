package jp.co.sony.ppogah.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.dto.EmployeeDto;
import jp.co.sony.ppogah.entity.Employee;
import jp.co.sony.ppogah.entity.EmployeeRole;
import jp.co.sony.ppogah.exception.PgCrowdException;
import jp.co.sony.ppogah.repository.EmployeeRepository;
import jp.co.sony.ppogah.repository.EmployeeRoleRepository;
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
	 * 日時フォマーター
	 */
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public ResultDto<String> checkDuplicated(final String loginAccount) {
		final Employee employee = new Employee();
		employee.setLoginAccount(loginAccount);
		final Example<Employee> example = Example.of(employee, ExampleMatcher.matching());
		return this.employeeRepository.findOne(example).isPresent()
				? ResultDto.failed(PgCrowd2Constants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public EmployeeDto getEmployeeById(final String id) {
		final Employee employee = this.employeeRepository.findById(Long.parseLong(id)).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowd2Constants.MESSAGE_STRING_FATAL_ERROR);
		});
		final EmployeeRole employeeRole = this.employeeExRepository.findById(Long.parseLong(id))
				.orElseGet(EmployeeRole::new);
		final EmployeeDto employeeDto = new EmployeeDto();
		SecondBeanUtils.copyNullableProperties(employee, employeeDto);
		employeeDto.setId(employee.getId().toString());
		employeeDto.setPassword(PgCrowd2Constants.DEFAULT_ROLE_NAME);
		employeeDto.setDateOfBirth(this.formatter.format(employee.getDateOfBirth()));
		employeeDto.setRoleId(employeeRole.getRoleId().toString());
		return employeeDto;
	}

	@Override
	public Pagination<EmployeeDto> getEmployeesByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowd2Constants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final Specification<Employee> status = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		if (StringUtils.isEmpty(keyword)) {
			final Specification<Employee> specification = Specification.where(status);
			final Page<Employee> pages = this.employeeRepository.findAll(specification, pageRequest);
			final List<EmployeeDto> employeeDtos = pages.stream().map(item -> {
				final EmployeeDto employeeDto = new EmployeeDto();
				SecondBeanUtils.copyNullableProperties(item, employeeDto);
				employeeDto.setId(item.getId().toString());
				employeeDto.setDateOfBirth(this.formatter.format(item.getDateOfBirth()));
				return employeeDto;
			}).collect(Collectors.toList());
			return Pagination.of(employeeDtos, pages.getTotalElements(), pageNum, PgCrowd2Constants.DEFAULT_PAGE_SIZE);
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
			employeeDto.setId(item.getId().toString());
			employeeDto.setDateOfBirth(this.formatter.format(item.getDateOfBirth()));
			return employeeDto;
		}).collect(Collectors.toList());
		return Pagination.of(employeeDtos, pages.getTotalElements(), pageNum, PgCrowd2Constants.DEFAULT_PAGE_SIZE);
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
		employee.setDeleteFlg(PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		this.employeeRepository.saveAndFlush(employee);
		return Boolean.TRUE;
	}

	@Override
	public void remove(final Long userId) {
		final Employee employee = this.employeeRepository.findById(userId).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowd2Constants.MESSAGE_STRING_PROHIBITED);
		});
		employee.setDeleteFlg(PgCrowd2Constants.LOGIC_DELETE_FLG);
		this.employeeRepository.saveAndFlush(employee);
		this.employeeExRepository.deleteById(userId);
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(SnowflakeUtils.snowflakeId());
		employee.setDeleteFlg(PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		this.employeeRepository.saveAndFlush(employee);
		if (StringUtils.isNotEmpty(employeeDto.getRoleId())
				&& !Objects.equals(Long.valueOf(0L), Long.parseLong(employeeDto.getRoleId()))) {
			final EmployeeRole employeeEx = new EmployeeRole();
			employeeEx.setEmployeeId(employee.getId());
			employeeEx.setRoleId(Long.parseLong(employeeDto.getRoleId()));
			this.employeeExRepository.saveAndFlush(employeeEx);
		}
	}

	@Override
	public ResultDto<String> update(final EmployeeDto employeeDto) {
		final Employee employee = this.employeeRepository.findById(Long.parseLong(employeeDto.getId()))
				.orElseThrow(() -> {
					throw new PgCrowdException(PgCrowd2Constants.MESSAGE_STRING_FATAL_ERROR);
				});
		final Employee originalEntity = new Employee();
		SecondBeanUtils.copyNullableProperties(employee, originalEntity);
		final EmployeeRole employeeRole = this.employeeExRepository.findById(Long.parseLong(employeeDto.getId()))
				.orElseGet(EmployeeRole::new);
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		if (originalEntity.equals(employee)
				&& Objects.equals(employeeRole.getRoleId(), Long.parseLong(employeeDto.getRoleId()))) {
			return ResultDto.failed(PgCrowd2Constants.MESSAGE_STRING_NOCHANGE);
		}
		this.employeeRepository.saveAndFlush(employee);
		return ResultDto.successWithoutData();
	}
}
