package jp.co.sony.ppogah.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.listener.PgCrowd2UserDetailsService;
import jp.co.sony.ppogah.listener.PgCrowdAccessDeniedHandler;
import jp.co.sony.ppogah.utils.CommonProjectUtils;
import lombok.extern.log4j.Log4j2;

/**
 * SpringSecurity配置クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Log4j2
@Configuration
@Order(1)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 * ログインサービス
	 */
	@Resource
	private PgCrowd2UserDetailsService pgCrowd2UserDetailsService;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.pgCrowd2UserDetailsService)
				.passwordEncoder(new BCryptPasswordEncoder(BCryptVersion.$2Y, 7));
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		log.info(PgCrowd2Constants.MESSAGE_SPRING_SECURITY);
		http.authorizeRequests(
				requests -> requests.antMatchers(PgCrowd2URLConstants.URL_STATIC_RESOURCE).permitAll().anyRequest()
						.authenticated())
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(
								new AntPathRequestMatcher(PgCrowd2URLConstants.URL_STATIC_RESOURCE,
										RequestMethod.GET.toString()),
								new AntPathRequestMatcher(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
										.concat(PgCrowd2URLConstants.URL_TO_LOGIN), RequestMethod.GET.toString()),
								new AntPathRequestMatcher(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
										.concat(PgCrowd2URLConstants.URL_LOGIN), RequestMethod.POST.toString()),
								new AntPathRequestMatcher(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
										.concat(PgCrowd2URLConstants.URL_LOG_OUT), RequestMethod.POST.toString()))
						.csrfTokenRepository(new CookieCsrfTokenRepository()))
				.exceptionHandling(
						handling -> handling.authenticationEntryPoint((request, response, authenticationException) -> {
							final ResponseLoginDto responseResult = new ResponseLoginDto(
									HttpStatus.UNAUTHORIZED.value(), authenticationException.getMessage());
							CommonProjectUtils.renderString(response, responseResult);
						}).accessDeniedHandler(new PgCrowdAccessDeniedHandler("/WEB-INF/system-error.ftl")))
				.formLogin(login -> login
						.loginPage(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowd2URLConstants.URL_TO_LOGIN))
						.loginProcessingUrl(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowd2URLConstants.URL_LOGIN))
						.defaultSuccessUrl(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowd2URLConstants.URL_TO_MAINMENU))
						.permitAll().usernameParameter("loginAcct").passwordParameter("userPswd"))
				.logout(logout -> logout
						.logoutUrl(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowd2URLConstants.URL_LOG_OUT))
						.logoutSuccessUrl(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowd2URLConstants.URL_TO_LOGIN)));
	}

}
