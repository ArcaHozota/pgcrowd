package jp.co.toshiba.ppocph.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 地域エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "districts")
@EqualsAndHashCode(callSuper = false)
public class District implements Serializable {

	private static final long serialVersionUID = -3956143435944525913L;

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
	 * 地方ID
	 */
	@Column(nullable = false)
	private Long chihoId;

	/**
	 * 都道府県旗
	 */
	private String districtFlag;

	/**
	 * 州都ID
	 */
	@Column(nullable = false)
	private Long shutoId;

	/**
	 * 論理削除フラグ
	 */
	@Column(nullable = false)
	private String deleteFlg;

	/**
	 * 地域都市関連
	 */
	@OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<City> cities;

	/**
	 * 地域地方関連
	 */
	@ManyToOne
	@JoinColumn(name = "chihoId", insertable = false, updatable = false)
	private Chiho chiho;

	/**
	 * 地域州都関連
	 */
	@OneToOne
	@JoinColumn(name = "shutoId", insertable = false, updatable = false)
	private City shutoCity;
}
