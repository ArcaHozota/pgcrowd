package jp.co.sony.ppogah.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.sony.ppogah.entity.EmployeeRole;

/**
 * 社員役割連携リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface EmployeeRoleRepository
		extends JpaRepository<EmployeeRole, Long>, JpaSpecificationExecutor<EmployeeRole> {
}
