package jp.co.sony.ppogah.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sony.ppogah.entity.District;

/**
 * 地域管理リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface DistrictRepository extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {

	/**
	 * 州都によって検索する
	 *
	 * @param name     キーワード
	 * @param pageable ページング検索
	 * @return Page<District>
	 */
	@Query(value = "select ds from District as ds inner join City as cy on cy.districtId = ds.id "
			+ "where ds.deleteFlg= 'approved' and (ds.name like :name or ds.chiho like :name or cy.name like :name)")
	Page<District> findByShutoLike(@Param("name") String name, Pageable pageable);
}
