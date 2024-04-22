package jp.co.sony.ppogah.listener;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.entity.Employee;
import jp.co.sony.ppogah.entity.EmployeeRole;
import jp.co.sony.ppogah.entity.Role;
import jp.co.sony.ppogah.entity.RoleAuth;
import jp.co.sony.ppogah.repository.AuthorityRepository;
import jp.co.sony.ppogah.repository.EmployeeRepository;
import jp.co.sony.ppogah.repository.EmployeeRoleRepository;
import jp.co.sony.ppogah.repository.RoleAuthRepository;
import jp.co.sony.ppogah.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * ログインコントローラ(SpringSecurity関連)
 *
 * @author ArkamaHozota
 * @since 2.00
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PgCrowd2UserDetailsService implements UserDetailsService {

	/**
	 * 社員管理マッパー
	 */
	private final EmployeeRepository employeeRepository;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleRepository employeeRoleRepository;

	/**
	 * 役割マッパー
	 */
	private final RoleRepository roleRepository;

	/**
	 * 役割権限マッパー
	 */
	private final RoleAuthRepository roleAuthRepository;

	/**
	 * 権限マッパー
	 */
	private final AuthorityRepository authorityRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Specification<Employee> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		final Specification<Employee> where2 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("loginAccount"), username);
		final Specification<Employee> where3 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("email"), username);
		final Specification<Employee> specification1 = Specification.where(where1)
				.and(Specification.where(where2).or(where3));
		final Optional<Employee> optionalEmployee = this.employeeRepository.findOne(specification1);
		if (optionalEmployee.isEmpty()) {
			throw new DisabledException(PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR1);
		}
		final Employee employee = optionalEmployee.get();
		final Optional<EmployeeRole> optionalEmployeeRole = this.employeeRoleRepository.findById(employee.getId());
		if (optionalEmployeeRole.isEmpty()) {
			throw new InsufficientAuthenticationException(PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR2);
		}
		final Specification<Role> where4 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		final Specification<Role> where5 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),
				optionalEmployeeRole.get().getRoleId());
		final Specification<Role> specification2 = Specification.where(where4).and(where5);
		final Optional<Role> optionalRole = this.roleRepository.findOne(specification2);
		if (optionalRole.isEmpty()) {
			throw new InsufficientAuthenticationException(PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR2);
		}
		final Specification<RoleAuth> where6 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("roleId"), optionalRole.get().getId());
		final List<RoleAuth> roleAuths = this.roleAuthRepository.findAll(where6);
		if (roleAuths.isEmpty()) {
			throw new AuthenticationCredentialsNotFoundException(PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR3);
		}
		final List<Long> authIds = roleAuths.stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
		final List<GrantedAuthority> authorities = this.authorityRepository.findAllById(authIds).stream()
				.map(item -> new SimpleGrantedAuthority(item.getName())).collect(Collectors.toList());
		return new SecurityAdmin(employee, authorities);
	}

}
