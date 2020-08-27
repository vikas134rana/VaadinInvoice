package com.vikas.vaadindemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Export {

	@Id
	private int id;

	@Column
	private String currency;

	@Column
	private int exchangeRate;

	@OneToOne
	@JoinColumn(name = "port_id")
	private Port port;

	@Column
	private int igstType;

	@OneToOne
	@JoinColumn(name = "invoice_id")
	private Invoice invoice;

	public Export() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(int exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Port getPort() {
		return port;
	}

	public void setPort(Port port) {
		this.port = port;
	}

	public int getIgstType() {
		return igstType;
	}

	public void setIgstType(int igstType) {
		this.igstType = igstType;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@Override
	public String toString() {
		return "Export [id=" + id + ", currency=" + currency + ", exchangeRate=" + exchangeRate + ", port=" + (port != null ? port.getId() : null)
				+ ", igstType=" + igstType + ", invoice=" + (invoice != null ? invoice.getId() : null) + "]";
	}

}
