package jp.co.toshiba.ppocph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.toshiba.ppocph.entity.EmployeeRole;

/**
 * 社員役割連携リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface EmployeeRoleRepository
		extends JpaRepository<EmployeeRole, Long>, JpaSpecificationExecutor<EmployeeRole> {
}
