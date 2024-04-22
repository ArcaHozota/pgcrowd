package jp.co.sony.ppogah.listener;

import java.util.List;
import java.util.stream.Collectors;

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
import jp.co.sony.ppogah.entity.RoleAuth;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * ログインコントローラ(SpringSecurity関連)
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PgCrowd2UserDetailsService implements UserDetailsService {

	/**
	 * 権限マッパー
	 */
	private final AuthorityMapper authorityMapper;

	/**
	 * 社員管理マッパー
	 */
	private final EmployeeMapper employeeMapper;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleMapper employeeRoleMapper;

	/**
	 * 役割マッパー
	 */
	private final RoleMapper roleMapper;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Employee employee = this.employeeMapper.selectByLoginAcct(username);
		if (employee == null) {
			throw new DisabledException(PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR1);
		}
		final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(employee.getId());
		if (employeeRole == null) {
			throw new InsufficientAuthenticationException(PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR2);
		}
		final Role role = this.roleMapper.selectByIdWithAuth(employeeRole.getRoleId());
		if (role.getRoleAuths().isEmpty()) {
			throw new AuthenticationCredentialsNotFoundException(PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR3);
		}
		final List<Long> authIds = role.getRoleAuths().stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
		final List<GrantedAuthority> authorities = this.authorityMapper.selectByIds(authIds).stream()
				.map(item -> new SimpleGrantedAuthority(item.getName())).collect(Collectors.toList());
		return new SecurityAdmin(employee, authorities);
	}

}
