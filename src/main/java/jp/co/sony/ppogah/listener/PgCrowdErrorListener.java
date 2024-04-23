package jp.co.sony.ppogah.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.DefaultDispatcherErrorHandler;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.config.ResponseLoginDto;
import jp.co.sony.ppogah.utils.CommonProjectUtils;
import lombok.extern.log4j.Log4j2;

/**
 * 共通エラーリスナー
 *
 * @author ArkamaHozota
 * @since 2.21
 */
@Log4j2
public class PgCrowdErrorListener extends DefaultDispatcherErrorHandler {

	/**
	 * エラーリスポンスを転送
	 */
	@Override
	protected void sendErrorResponse(final HttpServletRequest request, final HttpServletResponse response,
			final int code, final Exception e) {
		try {
			// WW-1977: Only put errors in the request when code is a 500 error
			if ((code == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) || (code == HttpServletResponse.SC_FORBIDDEN)) {
				// WW-4103: Only logs error when application error occurred, not Struts error
				log.error("Exception occurred during processing request: {}", e.getMessage(), e);
			}
			final ResponseLoginDto responseResult = new ResponseLoginDto(code,
					PgCrowd2Constants.MESSAGE_SPRINGSECURITY_REQUIRED_AUTH);
			CommonProjectUtils.renderString(response, responseResult);
		} catch (final IllegalStateException ise) {
			// Log illegalstate instead of passing unrecoverable exception to calling thread
			log.warn("Unable to send error response, code: {}; isCommited: {};", code, response.isCommitted(), ise);
		}
	}
}
