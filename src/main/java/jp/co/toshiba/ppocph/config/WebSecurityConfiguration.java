package jp.co.toshiba.ppocph.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import jakarta.annotation.Resource;
import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.common.PgCrowdURIConstants;
import jp.co.toshiba.ppocph.listener.PgCrowdUserDetailsService;
import jp.co.toshiba.ppocph.utils.PgCrowdUtils;
import lombok.extern.log4j.Log4j2;

/**
 * SpringSecurity配置クラス
 *
 * @author ArkamaHozota
 * @since 5.99
 */
@Log4j2
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

	/**
	 * ログインサービス
	 */
	@Resource
	private PgCrowdUserDetailsService pgCrowdUserDetailsService;

	@Bean
	protected AuthenticationManager authenticationManager(final AuthenticationManagerBuilder auth) {
		return auth.authenticationProvider(this.daoAuthenticationProvider()).getObject();
	}

	@Bean
	protected DaoAuthenticationProvider daoAuthenticationProvider() {
		final PgCrowdDaoAuthenticationProvider daoAuthenticationProvider = new PgCrowdDaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.pgCrowdUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(new PgCrowdPasswordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/static/**").permitAll()
						.requestMatchers(PgCrowdURIConstants.EMPLOYEE_TOPAGES, PgCrowdURIConstants.EMPLOYEE_PAGINATION)
						.hasAuthority("employee%retrieve")
						.requestMatchers(PgCrowdURIConstants.EMPLOYEE_ADDITION, PgCrowdURIConstants.EMPLOYEE_EDITION,
								PgCrowdURIConstants.EMPLOYEE_INSERT, PgCrowdURIConstants.EMPLOYEE_UPDATE)
						.hasAuthority("employee%edition").requestMatchers(PgCrowdURIConstants.EMPLOYEE_DELETE)
						.hasAuthority("employee%delete")
						.requestMatchers(PgCrowdURIConstants.ROLE_TOPAGES, PgCrowdURIConstants.ROLE_PAGINATION,
								PgCrowdURIConstants.ROLE_GETASSIGNED)
						.hasAuthority("role%retrieve")
						.requestMatchers(PgCrowdURIConstants.ROLE_INSERT, PgCrowdURIConstants.ROLE_UPDATE)
						.hasAuthority("role%edition")
						.requestMatchers(PgCrowdURIConstants.ROLE_ASSIGNMENT, PgCrowdURIConstants.ROLE_DELETE)
						.hasAuthority("role%delete").anyRequest().authenticated())
				.csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())).exceptionHandling(handling -> {
					handling.authenticationEntryPoint((request, response, authenticationException) -> {
						final ResponseLoginDto responseResult = new ResponseLoginDto(HttpStatus.UNAUTHORIZED.value(),
								authenticationException.getMessage());
						PgCrowdUtils.renderString(response, responseResult);
					});
					handling.accessDeniedHandler((request, response, accessDeniedException) -> {
						final ResponseLoginDto responseResult = new ResponseLoginDto(HttpStatus.FORBIDDEN.value(),
								PgCrowdConstants.MESSAGE_SPRINGSECURITY_REQUIREDAUTH);
						PgCrowdUtils.renderString(response, responseResult);
					});
				})
				.formLogin(formLogin -> formLogin.loginPage("/pgcrowd/employee/login")
						.loginProcessingUrl("/pgcrowd/employee/do/login").defaultSuccessUrl("/pgcrowd/to/mainmenu")
						.permitAll().usernameParameter("loginAcct").passwordParameter("userPswd"))
				.logout(logout -> logout.logoutUrl("/pgcrowd/employee/logout")
						.logoutSuccessUrl("/pgcrowd/employee/login"))
				.httpBasic(Customizer.withDefaults());
		log.info(PgCrowdConstants.MESSAGE_SPRING_SECURITY);
		return httpSecurity.build();
	}
}
