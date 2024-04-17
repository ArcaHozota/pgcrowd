package jp.co.sony.ppogah.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクトURLコンスタント
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PgCrowd2URLConstants {

	public static final String URL_EMPLOYEE_NAMESPACE = "/pgcrowd/employee";

	public static final String URL_ROLE_NAMESPACE = "/pgcrowd/role";

	public static final String URL_CHECK_NAME = "check";

	public static final String URL_TO_LOGIN = "login";

	public static final String URL_LOG_OUT = "logout";

	public static final String URL_LOGIN = "doLogin";

	public static final String URL_TO_REGISTER = "toSignUp";

	public static final String URL_REGISTER = "toroku";

	public static final String URL_TO_MAINMENU = "toMainmenu";

	public static final String URL_TO_ADDITION = "toAddition";

	public static final String URL_TO_EDITION = "toEdition";

	public static final String URL_TO_PAGES = "toPages";

	public static final String URL_PAGINATION = "pagination";

	public static final String URL_INFO_DELETE = "infoDelete";

	public static final String URL_INFO_RESTORE = "infoRestore";

	public static final String URL_INFO_INSERT = "infoSave";

	public static final String URL_INFO_UPDATE = "infoUpdate";

	public static final String URL_AUTH_LIST = "getAuthlist";

	public static final String URL_AUTH_ASSIGNED = "getAssignedAuth";

	public static final String URL_DO_ASSIGNMENT = "authAssignment";
}
