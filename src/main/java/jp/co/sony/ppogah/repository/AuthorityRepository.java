package jp.co.sony.ppogah.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.sony.ppogah.entity.Authority;

/**
 * 権限管理リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long>, JpaSpecificationExecutor<Authority> {
}
