package com.vikas.vaadindemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InvoiceItem {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "invoice_id")
	private Invoice invoice;

	@Column
	private String unit;

	@Column
	private int quantity;

	@Column
	private double unitPrice;

	@Column
	private double netAmount;

	@Column
	private double cgstRate;

	@Column
	private double cgstAmount;

	@Column
	private double sgstRate;

	@Column
	private double sgstAmount;

	@Column
	private double igstRate;

	@Column
	private double igstAmount;

	@Column
	private double totalAmount;

	public InvoiceItem() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public double getCgstRate() {
		return cgstRate;
	}

	public void setCgstRate(double cgstRate) {
		this.cgstRate = cgstRate;
	}

	public double getCgstAmount() {
		return cgstAmount;
	}

	public void setCgstAmount(double cgstAmount) {
		this.cgstAmount = cgstAmount;
	}

	public double getSgstRate() {
		return sgstRate;
	}

	public void setSgstRate(double sgstRate) {
		this.sgstRate = sgstRate;
	}

	public double getSgstAmount() {
		return sgstAmount;
	}

	public void setSgstAmount(double sgstAmount) {
		this.sgstAmount = sgstAmount;
	}

	public double getIgstRate() {
		return igstRate;
	}

	public void setIgstRate(double igstRate) {
		this.igstRate = igstRate;
	}

	public double getIgstAmount() {
		return igstAmount;
	}

	public void setIgstAmount(double igstAmount) {
		this.igstAmount = igstAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "InvoiceItem [id=" + id + ", item=" + item.getId() + ", invoice=" + invoice.getId() + ", unit=" + unit + ", quantity=" + quantity + ", unitPrice=" + unitPrice
				+ ", netAmount=" + netAmount + ", cgstRate=" + cgstRate + ", cgstAmount=" + cgstAmount + ", sgstRate=" + sgstRate + ", sgstAmount=" + sgstAmount
				+ ", igstRate=" + igstRate + ", igstAmount=" + igstAmount + ", totalAmount=" + totalAmount + "]";
	}

}
