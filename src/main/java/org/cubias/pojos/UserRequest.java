package org.cubias.pojos;

public class UserRequest {

	private int id;
	private int roleId;
	private String wdId;
	private String adUser;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getWdId() {
		return wdId;
	}

	public void setWdId(String wdId) {
		this.wdId = wdId;
	}

	public String getAdUser() {
		return adUser;
	}

	public void setAdUser(String adUser) {
		this.adUser = adUser;
	}

	@Override
	public String toString() {
		return "UserRequest [id=" + id + ", roleId=" + roleId + ", wdId=" + wdId + ", adUser=" + adUser + "]";
	}

}
