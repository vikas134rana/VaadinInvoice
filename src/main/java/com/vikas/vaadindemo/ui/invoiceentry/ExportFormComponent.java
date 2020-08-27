package com.vikas.vaadindemo.ui.invoiceentry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Port;
import com.vikas.vaadindemo.service.PortService;
import com.vikas.vaadindemo.util.InvoiceUtils;

@UIScope
@SpringComponent
public class ExportFormComponent extends FormLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	PortService portService;

	private Select<String> currencyEle;
	private NumberField exchangeRateEle;
	private ComboBox<Port> portCodeEle;
	private RadioButtonGroup<String> igstTypeEle;

	public Select<String> getCurrencyEle() {
		return currencyEle;
	}

	public NumberField getExchangeRateEle() {
		return exchangeRateEle;
	}

	public ComboBox<Port> getPortCodeEle() {
		return portCodeEle;
	}

	public RadioButtonGroup<String> getIgstTypeEle() {
		return igstTypeEle;
	}

	@PostConstruct
	public void init() {

		add(new H5("Export"));

		currencyEle = new Select<>();
		currencyEle.setEmptySelectionAllowed(true);
		currencyEle.setEmptySelectionCaption("Select Currency");
		currencyEle.setItems(InvoiceUtils.getCurrencies());

		exchangeRateEle = new NumberField();

		portCodeEle = new ComboBox<>();
		portCodeEle.setItems(portService.getAllPorts());
		portCodeEle.setItemLabelGenerator(p -> p.getCode());

		igstTypeEle = new RadioButtonGroup<>();
		igstTypeEle.setItems(InvoiceUtils.getIgstTypeMap().values());

		addFormItem(currencyEle, "Currency");
		addFormItem(exchangeRateEle, "Exchange Rate");
		addFormItem(portCodeEle, "Port Code");
		addFormItem(igstTypeEle, "Igst Type");

//		getStyle().set("border", "1px solid black");
		setResponsiveSteps(new ResponsiveStep("0px", 1, LabelsPosition.ASIDE));
		setHeightFull();
	}

}
