package com.vikas.vaadindemo.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.vikas.vaadindemo.entity.enums.InvoiceStatus;
import com.vikas.vaadindemo.util.InvoiceUtils;

@Entity
public class Invoice {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, unique = true)
	private String invoiceNumber;

	@Column(nullable = false)
	private LocalDateTime creationDate;

	@Column
	private int type;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private Seller seller;

	@ManyToOne
	@JoinColumn(name = "buyer_id")
	private Buyer buyer;

	@Column
	private int statusCode;

	@Column
	private int totalQuantity;

	@Column
	private double totalNetAmount;

	@Column
	private double totalCgstAmount;

	@Column
	private double totalSgstAmount;

	@Column
	private double totalIgstAmount;

	@Column
	private double totalAmount;

	@Column
	private String arDocNumber;

	@Column
	private String apDocNumber;

	@Column
	private String vehicleNumber;

	@Column
	private String transporterName;

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<InvoiceItem> invoiceItems;

	@OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL)
	private InvoicePdf invoicePdf;

	public Invoice() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeValue() {
		return InvoiceUtils.invoiceTypeValue(this.type);
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public Buyer getBuyer() {
		return buyer;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	public InvoiceStatus getStatusCode() {
		return InvoiceStatus.getInvoioceStatus(statusCode);
	}

	public void setStatusCode(InvoiceStatus invoiceStatus) {
		this.statusCode = invoiceStatus.getStatusCode();
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public List<InvoiceItem> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public InvoicePdf getInvoicePdf() {
		return invoicePdf;
	}

	public void setInvoicePdf(InvoicePdf invoicePdf) {
		this.invoicePdf = invoicePdf;
	}

	public double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public double getTotalCgstAmount() {
		return totalCgstAmount;
	}

	public void setTotalCgstAmount(double totalCgstAmount) {
		this.totalCgstAmount = totalCgstAmount;
	}

	public double getTotalSgstAmount() {
		return totalSgstAmount;
	}

	public void setTotalSgstAmount(double totalSgstAmount) {
		this.totalSgstAmount = totalSgstAmount;
	}

	public double getTotalIgstAmount() {
		return totalIgstAmount;
	}

	public void setTotalIgstAmount(double totalIgstAmount) {
		this.totalIgstAmount = totalIgstAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getArDocNumber() {
		return arDocNumber;
	}

	public void setArDocNumber(String arDocNumber) {
		this.arDocNumber = arDocNumber;
	}

	public String getApDocNumber() {
		return apDocNumber;
	}

	public void setApDocNumber(String apDocNumber) {
		this.apDocNumber = apDocNumber;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getTransporterName() {
		return transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	@Override
	public String toString() {
		return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber + ", creationDate=" + creationDate + ", type=" + type + ", seller="
				+ (seller != null ? seller.getId() : null) + ", buyer=" + (buyer != null ? buyer.getId() : null) + ", statusCode=" + statusCode
				+ ", totalQuantity=" + totalQuantity + ", totalNetAmount=" + totalNetAmount + ", totalCgstAmount=" + totalCgstAmount + ", totalSgstAmount="
				+ totalSgstAmount + ", totalIgstAmount=" + totalIgstAmount + ", totalAmount=" + totalAmount + ", arDocNumber=" + arDocNumber + ", apDocNumber="
				+ apDocNumber + ", vehicleNumber=" + vehicleNumber + ", transporterName=" + transporterName + ", invoiceItemsCount="
				+ (invoiceItems != null ? invoiceItems.size() : 0) + ", invoicePdf=" + (invoicePdf != null ? invoicePdf.getId() : null) + "]";
	}

}
