package com.vikas.vaadindemo.ui.invoiceentry;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class BuyerAddressComponent extends FormLayout {

	private static final long serialVersionUID = 1L;

	private Label buyerNameEle;
	private Label buyerAddressEle;
	private Label buyerPincodeEle;
	private Label buyerStateEle;
	private Label buyerCountryEle;
	private Label buyerGstinEle;

	public Label getBuyerNameEle() {
		return buyerNameEle;
	}

	public void setBuyerNameEle(Label buyerNameEle) {
		this.buyerNameEle = buyerNameEle;
	}

	public Label getBuyerAddressEle() {
		return buyerAddressEle;
	}

	public void setBuyerAddressEle(Label buyerAddressEle) {
		this.buyerAddressEle = buyerAddressEle;
	}

	public Label getBuyerPincodeEle() {
		return buyerPincodeEle;
	}

	public void setBuyerPincodeEle(Label buyerPincodeEle) {
		this.buyerPincodeEle = buyerPincodeEle;
	}

	public Label getBuyerStateEle() {
		return buyerStateEle;
	}

	public void setBuyerStateEle(Label buyerStateEle) {
		this.buyerStateEle = buyerStateEle;
	}

	public Label getBuyerCountryEle() {
		return buyerCountryEle;
	}

	public void setBuyerCountryEle(Label buyerCountryEle) {
		this.buyerCountryEle = buyerCountryEle;
	}

	public Label getBuyerGstinEle() {
		return buyerGstinEle;
	}

	public void setBuyerGstinEle(Label buyerGstinEle) {
		this.buyerGstinEle = buyerGstinEle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@PostConstruct
	public void init() {

		add(new H5("Buyer Address"));

		buyerNameEle = new Label();
		buyerAddressEle = new Label();
		buyerPincodeEle = new Label();
		buyerStateEle = new Label();
		buyerCountryEle = new Label();
		buyerGstinEle = new Label();

		addFormItem(buyerNameEle, "Name");
		addFormItem(buyerAddressEle, "Address");
		addFormItem(buyerPincodeEle, "Pincode");
		addFormItem(buyerStateEle, "State");
		addFormItem(buyerCountryEle, "Country");
		addFormItem(buyerGstinEle, "Gstin");

		VaadinUtils.setFormLabelWidth(this, "10%");
		setHeightFull();
		setResponsiveSteps(new ResponsiveStep("0px", 1, LabelsPosition.ASIDE));
//		getStyle().set("border", "1px solid black");
		
	}

}
