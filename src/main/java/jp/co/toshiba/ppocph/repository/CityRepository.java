package jp.co.toshiba.ppocph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.toshiba.ppocph.entity.City;

/**
 * 都市管理リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {
}
