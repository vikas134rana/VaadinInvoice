package com.vikas.vaadindemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Port {

	@Id
	private int id;

	@Column
	private String name;

	@Column
	private String code;

	public Port() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Port [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

}
