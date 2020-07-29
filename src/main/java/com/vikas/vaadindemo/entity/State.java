package com.vikas.vaadindemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class State {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String stateCode;

	@Column(nullable = false, unique = true)
	private String stateNumber;

	public State() {
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

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateNumber() {
		return stateNumber;
	}

	public void setStateNumber(String stateNumber) {
		this.stateNumber = stateNumber;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", stateCode=" + stateCode + ", stateNumber=" + stateNumber + "]";
	}

}
