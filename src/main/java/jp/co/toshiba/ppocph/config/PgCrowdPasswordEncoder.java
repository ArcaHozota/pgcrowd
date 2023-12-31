package jp.co.toshiba.ppocph.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * パスワードエンコーダ
 *
 * @author ArkamaHozota
 * @since 6.94
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PgCrowdPasswordEncoder implements PasswordEncoder {

	/**
	 * エンコーダ
	 */
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptVersion.$2A, 7);

	@Override
	public String encode(final CharSequence rawPassword) {
		return this.passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
		return this.passwordEncoder.matches(rawPassword, encodedPassword);
	}

}
