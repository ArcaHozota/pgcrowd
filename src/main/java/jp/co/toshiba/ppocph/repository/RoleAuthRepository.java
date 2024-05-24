package jp.co.toshiba.ppocph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.toshiba.ppocph.dto.RoleAuthIds;
import jp.co.toshiba.ppocph.entity.RoleAuth;

/**
 * 役割権限連携リポジトリ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface RoleAuthRepository extends JpaRepository<RoleAuth, RoleAuthIds>, JpaSpecificationExecutor<RoleAuth> {
}
