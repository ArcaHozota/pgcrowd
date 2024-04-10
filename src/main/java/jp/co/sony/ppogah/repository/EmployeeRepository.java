package jp.co.sony.ppogah.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.sony.ppogah.entity.Employee;

/**
 * 社員管理リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
}
