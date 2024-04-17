package jp.co.sony.ppogah.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.dto.RoleDto;
import jp.co.sony.ppogah.service.IRoleService;
import jp.co.sony.ppogah.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 役割情報処理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.62
 */
@Getter
@Setter
@Namespace(PgCrowd2URLConstants.URL_ROLE_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/WEB-INF/role-pages.ftl"),
		@Result(name = ERROR, location = "/WEB-INF/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/WEB-INF/admin-login.ftl") })
@ParentPackage("json-default")
@Controller
public class RoleHandler extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 7483637181412284924L;

	/**
	 * 社員サービスインターフェス
	 */
	@Resource
	private IRoleService iRoleService;

	/**
	 * リクエスト
	 */
	private transient HttpServletRequest request;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * 情報転送クラス
	 */
	private final RoleDto roleDto = new RoleDto();

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * getter for roleDto
	 *
	 * @return RoleDto
	 */
	public RoleDto getRoleDto() {
		this.roleDto.setId(this.getId());
		this.roleDto.setName(this.getName());
		return this.roleDto;
	}

	/**
	 * @see org.apache.struts2.interceptor.ServletRequestAware.setServletRequest();
	 */
	@Override
	public void setServletRequest(final HttpServletRequest request) {
		this.request = request;
	}
}
