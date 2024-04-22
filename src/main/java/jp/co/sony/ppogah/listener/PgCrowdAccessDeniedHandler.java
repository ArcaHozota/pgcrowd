package jp.co.sony.ppogah.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.config.ResponseLoginDto;
import jp.co.sony.ppogah.utils.CommonProjectUtils;

/**
 * AccessDeniedHandler拡張クラス(SpringSecurity関連)
 *
 * @author ArkamaHozota
 * @since 2.10
 */
public class PgCrowdAccessDeniedHandler extends ResponseLoginDto implements AccessDeniedHandler {

	private static final long serialVersionUID = -4129588644087602079L;

	/**
	 * システムエラー画面
	 */
	private String errorPage;

	/**
	 * コンストラクタ
	 *
	 * @param code    ステータス
	 * @param message メッセージ
	 */
	public PgCrowdAccessDeniedHandler(final Integer code, final String message) {
		super(code, message);
	}

	/**
	 * コンストラクタ
	 *
	 * @param errorPage システムエラー画面
	 */
	public PgCrowdAccessDeniedHandler(final String errorPage) {
		this.errorPage = errorPage;
	}

	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
			final AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// 自定义逻辑
		if (!response.isCommitted()) {
			if (this.errorPage != null) {
				// 重定向到错误页面
				response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
				response.sendRedirect(this.errorPage);
			} else {
				// 发送错误响应
				final ResponseLoginDto responseResult = new PgCrowdAccessDeniedHandler(HttpStatus.FORBIDDEN.value(),
						PgCrowd2Constants.MESSAGE_SPRINGSECURITY_REQUIRED_AUTH);
				CommonProjectUtils.renderString(response, responseResult);
			}
		}
	}

	/**
	 * setter of errorPage
	 *
	 * @param errorPage
	 */
	public void setErrorPage(final String errorPage) {
		this.errorPage = errorPage;
	}
}
