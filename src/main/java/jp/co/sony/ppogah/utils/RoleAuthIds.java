package jp.co.sony.ppogah.utils;

import java.io.Serializable;

import lombok.Data;

/**
 * 役割権限情報ID
 *
 * @author ArkamaHozota
 * @since 5.76
 */
@Data
public final class RoleAuthIds implements Serializable {

	private static final long serialVersionUID = -297785511370318383L;

	/**
	 * 権限ID
	 */
	private Long roleId;

	/**
	 * 役割ID
	 */
	private Long authId;
}
