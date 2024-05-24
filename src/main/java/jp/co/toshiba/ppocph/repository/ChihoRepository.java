package jp.co.toshiba.ppocph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.toshiba.ppocph.entity.Chiho;

/**
 * 地方管理リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface ChihoRepository extends JpaRepository<Chiho, Long>, JpaSpecificationExecutor<Chiho> {
}
