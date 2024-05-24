package jp.co.toshiba.ppocph.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 地方エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "chihos")
@EqualsAndHashCode(callSuper = false)
public class Chiho implements Serializable {

	private static final long serialVersionUID = -1347501624084143125L;

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
	 * 地方地域関連
	 */
	@OneToMany(mappedBy = "chiho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<District> districts;
}
