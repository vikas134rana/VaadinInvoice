package com.vikas.vaadindemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class InvoicePdf {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private byte[] pdfData;

	@OneToOne
	@JoinColumn(name = "invoice_id")
	private Invoice invoice;

	public InvoicePdf() {
	}

	public InvoicePdf(byte[] pdfData, Invoice invoice) {
		this.pdfData = pdfData;
		this.invoice = invoice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getPdfData() {
		return pdfData;
	}

	public void setPdfData(byte[] pdfData) {
		this.pdfData = pdfData;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@Override
	public String toString() {
		return "InvoicePdf [id=" + id + ", pdfDataLength=" + pdfData.length + ", invoice=" + invoice + "]";
	}

}
