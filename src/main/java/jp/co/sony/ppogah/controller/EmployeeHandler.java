package jp.co.sony.ppogah.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.dto.EmployeeDto;
import jp.co.sony.ppogah.service.IEmployeeService;
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
@Results({ @Result(name = "error", location = "/WEB-INF/system-error.jsp"),
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
	 * リクエスト
	 */
	private transient HttpServletRequest request;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<EmployeeDto> responsedJsondata;

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
		final String loginAccount = this.request.getParameter("loginAcct");
		final String password = this.request.getParameter("userPswd");
		final Boolean loginBoolean = this.iEmployeeService.login(loginAccount, password);
		if (Boolean.FALSE.equals(loginBoolean)) {
			return LOGIN;
		}
		return SUCCESS;
	}

	@Override
	public void setServletRequest(final HttpServletRequest request) {
		this.request = request;
	}
}
