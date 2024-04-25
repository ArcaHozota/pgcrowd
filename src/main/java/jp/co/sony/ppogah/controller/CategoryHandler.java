package jp.co.sony.ppogah.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 分類管理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.10
 */
@Getter
@Setter
@Namespace(PgCrowd2URLConstants.URL_CATEGORY_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/templates/categorykanri.ftl"),
		@Result(name = ERROR, location = "/templates/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/templates/admin-login.ftl") })
@ParentPackage("json-default")
@Controller
public class CategoryHandler extends ActionSupport {

	private static final long serialVersionUID = -6017782752547971104L;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * 分類管理画面初期表示
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_INIT_TEMPLATE)
	public String initial() {
		return SUCCESS;
	}

	/**
	 * 都市情報画面初期表示
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_TO_CITIES, results = {
			@Result(name = SUCCESS, location = "/templates/city-pages.ftl") })
	public String toCities() {
		return SUCCESS;
	}

	/**
	 * 地域情報画面初期表示
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_TO_DISTRICTS, results = {
			@Result(name = SUCCESS, location = "/templates/district-pages.ftl") })
	public String toDistricts() {
		return SUCCESS;
	}
}
