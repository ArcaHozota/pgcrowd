package jp.co.sony.ppogah.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jp.co.sony.ppogah.common.PgCrowdConstants;
import jp.co.sony.ppogah.dto.RoleDto;
import jp.co.sony.ppogah.entity.Authority;
import jp.co.sony.ppogah.entity.EmployeeRole;
import jp.co.sony.ppogah.entity.Role;
import jp.co.sony.ppogah.entity.RoleAuth;
import jp.co.sony.ppogah.exception.PgCrowdException;
import jp.co.sony.ppogah.repository.AuthorityRepository;
import jp.co.sony.ppogah.repository.EmployeeRoleRepository;
import jp.co.sony.ppogah.repository.RoleAuthRepository;
import jp.co.sony.ppogah.repository.RoleRepository;
import jp.co.sony.ppogah.service.IRoleService;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import jp.co.sony.ppogah.utils.SecondBeanUtils;
import jp.co.sony.ppogah.utils.SnowflakeUtils;
import jp.co.sony.ppogah.utils.StringUtils;
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
	 * 役割ID
	 */
	private static final String ROLE_ID = "roleId";

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
	private final AuthorityRepository pgAuthRepository;

	/**
	 * 社員役割連携リポジトリ
	 */
	private final EmployeeRoleRepository employeeExRepository;

	/**
	 * 役割権限連携リポジトリ
	 */
	private final RoleAuthRepository roleExRepository;

	@Override
	public ResultDto<String> checkDuplicated(final String name) {
		final Specification<Role> where1 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DELETE_FLG),
				PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Specification<Role> where2 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ROLE_NAME),
				name);
		final Specification<Role> specification = Specification.where(where1).and(where2);
		return this.roleRepository.findOne(specification).isPresent()
				? ResultDto.failed(PgCrowdConstants.MESSAGE_ROLE_NAME_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public ResultDto<String> doAssignment(final Map<String, List<Long>> paramMap) {
		final Long[] idArray = { 1L, 5L, 9L, 12L };
		final Long roleId = paramMap.get(ROLE_ID).get(0);
		final Specification<RoleAuth> where = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ROLE_ID),
				roleId);
		final Specification<RoleAuth> specification = Specification.where(where);
		final List<RoleAuth> list1 = this.roleExRepository.findAll(specification);
		final List<Long> list2 = list1.stream().map(RoleAuth::getAuthId).sorted().collect(Collectors.toList());
		final List<Long> authIds = paramMap.get("authIdArray").stream().filter(a -> !Arrays.asList(idArray).contains(a))
				.sorted().collect(Collectors.toList());
		if (list2.equals(authIds)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		this.roleExRepository.deleteAll(list1);
		final List<RoleAuth> list = authIds.stream().map(item -> {
			final RoleAuth roleEx = new RoleAuth();
			roleEx.setAuthId(item);
			roleEx.setRoleId(roleId);
			return roleEx;
		}).collect(Collectors.toList());
		try {
			this.roleExRepository.saveAll(list);
		} catch (final Exception e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_FORBIDDEN2);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public List<Long> getAuthIdsById(final Long id) {
		final Specification<RoleAuth> where = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ROLE_ID),
				id);
		final Specification<RoleAuth> specification = Specification.where(where);
		return this.roleExRepository.findAll(specification).stream().map(RoleAuth::getAuthId)
				.collect(Collectors.toList());
	}

	@Override
	public List<Authority> getAuthList() {
		return this.pgAuthRepository.findAll().stream().sorted(Comparator.comparing(Authority::getId))
				.collect(Collectors.toList());
	}

	@Override
	public RoleDto getRoleById(final Long id) {
		final RoleDto roleDto = new RoleDto();
		final Role role = this.roleRepository.findById(id).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_NOT_EXISTS);
		});
		SecondBeanUtils.copyNullableProperties(role, roleDto);
		return roleDto;
	}

	@Override
	public List<RoleDto> getRolesByEmployeeId(final String employeeId) {
		final List<RoleDto> secondRoles = new ArrayList<>();
		final RoleDto secondRole = new RoleDto();
		secondRole.setId("0");
		secondRole.setName(PgCrowdConstants.DEFAULT_ROLE_NAME);
		final Specification<Role> where1 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DELETE_FLG),
				PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Specification<Role> specification1 = Specification.where(where1);
		final List<RoleDto> roleDtos = this.roleRepository.findAll(specification1).stream().map(item -> {
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
		final Optional<EmployeeRole> roledOptional = this.employeeExRepository.findById(Long.parseLong(employeeId));
		if (roledOptional.isEmpty()) {
			return secondRoles;
		}
		secondRoles.clear();
		final Long roleId = roledOptional.get().getRoleId();
		final List<RoleDto> selectedRole = roleDtos.stream()
				.filter(a -> StringUtils.isEqual(roleId.toString(), a.getId())).collect(Collectors.toList());
		secondRoles.addAll(selectedRole);
		secondRoles.addAll(roleDtos);
		return secondRoles.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<RoleDto> getRolesByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final Specification<Role> where1 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DELETE_FLG),
				PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Specification<Role> specification = Specification.where(where1);
		if (StringUtils.isEmpty(keyword)) {
			final Page<Role> pages = this.roleRepository.findAll(specification, pageRequest);
			final List<RoleDto> roleDtos = pages.stream().map(item -> {
				final RoleDto roleDto = new RoleDto();
				SecondBeanUtils.copyNullableProperties(item, roleDto);
				return roleDto;
			}).collect(Collectors.toList());
			return Pagination.of(roleDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		if (StringUtils.isDigital(keyword)) {
			final Page<Role> byIdLike = this.roleRepository.findByIdLike(keyword, PgCrowdConstants.LOGIC_DELETE_INITIAL,
					pageRequest);
			final List<RoleDto> roleDtos = byIdLike.stream().map(item -> {
				final RoleDto roleDto = new RoleDto();
				SecondBeanUtils.copyNullableProperties(item, roleDto);
				return roleDto;
			}).collect(Collectors.toList());
			return Pagination.of(roleDtos, byIdLike.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		final Specification<Role> where2 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(ROLE_NAME),
				searchStr);
		final Page<Role> pages = this.roleRepository.findAll(specification.and(where2), pageRequest);
		final List<RoleDto> roleDtos = pages.stream().map(item -> {
			final RoleDto roleDto = new RoleDto();
			SecondBeanUtils.copyNullableProperties(item, roleDto);
			return roleDto;
		}).collect(Collectors.toList());
		return Pagination.of(roleDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		final Specification<EmployeeRole> where = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get(ROLE_ID), id);
		final Specification<EmployeeRole> specification = Specification.where(where);
		final List<EmployeeRole> list = this.employeeExRepository.findAll(specification);
		if (!list.isEmpty()) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_FORBIDDEN);
		}
		final Role role = this.roleRepository.findById(id).orElseThrow(() -> {
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
		final Role role = this.roleRepository.findById(Long.parseLong(roleDto.getId())).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final Role originalEntity = new Role();
		SecondBeanUtils.copyNullableProperties(role, originalEntity);
		SecondBeanUtils.copyNullableProperties(roleDto, role);
		if (originalEntity.equals(role)) {
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
