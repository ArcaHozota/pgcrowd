package jp.co.sony.ppogah.entity;

import java.io.Serializable;

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
 * 社員役割連携エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "employee_role")
@EqualsAndHashCode(callSuper = false)
public final class EmployeeRole implements Serializable {

	private static final long serialVersionUID = 8049959021603519067L;

	/**
	 * 社員ID
	 */
	@Id
	private Long employeeId;

	/**
	 * 役割ID
	 */
	@Column(nullable = false)
	private Long roleId;
}
