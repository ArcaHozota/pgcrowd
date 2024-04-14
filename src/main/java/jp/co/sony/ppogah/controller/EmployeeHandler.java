package jp.co.sony.ppogah.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.dto.EmployeeDto;
import jp.co.sony.ppogah.dto.RoleDto;
import jp.co.sony.ppogah.service.IEmployeeService;
import jp.co.sony.ppogah.service.IRoleService;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 社員情報処理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.10
 */
@Getter
@Setter
@Namespace("/pgcrowd/employee")
@Results({ @Result(name = "success", location = "/WEB-INF/admin-pages.jsp"),
		@Result(name = "error", location = "/WEB-INF/system-error.jsp"),
		@Result(name = "none", type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = "login", location = "/WEB-INF/admin-login.jsp") })
@ParentPackage("json-default")
@Controller
public class EmployeeHandler extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = -6017782752547971104L;

	/**
	 * 社員サービスインターフェス
	 */
	@Resource
	private IEmployeeService iEmployeeService;

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
	private transient ResultDto<Object> responsedJsondata;

	/**
	 * ログアウトする
	 *
	 * @return String
	 */
	@Action("checkDelete")
	public String checkDelete() {
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * ログイン画面初期表示する
	 *
	 * @return String
	 */
	@Action("login")
	public String initial() {
		return LOGIN;
	}

	/**
	 * ログインする
	 *
	 * @return String
	 */
	@Action(value = "doLogin", results = { @Result(name = "success", location = "/WEB-INF/mainmenu.jsp") })
	public String login() {
		final String loginAccount = this.getRequest().getParameter("loginAcct");
		final String password = this.getRequest().getParameter("userPswd");
		final Boolean loginBoolean = this.iEmployeeService.login(loginAccount, password);
		if (Boolean.FALSE.equals(loginBoolean)) {
			return LOGIN;
		}
		return SUCCESS;
	}

	/**
	 * ログアウトする
	 *
	 * @return String
	 */
	@Action("logout")
	public String logout() {
		this.request.getSession().invalidate();
		return LOGIN;
	}

	/**
	 * 情報一覧画面初期表示する
	 *
	 * @return String
	 */
	@Action("pagination")
	public String pagination() {
		final String pageNum = this.getRequest().getParameter("pageNum");
		final String keyword = this.getRequest().getParameter("keyword");
		final Pagination<EmployeeDto> employees = this.iEmployeeService.getEmployeesByKeyword(Integer.parseInt(pageNum),
				keyword);
		this.setResponsedJsondata(ResultDto.successWithData(employees));
		return NONE;
	}

	@Override
	public void setServletRequest(final HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 情報追加画面へ移動する
	 *
	 * @return String
	 */
	@Action(value = "toAddition", results = { @Result(name = "success", location = "/WEB-INF/admin-addinfo.jsp") })
	public String toAddition() {
		final List<RoleDto> roleDtos = this.iRoleService.getRolesByEmployeeId(null);
		ActionContext.getContext().put("employeeRoles", roleDtos);
		return SUCCESS;
	}

	/**
	 * 情報更新画面へ移動する
	 *
	 * @return String
	 */
	@Action(value = "toEdition", results = { @Result(name = "success", location = "/WEB-INF/admin-editinfo.jsp") })
	public String toEdition() {
		final String editId = this.getRequest().getParameter("editId");
		final EmployeeDto employeeDto = this.iEmployeeService.getEmployeeById(editId);
		final List<RoleDto> roleDtos = this.iRoleService.getRolesByEmployeeId(editId);
		ActionContext.getContext().put("employeeInfo", employeeDto);
		ActionContext.getContext().put("employeeRoles", roleDtos);
		return SUCCESS;
	}

	/**
	 * メインメニューへ移動する
	 *
	 * @return String
	 */
	@Action(value = "toMainmenu", results = { @Result(name = "success", location = "/WEB-INF/mainmenu.jsp") })
	public String toMainmenu() {
		return SUCCESS;
	}

	/**
	 * 情報一覧画面へ移動する
	 *
	 * @return String
	 */
	@Action("toPages")
	public String toPages() {
		return SUCCESS;
	}
}
