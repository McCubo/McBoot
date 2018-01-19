package org.cubias.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class FowAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer audId;

	private Date audEventDate;
	private String audTableName;
	private String audFieldName;
	private String audOldValue;
	private String audNewValue;
	@OneToOne
	@JoinColumn(name = "aud_use_id")
	private FowUser audUse;
	
	private String audRecordId;
	
	protected FowAudit() {
	}

	public FowAudit(Date audEventDate, String audTableName, String audFieldName, String audOldValue, String audNewValue,
			FowUser audUse, String audRecordId) {
		this.audEventDate = audEventDate;
		this.audTableName = audTableName;
		this.audFieldName = audFieldName;
		this.audOldValue = audOldValue;
		this.audNewValue = audNewValue;
		this.audUse = audUse;
		this.audRecordId = audRecordId;
	}

	@Override
	public String toString() {
		return "FowAudit [audId=" + audId + ", audEventDate=" + audEventDate + ", audTableName=" + audTableName
				+ ", audFieldName=" + audFieldName + ", audOldValue=" + audOldValue + ", audNewValue=" + audNewValue
				+ ", audUse=" + audUse.getUseAdUser() + "]";
	}

	public Integer getAudId() {
		return audId;
	}

	public void setAudId(Integer audId) {
		this.audId = audId;
	}

	public Date getAudEventDate() {
		return audEventDate;
	}

	public void setAudEventDate(Date audEventDate) {
		this.audEventDate = audEventDate;
	}

	public String getAudTableName() {
		return audTableName;
	}

	public void setAudTableName(String audTableName) {
		this.audTableName = audTableName;
	}

	public String getAudFieldName() {
		return audFieldName;
	}

	public void setAudFieldName(String audFieldName) {
		this.audFieldName = audFieldName;
	}

	public String getAudOldValue() {
		return audOldValue;
	}

	public void setAudOldValue(String audOldValue) {
		this.audOldValue = audOldValue;
	}

	public String getAudNewValue() {
		return audNewValue;
	}

	public void setAudNewValue(String audNewValue) {
		this.audNewValue = audNewValue;
	}

	public FowUser getAudUse() {
		return audUse;
	}

	public void setAudUse(FowUser audUse) {
		this.audUse = audUse;
	}

	public String getAudRecordId() {
		return audRecordId;
	}

	public void setAudRecordId(String audRecordId) {
		this.audRecordId = audRecordId;
	}

}
