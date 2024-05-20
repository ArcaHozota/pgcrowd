package jp.co.sony.ppogah.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 地方情報転送クラス
 *
 * @author ArkamaHozota
 * @since 4.22
 */
@Data
public final class ChihoDto implements Serializable {

	private static final long serialVersionUID = -3521775427763684626L;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;
}
