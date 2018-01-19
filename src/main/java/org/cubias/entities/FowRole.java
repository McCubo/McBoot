package org.cubias.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FowRole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer rolId;

	private String rolName;

	protected FowRole() {
	}

	public FowRole(String rolName) {
		this.rolName = rolName;
	}

	@Override
	public String toString() {
		return "FowRole [rolId=" + rolId + ", rolName=" + rolName + "]";
	}

	public Integer getRolId() {
		return rolId;
	}

	public void setRolId(Integer rolId) {
		this.rolId = rolId;
	}

	public String getRolName() {
		return rolName;
	}

	public void setRolName(String rolName) {
		this.rolName = rolName;
	}

}
