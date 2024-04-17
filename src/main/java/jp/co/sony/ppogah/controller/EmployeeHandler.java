package jp.co.sony.ppogah.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.util.List;

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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.common.PgCrowdConstants;
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
@Namespace(PgCrowd2URLConstants.URL_EMPLOYEE_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/WEB-INF/admin-pages.ftl"),
		@Result(name = ERROR, location = "/WEB-INF/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/WEB-INF/admin-login.ftl") })
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
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * 情報転送クラス
	 */
	private final EmployeeDto employeeDto = new EmployeeDto();

	/**
	 * ID
	 */
	private String id;

	/**
	 * アカウント
	 */
	private String loginAccount;

	/**
	 * ユーザ名称
	 */
	private String username;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * メール
	 */
	private String email;

	/**
	 * 生年月日
	 */
	private String dateOfBirth;

	/**
	 * 役割ID
	 */
	private String roleId;

	/**
	 * getter for employeeDto
	 *
	 * @return EmployeeDto
	 */
	public EmployeeDto getEmployeeDto() {
		this.employeeDto.setId(this.getId());
		this.employeeDto.setLoginAccount(this.getLoginAccount());
		this.employeeDto.setUsername(this.getUsername());
		this.employeeDto.setPassword(this.getPassword());
		this.employeeDto.setEmail(this.getEmail());
		this.employeeDto.setDateOfBirth(this.getDateOfBirth());
		this.employeeDto.setRoleId(this.getRoleId());
		return this.employeeDto;
	}

	/**
	 * 社員情報を削除する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_INFO_DELETE)
	public String infoDelete() {
		final String userId = this.getRequest().getParameter("userId");
		this.iEmployeeService.remove(Long.parseLong(userId));
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 情報をリセットする
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_INFO_RESTORE)
	public String infoRestore() {
		final String editId = this.getRequest().getParameter("editId");
		final EmployeeDto employeeDto2 = this.iEmployeeService.getEmployeeById(editId);
		this.setResponsedJsondata(ResultDto.successWithData(employeeDto2));
		return NONE;
	}

	/**
	 * 社員情報を保存する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_INFO_INSERT, interceptorRefs = { @InterceptorRef(value = "json") })
	public String infoSave() {
		this.iEmployeeService.save(this.getEmployeeDto());
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 社員情報を更新する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_INFO_UPDATE, interceptorRefs = { @InterceptorRef(value = "json") })
	public String infoUpdate() {
		final ResultDto<String> updateInfo = this.iEmployeeService.update(this.getEmployeeDto());
		this.setResponsedJsondata(updateInfo);
		return NONE;
	}

	/**
	 * ログイン画面初期表示する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_TO_LOGIN)
	public String initial() {
		return LOGIN;
	}

	/**
	 * ログインする
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_LOGIN, results = {
			@Result(name = SUCCESS, location = "/WEB-INF/mainmenu.ftl") })
	public String login() {
		final String loginAcct = this.getRequest().getParameter("loginAcct");
		final String userPswd = this.getRequest().getParameter("userPswd");
		final Boolean loginBoolean = this.iEmployeeService.login(loginAcct, userPswd);
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
	@Action(PgCrowd2URLConstants.URL_LOG_OUT)
	public String logout() {
		this.request.getSession().invalidate();
		return LOGIN;
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
		final Pagination<EmployeeDto> employees = this.iEmployeeService.getEmployeesByKeyword(Integer.parseInt(pageNum),
				keyword);
		this.setResponsedJsondata(ResultDto.successWithData(employees));
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
	 * 情報追加画面へ移動する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_TO_ADDITION, results = {
			@Result(name = SUCCESS, location = "/WEB-INF/admin-addinfo.ftl") })
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
	@Action(value = PgCrowd2URLConstants.URL_TO_EDITION, results = {
			@Result(name = SUCCESS, location = "/WEB-INF/admin-editinfo.ftl") })
	public String toEdition() {
		final String editId = this.getRequest().getParameter("editId");
		final EmployeeDto employeeDto2 = this.iEmployeeService.getEmployeeById(editId);
		final List<RoleDto> roleDtos = this.iRoleService.getRolesByEmployeeId(editId);
		ActionContext.getContext().put("employeeInfo", employeeDto2);
		ActionContext.getContext().put("employeeRoles", roleDtos);
		return SUCCESS;
	}

	/**
	 * メインメニューへ移動する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_TO_MAINMENU, results = {
			@Result(name = SUCCESS, location = "/WEB-INF/mainmenu.ftl") })
	public String toMainmenu() {
		return SUCCESS;
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

	/**
	 * 登録する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_REGISTER)
	public String toroku() {
		final String inputEmail = this.getRequest().getParameter("email");
		final String inputPassword = this.getRequest().getParameter("password");
		final String inputDate = this.getRequest().getParameter("dateOfBirth");
		final EmployeeDto employeeDto2 = new EmployeeDto();
		employeeDto2.setEmail(inputEmail);
		employeeDto2.setPassword(inputPassword);
		employeeDto2.setDateOfBirth(inputDate);
		final Boolean toroku = this.iEmployeeService.register(employeeDto2);
		if (Boolean.FALSE.equals(toroku)) {
			ActionContext.getContext().put("torokuMsg", PgCrowdConstants.MESSAGE_TOROKU_FAILURE);
		} else {
			ActionContext.getContext().put("torokuMsg", PgCrowdConstants.MESSAGE_TOROKU_SUCCESS);
		}
		ActionContext.getContext().put("registeredEmail", inputEmail);
		return LOGIN;
	}

	/**
	 * 登録画面へ移動する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_TO_REGISTER, results = {
			@Result(name = SUCCESS, location = "/WEB-INF/admin-toroku.ftl") })
	public String toSignUp() {
		return SUCCESS;
	}
}
