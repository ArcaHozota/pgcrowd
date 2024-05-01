package jp.co.sony.ppogah.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 社員エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "employees")
@EqualsAndHashCode(callSuper = false)
public final class Employee implements Serializable {

	private static final long serialVersionUID = -7478708453453699683L;

	/**
	 * ID
	 */
	@Id
	private Long id;

	/**
	 * アカウント
	 */
	@Column(nullable = false)
	private String loginAccount;

	/**
	 * ユーザ名称
	 */
	@Column(nullable = false)
	private String username;

	/**
	 * パスワード
	 */
	@Column(nullable = false)
	private String password;

	/**
	 * メール
	 */
	@Column(nullable = false)
	private String email;

	/**
	 * 生年月日
	 */
	@Column(nullable = false)
	private LocalDate dateOfBirth;

	/**
	 * 作成時間
	 */
	@Column(nullable = false)
	private LocalDateTime createdTime;

	/**
	 * 論理削除フラグ
	 */
	@Column(nullable = false)
	private String deleteFlg;
}
