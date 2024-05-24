package jp.co.toshiba.ppocph.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 都市エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "cities")
@EqualsAndHashCode(callSuper = false)
public class City implements Serializable {

	private static final long serialVersionUID = -4552061040586886910L;

	/**
	 * ID
	 */
	@Id
	private Long id;

	/**
	 * 名称
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * 読み方
	 */
	private String pronunciation;

	/**
	 * 人口数量
	 */
	@Column(nullable = false)
	private Long population;

	/**
	 * 市町村旗
	 */
	private String cityFlag;

	/**
	 * 地域ID
	 */
	@Column(nullable = false)
	private Long districtId;

	/**
	 * 論理削除フラグ
	 */
	@Column(nullable = false)
	private String deleteFlg;

	/**
	 * 都市地域関連
	 */
	@ManyToOne
	@JoinColumn(name = "districtId", insertable = false, updatable = false)
	private District district;

	/**
	 * 州都地域関連
	 */
	@OneToOne(mappedBy = "shutoCity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private District shutoDistrict;
}
