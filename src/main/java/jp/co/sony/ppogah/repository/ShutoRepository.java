package jp.co.sony.ppogah.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.sony.ppogah.entity.Shuto;

/**
 * 州都管理リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface ShutoRepository extends JpaRepository<Shuto, Long>, JpaSpecificationExecutor<Shuto> {
}
