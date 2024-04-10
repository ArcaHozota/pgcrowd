package jp.co.sony.ppogah.service;

import jp.co.sony.ppogah.dto.EmployeeDto;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;

/**
 * 社員サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface IEmployeeService {

	/**
	 * ログインアカウントを重複するかどうかを確認する
	 *
	 * @param loginAccount ログインアカウント
	 */
	ResultDto<String> check(String loginAccount);

	/**
	 * ユーザ編集権限チェック
	 *
	 * @param id 社員ID
	 * @return Boolean
	 */
	Boolean checkEdition(Long id);

	/**
	 * IDによって社員情報を取得する
	 *
	 * @param id 社員ID
	 * @return Employee
	 */
	EmployeeDto getEmployeeById(Long id);

	/**
	 * キーワードによって社員情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Employee>
	 */
	Pagination<EmployeeDto> getEmployeesByKeyword(Integer pageNum, String keyword, Long userId);

	/**
	 * 社員登録
	 *
	 * @param employeeDto 社員情報DTO
	 * @return Boolean
	 */
	Boolean register(EmployeeDto employeeDto);

	/**
	 * 社員情報削除
	 *
	 * @param userId 社員ID
	 */
	void removeById(Long userId);

	/**
	 * 社員情報追加
	 *
	 * @param employeeDto 社員情報転送クラス
	 */
	void save(EmployeeDto employeeDto);

	/**
	 * 社員情報行更新
	 *
	 * @param employeeDto 社員情報転送クラス
	 */
	ResultDto<String> update(EmployeeDto employeeDto);
}
