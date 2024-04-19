package jp.co.sony.ppogah.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.dto.RoleDto;
import jp.co.sony.ppogah.entity.Authority;
import jp.co.sony.ppogah.service.IRoleService;
import jp.co.sony.ppogah.utils.Pagination;
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
	 * 役割サービスインターフェス
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
	 * 権限IDリスト
	 */
	private List<Long> authIds;

	/**
	 * 役割IDリスト
	 */
	private List<Long> roleIds;

	/**
	 * 名称重複チェック
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_CHECK_NAME)
	public String checkDuplicated() {
		final String roleName = this.getRequest().getParameter("roleName");
		final ResultDto<String> checkDuplicated = this.iRoleService.checkDuplicated(roleName);
		this.setResponsedJsondata(checkDuplicated);
		return NONE;
	}

	/**
	 * 権限情報を更新する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_DO_ASSIGNMENT, interceptorRefs = { @InterceptorRef("json") })
	public String doAssignment() {
		final List<Long> authIds2 = this.getAuthIds();
		final List<Long> roleIds2 = this.getRoleIds();
		final Map<String, List<Long>> paramMaps = new HashMap<>();
		paramMaps.put("authIds", authIds2);
		paramMaps.put("roleIds", roleIds2);
		final ResultDto<String> doAssignment = this.iRoleService.doAssignment(paramMaps);
		this.setResponsedJsondata(doAssignment);
		return NONE;
	}

	/**
	 * 付与された権限情報を取得する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_AUTH_ASSIGNED)
	public String getAssignedAuth() {
		final String fuyoId = this.getRequest().getParameter("fuyoId");
		final List<Long> authIdsById = this.iRoleService.getAuthIdsById(Long.parseLong(fuyoId));
		this.setResponsedJsondata(ResultDto.successWithData(authIdsById));
		return NONE;
	}

	/**
	 * 権限情報を取得する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_AUTH_LIST)
	public String getAuthlist() {
		final List<Authority> authList = this.iRoleService.getAuthList();
		this.setResponsedJsondata(ResultDto.successWithData(authList));
		return NONE;
	}

	/**
	 * getter for roleDto
	 *
	 * @return RoleDto
	 */
	private RoleDto getRoleDto() {
		this.roleDto.setId(this.getId());
		this.roleDto.setName(this.getName());
		return this.roleDto;
	}

	/**
	 * 役割情報を削除する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_INFO_DELETE)
	public String infoDelete() {
		final String roleId = this.getRequest().getParameter("roleId");
		final ResultDto<String> remove = this.iRoleService.remove(Long.parseLong(roleId));
		this.setResponsedJsondata(remove);
		return NONE;
	}

	/**
	 * 役割情報を保存する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_INFO_INSERT, interceptorRefs = { @InterceptorRef("json") })
	public String infoSave() {
		final RoleDto roleDto2 = this.getRoleDto();
		this.iRoleService.save(roleDto2);
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 役割情報を更新する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_INFO_UPDATE, interceptorRefs = { @InterceptorRef("json") })
	public String infoUpdate() {
		final RoleDto roleDto2 = this.getRoleDto();
		final ResultDto<String> update = this.iRoleService.update(roleDto2);
		this.setResponsedJsondata(update);
		return NONE;
	}

	/**
	 * 情報一覧画面初期表示する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_PAGINATION)
	public String pagination() {
		final String pageNum = this.getRequest().getParameter("pageNum");
		final String keyword = this.getRequest().getParameter("keyword");
		final Pagination<RoleDto> roleDtos = this.iRoleService.getRolesByKeyword(Integer.parseInt(pageNum), keyword);
		this.setResponsedJsondata(ResultDto.successWithData(roleDtos));
		return NONE;
	}

	/**
	 * @see org.apache.struts2.interceptor.ServletRequestAware.setServletRequest();
	 */
	@Override
	public void setServletRequest(final HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 情報一覧画面へ移動する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_TO_PAGES)
	public String toPages() {
		return SUCCESS;
	}
}
