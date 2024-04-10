package jp.co.sony.ppogah.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import jp.co.sony.ppogah.utils.RoleAuthIds;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 役割権限連携エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "role_auth")
@IdClass(RoleAuthIds.class)
@EqualsAndHashCode(callSuper = false)
public final class RoleAuth implements Serializable {

	private static final long serialVersionUID = 4995165208601855074L;

	/**
	 * 権限ID
	 */
	@Id
	private Long authId;

	/**
	 * 役割ID
	 */
	@Id
	private Long roleId;
}
