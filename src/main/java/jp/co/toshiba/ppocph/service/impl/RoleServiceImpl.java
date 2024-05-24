package jp.co.toshiba.ppocph.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.AuthorityDto;
import jp.co.toshiba.ppocph.dto.RoleDto;
import jp.co.toshiba.ppocph.entity.Authority;
import jp.co.toshiba.ppocph.entity.EmployeeRole;
import jp.co.toshiba.ppocph.entity.Role;
import jp.co.toshiba.ppocph.entity.RoleAuth;
import jp.co.toshiba.ppocph.exception.PgCrowdException;
import jp.co.toshiba.ppocph.repository.AuthorityRepository;
import jp.co.toshiba.ppocph.repository.EmployeeRoleRepository;
import jp.co.toshiba.ppocph.repository.RoleAuthRepository;
import jp.co.toshiba.ppocph.repository.RoleRepository;
import jp.co.toshiba.ppocph.service.IRoleService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import jp.co.toshiba.ppocph.utils.SecondBeanUtils;
import jp.co.toshiba.ppocph.utils.SnowflakeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 役割サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleServiceImpl implements IRoleService {

	/**
	 * 論理削除フラグ
	 */
	private static final String DELETE_FLG = "deleteFlg";

	/**
	 * 役割名称
	 */
	private static final String ROLE_NAME = "name";

	/**
	 * 役割管理リポジトリ
	 */
	private final RoleRepository roleRepository;

	/**
	 * 権限管理リポジトリ
	 */
	private final AuthorityRepository authorityRepository;

	/**
	 * 社員役割連携リポジトリ
	 */
	private final EmployeeRoleRepository employeeRoleRepository;

	/**
	 * 役割権限連携リポジトリ
	 */
	private final RoleAuthRepository roleExRepository;

	@Override
	public ResultDto<String> checkDuplicated(final String name) {
		final Role aRole = new Role();
		aRole.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		aRole.setName(name);
		final Example<Role> example = Example.of(aRole, ExampleMatcher.matching());
		return this.roleRepository.findOne(example).isPresent()
				? ResultDto.failed(PgCrowdConstants.MESSAGE_ROLE_NAME_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public ResultDto<String> doAssignment(final Map<String, List<String>> paramMap) {
		final Long[] idArray = { 1L, 5L, 9L, 12L };
		final Long roleId = Long.parseLong(paramMap.get("roleIds").get(0));
		final RoleAuth aRoleAuth = new RoleAuth();
		aRoleAuth.setRoleId(roleId);
		final Example<RoleAuth> example = Example.of(aRoleAuth, ExampleMatcher.matching());
		final List<RoleAuth> list1 = this.roleExRepository.findAll(example);
		final List<Long> list2 = list1.stream().map(RoleAuth::getAuthId).sorted().collect(Collectors.toList());
		final List<Long> authIds = paramMap.get("authIds").stream().map(Long::parseLong)
				.filter(a -> !Arrays.asList(idArray).contains(a)).sorted().collect(Collectors.toList());
		if (CommonProjectUtils.isEqual(authIds, list2)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		this.roleExRepository.deleteAll(list1);
		final List<RoleAuth> list = authIds.stream().map(item -> {
			final RoleAuth roleAuth = new RoleAuth();
			roleAuth.setAuthId(item);
			roleAuth.setRoleId(roleId);
			return roleAuth;
		}).collect(Collectors.toList());
		try {
			this.roleExRepository.saveAll(list);
		} catch (final Exception e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_FORBIDDEN2);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public List<String> getAuthIdsById(final Long id) {
		final RoleAuth aRoleAuth = new RoleAuth();
		aRoleAuth.setRoleId(id);
		final Example<RoleAuth> example = Example.of(aRoleAuth, ExampleMatcher.matching());
		return this.roleExRepository.findAll(example).stream().map(a -> a.getAuthId().toString())
				.collect(Collectors.toList());
	}

	@Override
	public List<AuthorityDto> getAuthList() {
		return this.authorityRepository.findAll().stream().sorted(Comparator.comparing(Authority::getId)).map(item -> {
			final AuthorityDto authorityDto = new AuthorityDto();
			SecondBeanUtils.copyNullableProperties(item, authorityDto);
			authorityDto.setId(item.getId().toString());
			authorityDto.setCategoryId(String.valueOf(item.getCategoryId()));
			return authorityDto;
		}).collect(Collectors.toList());
	}

	@Override
	public RoleDto getRoleById(final Long id) {
		final Role aRole = new Role();
		aRole.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		aRole.setId(id);
		final Example<Role> example = Example.of(aRole, ExampleMatcher.matching());
		final Role role = this.roleRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_NOT_EXISTS);
		});
		final RoleDto roleDto = new RoleDto();
		SecondBeanUtils.copyNullableProperties(role, roleDto);
		roleDto.setId(role.getId().toString());
		return roleDto;
	}

	@Override
	public List<RoleDto> getRolesByEmployeeId(final String employeeId) {
		final List<RoleDto> secondRoles = new ArrayList<>();
		final RoleDto secondRole = new RoleDto();
		secondRole.setId("0");
		secondRole.setName(PgCrowdConstants.DEFAULT_ROLE_NAME);
		final Role aRole = new Role();
		aRole.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Example<Role> example = Example.of(aRole, ExampleMatcher.matching());
		final List<RoleDto> roleDtos = this.roleRepository.findAll(example).stream().map(item -> {
			final RoleDto roleDto = new RoleDto();
			SecondBeanUtils.copyNullableProperties(item, roleDto);
			roleDto.setId(item.getId().toString());
			return roleDto;
		}).collect(Collectors.toList());
		secondRoles.add(secondRole);
		secondRoles.addAll(roleDtos);
		if (employeeId == null) {
			return secondRoles;
		}
		final Optional<EmployeeRole> roledOptional = this.employeeRoleRepository.findById(Long.parseLong(employeeId));
		if (roledOptional.isEmpty()) {
			return secondRoles;
		}
		secondRoles.clear();
		final Long roleId = roledOptional.get().getRoleId();
		final List<RoleDto> selectedRole = roleDtos.stream()
				.filter(a -> CommonProjectUtils.isEqual(roleId.toString(), a.getId())).collect(Collectors.toList());
		secondRoles.addAll(selectedRole);
		secondRoles.addAll(roleDtos);
		return secondRoles.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<RoleDto> getRolesByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final Specification<Role> specification = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get(DELETE_FLG), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Page<Role> pages = this.roleRepository.findAll(specification, pageRequest);
			final List<RoleDto> roleDtos = pages.stream().map(item -> {
				final RoleDto roleDto = new RoleDto();
				SecondBeanUtils.copyNullableProperties(item, roleDto);
				roleDto.setId(item.getId().toString());
				return roleDto;
			}).collect(Collectors.toList());
			return Pagination.of(roleDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Specification<Role> where2 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(ROLE_NAME),
				searchStr);
		final Page<Role> pages = this.roleRepository.findAll(specification.and(where2), pageRequest);
		final List<RoleDto> roleDtos = pages.stream().map(item -> {
			final RoleDto roleDto = new RoleDto();
			SecondBeanUtils.copyNullableProperties(item, roleDto);
			roleDto.setId(item.getId().toString());
			return roleDto;
		}).collect(Collectors.toList());
		return Pagination.of(roleDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		final EmployeeRole employeeRole = new EmployeeRole();
		employeeRole.setRoleId(id);
		final Example<EmployeeRole> example = Example.of(employeeRole, ExampleMatcher.matching());
		final List<EmployeeRole> list = this.employeeRoleRepository.findAll(example);
		if (!list.isEmpty()) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_FORBIDDEN);
		}
		final Role aRole = new Role();
		aRole.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		aRole.setId(id);
		final Example<Role> example2 = Example.of(aRole, ExampleMatcher.matching());
		final Role role = this.roleRepository.findOne(example2).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		role.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_FLG);
		this.roleRepository.saveAndFlush(role);
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final RoleDto roleDto) {
		final Role role = new Role();
		SecondBeanUtils.copyNullableProperties(roleDto, role);
		role.setId(SnowflakeUtils.snowflakeId());
		role.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		this.roleRepository.saveAndFlush(role);
	}

	@Override
	public ResultDto<String> update(final RoleDto roleDto) {
		final Role aRole = new Role();
		aRole.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		aRole.setId(Long.parseLong(roleDto.getId()));
		final Example<Role> example = Example.of(aRole, ExampleMatcher.matching());
		final Role role = this.roleRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final Role originalEntity = new Role();
		SecondBeanUtils.copyNullableProperties(role, originalEntity);
		SecondBeanUtils.copyNullableProperties(roleDto, role);
		if (CommonProjectUtils.isEqual(originalEntity, role)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		try {
			this.roleRepository.saveAndFlush(role);
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_ROLE_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
