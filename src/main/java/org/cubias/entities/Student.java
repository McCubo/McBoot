package org.cubias.entities;

import java.util.Date;

public class Student {
	private long id;
	private String name;
	private String subject;

	public Student(String name, String subject) {
		super();
		this.id = new Date().getTime();
		this.name = name;
		this.subject = subject;
	}

	public Student() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "Student {id=" + id + ", name=" + name + ", subject=" + subject + "}";
	}

}
