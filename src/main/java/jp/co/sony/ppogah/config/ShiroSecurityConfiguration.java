package jp.co.sony.ppogah.config;

import javax.annotation.Resource;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.listener.PgCrowd2Realm;
import lombok.extern.log4j.Log4j2;

/**
 * Shiro設定クラス
 *
 * @author ArkamaHozota
 * @since 1.92
 */
@Log4j2
@Configuration
public class ShiroSecurityConfiguration {

	/**
	 * 項目登録認証クラス
	 */
	@Resource
	private PgCrowd2Realm pgCrowd2Realm;

	/**
	 * ウェブセキュリティフィルタを設置する
	 *
	 * @return DefaultWebSecurityManager
	 */
	@Bean
	protected DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition() {
		final DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
		defaultShiroFilterChainDefinition.addPathDefinition(
				PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/").concat(PgCrowd2URLConstants.URL_TO_LOGIN),
				"anon");
		defaultShiroFilterChainDefinition.addPathDefinition(
				PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/").concat(PgCrowd2URLConstants.URL_LOGIN), "anon");
		defaultShiroFilterChainDefinition.addPathDefinition(
				PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE.concat("/").concat(PgCrowd2URLConstants.URL_LOG_OUT),
				"anon");
		defaultShiroFilterChainDefinition.addPathDefinition("/static/**", "anon");
		defaultShiroFilterChainDefinition.addPathDefinition("/**", "authc");
		return defaultShiroFilterChainDefinition;
	}

	/**
	 * ウェブセキュリティマネージャーを設置する
	 *
	 * @return DefaultWebSecurityManager
	 */
	@Bean
	protected DefaultWebSecurityManager defaultWebSecurityManager() {
		final DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		final HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("md5");
		matcher.setHashIterations(2);
		this.pgCrowd2Realm.setCredentialsMatcher(matcher);
		defaultWebSecurityManager.setRealm(this.pgCrowd2Realm);
		log.info(PgCrowd2Constants.MESSAGE_SPRING_SECURITY);
		return defaultWebSecurityManager;
	}
}
