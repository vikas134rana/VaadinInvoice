package com.vikas.vaadindemo.ui.invoiceentry;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Buyer;
import com.vikas.vaadindemo.service.BuyerService;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class BuyerDetailFormComponent extends FormLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	BuyerService buyerService;

	private ComboBox<Buyer> buyerEle;
	private TextField remarksEle;
	private TextField vehicleNumberEle;
	private TextField transporterNameEle;
	private DatePicker invoiceDateEle;

	private FormItem vehicleNumberFormItem;
	private FormItem transporterNameFormItem;
	private FormItem invoiceDateFormItem;

	public ComboBox<Buyer> getBuyerEle() {
		return buyerEle;
	}

	public void setBuyerEle(ComboBox<Buyer> buyerEle) {
		this.buyerEle = buyerEle;
	}

	public TextField getRemarksEle() {
		return remarksEle;
	}

	public void setRemarksEle(TextField remarksEle) {
		this.remarksEle = remarksEle;
	}

	public TextField getVehicleNumberEle() {
		return vehicleNumberEle;
	}

	public void setVehicleNumberEle(TextField vehicleNumberEle) {
		this.vehicleNumberEle = vehicleNumberEle;
	}

	public TextField getTransporterNameEle() {
		return transporterNameEle;
	}

	public void setTransporterNameEle(TextField transporterNameEle) {
		this.transporterNameEle = transporterNameEle;
	}

	public DatePicker getInvoiceDateEle() {
		return invoiceDateEle;
	}

	public void setInvoiceDateEle(DatePicker invoiceDateEle) {
		this.invoiceDateEle = invoiceDateEle;
	}

	public BuyerService getBuyerService() {
		return buyerService;
	}

	public FormItem getVehicleNumberFormItem() {
		return vehicleNumberFormItem;
	}

	public FormItem getTransporterNameFormItem() {
		return transporterNameFormItem;
	}

	public FormItem getInvoiceDateFormItem() {
		return invoiceDateFormItem;
	}

	@PostConstruct
	public void init() {

		add(new H5("Buyer Detail"));

		buyerEle = new ComboBox<>();
		buyerEle.setItems(buyerService.getAllBuyers());
		buyerEle.setItemLabelGenerator(b -> b.getAddress().getName());

		remarksEle = new TextField();

		vehicleNumberEle = new TextField();
		transporterNameEle = new TextField();
		invoiceDateEle = new DatePicker(LocalDate.now());

		addFormItem(buyerEle, "Buyer");
		addFormItem(remarksEle, "Remarks");

		vehicleNumberFormItem = addFormItem(vehicleNumberEle, "Vehicle Number");
		vehicleNumberFormItem.setVisible(false);

		transporterNameFormItem = addFormItem(transporterNameEle, "Transporter Name");
		transporterNameFormItem.setVisible(false);

		invoiceDateFormItem = addFormItem(invoiceDateEle, "Invoice Date");
		invoiceDateFormItem.setVisible(false);

		VaadinUtils.setFormLabelWidth(this, "30%");
//		getStyle().set("border", "1px solid black");
		setHeightFull();
	}

}
