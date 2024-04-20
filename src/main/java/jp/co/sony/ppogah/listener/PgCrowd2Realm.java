package jp.co.sony.ppogah.listener;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import jp.co.sony.ppogah.entity.Employee;
import jp.co.sony.ppogah.service.IEmployeeService;

/**
 * 項目登録認証クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Component
public class PgCrowd2Realm extends AuthorizingRealm {

	/**
	 * 社員サービスインターフェス
	 */
	@Resource
	private IEmployeeService iEmployeeService;

	/**
	 * 権限確認メソッド
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
			throws AuthenticationException {
		final String account = token.getPrincipal().toString();
		final Optional<Employee> employee = this.iEmployeeService.getEmployeeByAccount(account);
		if (employee.isPresent()) {
			return new SimpleAuthenticationInfo(token.getPrincipal(), employee.get().getPassword(),
					ByteSource.Util.bytes("pgcrowd2"), account);
		}
		return null;
	}

	/**
	 * 権限付与メソッド
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
		return null;
	}

}
