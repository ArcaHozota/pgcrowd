package jp.co.sony.ppogah.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
@EnableWebSecurity
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
		http.authorizeRequests(requests -> requests.antMatchers(PgCrowd2URLConstants.URL_STATIC_RESOURCE).permitAll()
				.anyRequest().authenticated()).csrf(
						csrf -> csrf
								.ignoringRequestMatchers(new AntPathRequestMatcher(
										PgCrowd2URLConstants.URL_STATIC_RESOURCE, RequestMethod.GET.toString()))
								.csrfTokenRepository(new CookieCsrfTokenRepository()))
				.exceptionHandling(
						handling -> handling.authenticationEntryPoint((request, response, authenticationException) -> {
							final ResponseLoginDto responseResult = new ResponseLoginDto(
									HttpStatus.UNAUTHORIZED.value(), authenticationException.getMessage());
							CommonProjectUtils.renderString(response, responseResult);
						}).accessDeniedHandler((request, response, accessDeniedException) -> {
							final ResponseLoginDto responseResult = new ResponseLoginDto(HttpStatus.FORBIDDEN.value(),
									PgCrowd2Constants.MESSAGE_SPRINGSECURITY_REQUIRED_AUTH);
							CommonProjectUtils.renderString(response, responseResult);
						}))
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

//	@Bean
//	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(
//				authorize -> authorize.requestMatchers(CrowdProjectURLConstants.URL_STATIC_RESOURCE).permitAll()
//						.requestMatchers(CrowdProjectURLConstants.URL_EMPLOYEE_TO_PAGES,
//								CrowdProjectURLConstants.URL_EMPLOYEE_PAGINATION)
//						.hasAuthority("employee%retrieve")
//						.requestMatchers(CrowdProjectURLConstants.URL_EMPLOYEE_INFOSAVE,
//								CrowdProjectURLConstants.URL_EMPLOYEE_INFOUPD,
//								CrowdProjectURLConstants.URL_EMPLOYEE_TO_ADDITION,
//								CrowdProjectURLConstants.URL_EMPLOYEE_TO_EDITION)
//						.hasAuthority("employee%edition").requestMatchers(CrowdProjectURLConstants.URL_EMPLOYEE_DELETE)
//						.hasAuthority("employee%delete").requestMatchers(CrowdProjectURLConstants.URL_ROLE_TO_PAGES)
//						.hasAuthority("role%retrieve").anyRequest().authenticated())
//				.csrf(csrf -> csrf.ignoringRequestMatchers(CrowdProjectURLConstants.URL_STATIC_RESOURCE)
//						.csrfTokenRepository(new CookieCsrfTokenRepository()))
//				.exceptionHandling().authenticationEntryPoint((request, response, authenticationException) -> {
//					final ResponseLoginDto responseResult = new ResponseLoginDto(HttpStatus.UNAUTHORIZED.value(),
//							authenticationException.getMessage());
//					CrowdProjectUtils.renderString(response, responseResult);
//				}).accessDeniedHandler((request, response, accessDeniedException) -> {
//					final ResponseLoginDto responseResult = new ResponseLoginDto(HttpStatus.FORBIDDEN.value(),
//							CrowdProjectConstants.MESSAGE_SPRINGSECURITY_REQUIREDAUTH);
//					CrowdProjectUtils.renderString(response, responseResult);
//				}).and().formLogin(formLogin -> {
//					formLogin.loginPage("/pgcrowd/employee/login").loginProcessingUrl("/pgcrowd/employee/do/login")
//							.defaultSuccessUrl("/pgcrowd/to/mainmenu").permitAll().usernameParameter("loginAcct")
//							.passwordParameter("userPswd");
//					try {
//						formLogin.and().logout(logout -> logout.logoutUrl("/pgcrowd/employee/logout")
//								.logoutSuccessUrl("/pgcrowd/employee/login"));
//					} catch (final Exception e) {
//						throw new CrowdProjectException(CrowdProjectConstants.MESSAGE_STRING_FATAL_ERROR);
//					}
//				}).httpBasic(Customizer.withDefaults());
//		log.info(PgCrowd2Constants.MESSAGE_SPRING_SECURITY);
//		return http.build();
//	}
}
