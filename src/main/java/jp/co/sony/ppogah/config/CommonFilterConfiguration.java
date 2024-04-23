package jp.co.sony.ppogah.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * CSPFilter配置クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Configuration
public class CommonFilterConfiguration {

	@Bean
	protected FilterRegistrationBean<CSPFilter> cspFilterRegistrationBean() {
		// 创建一个过滤器注册对象
		final FilterRegistrationBean<CSPFilter> registrationBean = new FilterRegistrationBean<>();
		// 设置过滤器实例
		registrationBean.setFilter(new CSPFilter());
		// 设置过滤器的名称
		registrationBean.setName("CSPFilter");
		// 设置过滤器的顺序
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		// 设置过滤器的 URL 匹配模式
		registrationBean.addUrlPatterns("/*");
		// 返回过滤器注册对象
		return registrationBean;
	}
}
