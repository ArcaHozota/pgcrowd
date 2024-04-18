package jp.co.sony.ppogah.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.sony.ppogah.dto.RoleAuthIds;
import jp.co.sony.ppogah.entity.RoleAuth;

/**
 * 役割権限連携リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface RoleAuthRepository extends JpaRepository<RoleAuth, RoleAuthIds>, JpaSpecificationExecutor<RoleAuth> {
}
