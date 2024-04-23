package jp.co.sony.ppogah.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 共通フィルタ
 *
 * @author ArkamaHozota
 * @since 2.29
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CSPFilter implements Filter {

	@Override
	public void destroy() {
		// 销毁过滤器
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		// 对所有的响应添加 CSP 头
		final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Content-Security-Policy",
				"script-src 'nonce-Ytvk0lE3pg1BL713YR9i89Kn' 'strict-dynamic'");
		chain.doFilter(request, response);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// 初始化过滤器
	}
}
