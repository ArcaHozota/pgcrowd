package jp.co.toshiba.ppocph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.toshiba.ppocph.entity.District;

/**
 * 地域管理リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface DistrictRepository extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {
}
