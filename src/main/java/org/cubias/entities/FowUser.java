package org.cubias.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class FowUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer useId;

	@OneToOne
	@JoinColumn(name = "use_rol_id")
	private FowRole useRole;
	private String useWdId;
	private String useAdUser;
	private Date useInsertedDate;

	protected FowUser() {
	}

	public FowUser(FowRole useRole, String useWdId, String useAdUser, Date useInsertedDate) {
		this.useRole = useRole;
		this.useWdId = useWdId;
		this.useAdUser = useAdUser;
		this.useInsertedDate = useInsertedDate;
	}

	@Override
	public String toString() {
		return "FowUser [useId=" + useId + ", useRole=" + useRole.getRolName() + ", useWdId=" + useWdId + ", useAdUser="
				+ useAdUser + ", useInsertedDate=" + useInsertedDate + "]";
	}

	public Integer getUseId() {
		return useId;
	}

	public void setUseId(Integer useId) {
		this.useId = useId;
	}

	public FowRole getUseRole() {
		return useRole;
	}

	public void setUseRole(FowRole useRole) {
		this.useRole = useRole;
	}

	public String getUseWdId() {
		return useWdId;
	}

	public void setUseWdId(String useWdId) {
		this.useWdId = useWdId;
	}

	public String getUseAdUser() {
		return useAdUser;
	}

	public void setUseAdUser(String useAdUser) {
		this.useAdUser = useAdUser;
	}

	public Date getUseInsertedDate() {
		return useInsertedDate;
	}

	public void setUseInsertedDate(Date useInsertedDate) {
		this.useInsertedDate = useInsertedDate;
	}

}
