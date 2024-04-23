package jp.co.sony.ppogah.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.io.IOException;
import java.io.InputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.utils.CommonProjectUtils;
import jp.co.sony.ppogah.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 共通SVGイメージハンドラ
 *
 * @author ArkamaHozota
 * @since 1.10
 */
@Getter
@Setter
@Namespace(PgCrowd2URLConstants.URL_SVG_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/WEB-INF/mainmenu.ftl"),
		@Result(name = ERROR, location = "/WEB-INF/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/WEB-INF/admin-login.ftl") })
@ParentPackage("json-default")
@Controller
public class SvgHandler extends ActionSupport {

	private static final long serialVersionUID = -171237033831060185L;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * ResourceLoader
	 */
	private final ResourceLoader resourceLoader = new DefaultResourceLoader();

	/**
	 * メインメニューアイコン取得する
	 *
	 * @param svgSource ソースパス
	 * @param response  リスポンス
	 * @throws IOException
	 */
	@Action(PgCrowd2URLConstants.URL_MAINMENU_ICONS)
	public String getSvgImage() throws IOException {
		final String svgSource = ActionContext.getContext().getServletRequest().getParameter("icons");
		final Resource resource = this.getResourceLoader().getResource("classpath:static/image/icons/" + svgSource);
		final InputStream inputStream = resource.getInputStream();
		final byte[] buffer = new byte[(int) resource.getFile().length()];
		inputStream.read(buffer);
		ActionContext.getContext().getServletResponse().setContentType("image/svg+xml");
		ActionContext.getContext().getServletResponse().setCharacterEncoding(CommonProjectUtils.CHARSET_UTF8.name());
		ActionContext.getContext().getServletResponse().getOutputStream().write(buffer);
		return SUCCESS;
	}

	/**
	 * 都市アイコン取得する
	 *
	 * @param svgSource ソースパス
	 * @param response  リスポンス
	 * @throws IOException
	 */
	@Action(value = PgCrowd2URLConstants.URL_CITY_FLAGS, results = {
			@Result(name = SUCCESS, location = "/WEB-INF/city-pages.ftl") })
	public String getSvgImageCity() throws IOException {
		final String svgSource = ActionContext.getContext().getServletRequest().getParameter("flags");
		final Resource resource = this.getResourceLoader().getResource("classpath:static/image/flags/" + svgSource);
		final InputStream inputStream = resource.getInputStream();
		final byte[] buffer = new byte[(int) resource.getFile().length()];
		inputStream.read(buffer);
		ActionContext.getContext().getServletResponse().setContentType("image/svg+xml");
		ActionContext.getContext().getServletResponse().setCharacterEncoding(CommonProjectUtils.CHARSET_UTF8.name());
		ActionContext.getContext().getServletResponse().getOutputStream().write(buffer);
		return SUCCESS;
	}

	/**
	 * 地域アイコン取得する
	 *
	 * @param svgSource ソースパス
	 * @param response  リスポンス
	 * @throws IOException
	 */
	@Action(value = PgCrowd2URLConstants.URL_DISTRICT_FLAGS, results = {
			@Result(name = SUCCESS, location = "/WEB-INF/district-pages.ftl") })
	public String getSvgImageDistrict() throws IOException {
		final String svgSource = ActionContext.getContext().getServletRequest().getParameter("flags");
		final Resource resource = this.getResourceLoader()
				.getResource("classpath:static/image/flags/prefectures/" + svgSource);
		final InputStream inputStream = resource.getInputStream();
		final byte[] buffer = new byte[(int) resource.getFile().length()];
		inputStream.read(buffer);
		ActionContext.getContext().getServletResponse().setContentType("image/svg+xml");
		ActionContext.getContext().getServletResponse().setCharacterEncoding(CommonProjectUtils.CHARSET_UTF8.name());
		ActionContext.getContext().getServletResponse().getOutputStream().write(buffer);
		return SUCCESS;
	}
}
