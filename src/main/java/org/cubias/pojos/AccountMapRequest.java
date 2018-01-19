package org.cubias.pojos;

import org.hibernate.validator.constraints.NotBlank;

public class AccountMapRequest {

	private String id;
	@NotBlank(message = "TI Director Field can not be empty")
	private String director;
	@NotBlank(message = "Program name can not be null")
	private String program;
	@NotBlank(message = "Start Date can not be null")
	private String startdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

}
