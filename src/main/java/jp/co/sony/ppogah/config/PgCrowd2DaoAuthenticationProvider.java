package jp.co.sony.ppogah.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ログイン認証サービス
 *
 * @author ArkamaHozota
 * @since 2.88
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PgCrowd2DaoAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(final UserDetails userDetails,
			final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(
					this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
							PgCrowd2Constants.MESSAGE_SPRINGSECURITY_REQUIRED_AUTH));
		}
		final String presentedPassword = authentication.getCredentials().toString();
		if (!this.getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
			this.logger.debug("Failed to authenticate since password does not match stored value");
			throw new BadCredentialsException(
					this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
							PgCrowd2Constants.MESSAGE_SPRINGSECURITY_LOGINERROR4));
		}
	}
}
