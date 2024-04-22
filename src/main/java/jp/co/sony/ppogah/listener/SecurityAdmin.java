package jp.co.sony.ppogah.listener;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import jp.co.sony.ppogah.entity.Employee;
import jp.co.sony.ppogah.utils.CommonProjectUtils;
import lombok.EqualsAndHashCode;

/**
 * User拡張クラス(SpringSecurity関連)
 *
 * @author ArkamaHozota
 * @since 2.00
 */
@EqualsAndHashCode(callSuper = false)
public final class SecurityAdmin extends User {

	private static final long serialVersionUID = 3827955098466369880L;

	private final Employee originalAdmin;

	SecurityAdmin(final Employee admin, final Collection<GrantedAuthority> authorities) {
		super(admin.getLoginAccount(), admin.getPassword(), true, true, true, true, authorities);
		this.originalAdmin = admin;
		this.originalAdmin.setPassword(CommonProjectUtils.EMPTY_STRING);
	}

	public Employee getOriginalAdmin() {
		return this.originalAdmin;
	}
}
