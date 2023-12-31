package jp.co.toshiba.ppocph.service;

import java.util.List;
import java.util.Map;

import jp.co.toshiba.ppocph.dto.RoleDto;
import jp.co.toshiba.ppocph.entity.PgAuth;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;

/**
 * 役割サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 4.46
 */
public interface IRoleService {

	/**
	 * 役割名称が重複するかどうかをチェックする
	 *
	 * @param name 役割名称
	 * @return true:重複する; false: 重複しない;
	 */
	ResultDto<String> check(String name);

	/**
	 * 権限を付与する
	 *
	 * @param paramMap パラメータ
	 * @return ResultDto<String>
	 */
	ResultDto<String> doAssignment(Map<String, List<Long>> paramMap);

	/**
	 * 付与された権限を表示する
	 *
	 * @param roleId 役割ID
	 * @return List<Long>
	 */
	List<Long> getAuthIdListByRoleId(Long roleId);

	/**
	 * 権限リストを取得する
	 *
	 * @return List<PgAuth>
	 */
	List<PgAuth> getAuthlist();

	/**
	 * 社員役割連携情報を取得する
	 *
	 * @param id 社員ID
	 * @return List<String>
	 */
	List<RoleDto> getEmployeeRolesById(Long id);

	/**
	 * キーワードによって役割情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Role>
	 */
	Pagination<RoleDto> getRolesByKeyword(Integer pageNum, String keyword);

	/**
	 * 役割IDによって情報を削除する
	 *
	 * @param roleId 役割ID
	 * @return ResultDto<String>
	 */
	ResultDto<String> removeById(Long roleId);

	/**
	 * 役割情報追加
	 *
	 * @param roleDto 役割情報転送クラス
	 */
	void save(RoleDto roleDto);

	/**
	 * 役割情報更新
	 *
	 * @param roleDto 役割情報転送クラス
	 */
	ResultDto<String> update(RoleDto roleDto);
}
