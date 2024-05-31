package jp.co.toshiba.ppocph.service.impl;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.EmployeeDto;
import jp.co.toshiba.ppocph.entity.Employee;
import jp.co.toshiba.ppocph.entity.EmployeeRole;
import jp.co.toshiba.ppocph.entity.Role;
import jp.co.toshiba.ppocph.exception.PgCrowdException;
import jp.co.toshiba.ppocph.repository.EmployeeRepository;
import jp.co.toshiba.ppocph.repository.EmployeeRoleRepository;
import jp.co.toshiba.ppocph.repository.RoleRepository;
import jp.co.toshiba.ppocph.service.IEmployeeService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import jp.co.toshiba.ppocph.utils.SecondBeanUtils;
import jp.co.toshiba.ppocph.utils.SnowflakeUtils;
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
	 * 日時フォマーター
	 */
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * パスワードエンコーダ
	 */
	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder(BCryptVersion.$2Y, 7);

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
	private final EmployeeRoleRepository employeeRoleRepository;

	/**
	 * 役割管理リポジトリ
	 */
	private final RoleRepository roleRepository;

	@Override
	public ResultDto<String> checkDuplicated(final String loginAccount) {
		final Employee employee = new Employee();
		employee.setLoginAccount(loginAccount);
		final Example<Employee> example = Example.of(employee, ExampleMatcher.matching());
		return this.employeeRepository.findOne(example).isPresent()
				? ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public EmployeeDto getEmployeeById(final String id) {
		final Employee aEmployee = new Employee();
		aEmployee.setId(Long.parseLong(id));
		aEmployee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Example<Employee> example = Example.of(aEmployee, ExampleMatcher.matching());
		final Employee employee = this.employeeRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final EmployeeRole employeeRole = this.employeeRoleRepository.findById(Long.parseLong(id))
				.orElseGet(EmployeeRole::new);
		final EmployeeDto employeeDto = new EmployeeDto();
		SecondBeanUtils.copyNullableProperties(employee, employeeDto);
		employeeDto.setId(employee.getId().toString());
		employeeDto.setPassword(PgCrowdConstants.DEFAULT_ROLE_NAME);
		employeeDto.setDateOfBirth(EmployeeServiceImpl.FORMATTER.format(employee.getDateOfBirth()));
		employeeDto.setRoleId(employeeRole.getRoleId().toString());
		return employeeDto;
	}

	@Override
	public Pagination<EmployeeDto> getEmployeesByKeyword(final Integer pageNum, final String keyword, final Long userId,
			final String authChkFlag) {
		final Specification<Employee> status = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		if (Boolean.FALSE.equals(Boolean.valueOf(authChkFlag))) {
			final Specification<Employee> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
					.equal(root.get("id"), userId);
			final Specification<Employee> specification = Specification.where(status).and(where1);
			final Employee employee = this.employeeRepository.findOne(specification).orElseThrow(() -> {
				throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
			});
			final EmployeeDto employeeDto = new EmployeeDto();
			SecondBeanUtils.copyNullableProperties(employee, employeeDto);
			employeeDto.setId(employee.getId().toString());
			employeeDto.setDateOfBirth(EmployeeServiceImpl.FORMATTER.format(employee.getDateOfBirth()));
			final List<EmployeeDto> employeeDtos = new ArrayList<>();
			employeeDtos.add(employeeDto);
			return Pagination.of(employeeDtos, employeeDtos.size(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Page<Employee> pages = this.employeeRepository.findAll(status, pageRequest);
			final List<EmployeeDto> employeeDtos = pages.stream().map(item -> {
				final EmployeeDto employeeDto = new EmployeeDto();
				SecondBeanUtils.copyNullableProperties(item, employeeDto);
				employeeDto.setId(item.getId().toString());
				employeeDto.setDateOfBirth(EmployeeServiceImpl.FORMATTER.format(item.getDateOfBirth()));
				return employeeDto;
			}).collect(Collectors.toList());
			return Pagination.of(employeeDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Specification<Employee> specification = (root, query, criteriaBuilder) -> {
			criteriaBuilder.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
			return criteriaBuilder.or(criteriaBuilder.like(root.get("loginAccount"), searchStr),
					criteriaBuilder.like(root.get("username"), searchStr),
					criteriaBuilder.like(root.get("email"), searchStr));
		};
		final Page<Employee> pages = this.employeeRepository.findAll(specification, pageRequest);
		final List<EmployeeDto> employeeDtos = pages.stream().map(item -> {
			final EmployeeDto employeeDto = new EmployeeDto();
			SecondBeanUtils.copyNullableProperties(item, employeeDto);
			employeeDto.setId(item.getId().toString());
			employeeDto.setDateOfBirth(EmployeeServiceImpl.FORMATTER.format(item.getDateOfBirth()));
			return employeeDto;
		}).collect(Collectors.toList());
		return Pagination.of(employeeDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	/**
	 * デフォルトのアカウントを取得する
	 *
	 * @return String
	 */
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
	public Boolean register(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		employee.setEmail(employeeDto.getEmail());
		final Example<Employee> example = Example.of(employee, ExampleMatcher.matching());
		final Optional<Employee> findOne = this.employeeRepository.findOne(example);
		if (findOne.isPresent()) {
			return Boolean.FALSE;
		}
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(SnowflakeUtils.snowflakeId());
		employee.setLoginAccount(this.getRandomStr());
		employee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), EmployeeServiceImpl.FORMATTER));
		this.employeeRepository.saveAndFlush(employee);
		final Role aRole = new Role();
		aRole.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		aRole.setName("正社員");
		final Example<Role> example2 = Example.of(aRole, ExampleMatcher.matching());
		final Role role = this.roleRepository.findOne(example2).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final EmployeeRole employeeRole = new EmployeeRole();
		employeeRole.setEmployeeId(employee.getId());
		employeeRole.setRoleId(role.getId());
		this.employeeRoleRepository.saveAndFlush(employeeRole);
		return Boolean.TRUE;
	}

	@Override
	public void remove(final Long userId) {
		final Employee aEmployee = new Employee();
		aEmployee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		aEmployee.setId(userId);
		final Example<Employee> example = Example.of(aEmployee, ExampleMatcher.matching());
		final Employee employee = this.employeeRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_PROHIBITED);
		});
		employee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_FLG);
		this.employeeRepository.saveAndFlush(employee);
		this.employeeRoleRepository.deleteById(userId);
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(SnowflakeUtils.snowflakeId());
		employee.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		employee.setPassword(EmployeeServiceImpl.ENCODER.encode(employeeDto.getPassword()));
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), EmployeeServiceImpl.FORMATTER));
		this.employeeRepository.saveAndFlush(employee);
		if (CommonProjectUtils.isNotEmpty(employeeDto.getRoleId())
				&& !Objects.equals(Long.valueOf(0L), Long.parseLong(employeeDto.getRoleId()))) {
			final EmployeeRole employeeEx = new EmployeeRole();
			employeeEx.setEmployeeId(employee.getId());
			employeeEx.setRoleId(Long.parseLong(employeeDto.getRoleId()));
			this.employeeRoleRepository.saveAndFlush(employeeEx);
		}
	}

	@Override
	public ResultDto<String> update(final EmployeeDto employeeDto) {
		final String password = employeeDto.getPassword();
		final Employee employee = this.employeeRepository.findById(Long.parseLong(employeeDto.getId()))
				.orElseThrow(() -> {
					throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
				});
		final EmployeeRole employeeRole = this.employeeRoleRepository.findById(Long.parseLong(employeeDto.getId()))
				.orElseGet(EmployeeRole::new);
		boolean passwordMatch = false;
		if (CommonProjectUtils.isNotEmpty(password)) {
			passwordMatch = ENCODER.matches(password, employee.getPassword());
		} else {
			passwordMatch = true;
		}
		final EmployeeDto aEmployeeDto = new EmployeeDto();
		SecondBeanUtils.copyNullableProperties(employee, aEmployeeDto);
		aEmployeeDto.setId(employee.getId().toString());
		aEmployeeDto.setPassword(password);
		aEmployeeDto.setDateOfBirth(FORMATTER.format(employee.getDateOfBirth()));
		aEmployeeDto.setRoleId(employeeRole.getRoleId().toString());
		if (CommonProjectUtils.isNotEqual(aEmployeeDto, employeeDto) && passwordMatch) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		employee.setLoginAccount(employeeDto.getLoginAccount());
		employee.setUsername(employeeDto.getUsername());
		if (!passwordMatch) {
			employee.setPassword(ENCODER.encode(password));
		}
		employee.setEmail(employeeDto.getEmail());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), FORMATTER));
		employeeRole.setRoleId(Long.parseLong(employeeDto.getRoleId()));
		this.employeeRoleRepository.saveAndFlush(employeeRole);
		this.employeeRepository.saveAndFlush(employee);
		return ResultDto.successWithoutData();
	}
}
