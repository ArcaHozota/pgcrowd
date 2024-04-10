package jp.co.sony.ppogah.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 役割情報転送クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class RoleDto implements Serializable {

	private static final long serialVersionUID = 3437599376410641198L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;
}
