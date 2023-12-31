package jp.co.toshiba.ppocph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.toshiba.ppocph.entity.EmployeeEx;

/**
 * 社員役割連携リポジトリ
 *
 * @author ArkamaHozota
 * @since 5.00
 */
public interface EmployeeExRepository extends JpaRepository<EmployeeEx, Long>, JpaSpecificationExecutor<EmployeeEx> {
}
