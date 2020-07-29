package com.vikas.vaadindemo.ui;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class Header extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	public Button invoicePriceMaintainance;
	public Button invoiceEntry;
	public Button invoicePrinting;
	public Button salesRegister;

	@PostConstruct
	public void init() {

		invoicePriceMaintainance = new Button("Invoice Price Maintainance");
		invoiceEntry = new Button("Invoice Entry");
		invoicePrinting = new Button("Invoice Printing");
		salesRegister = new Button("Sales Register");

		add(new H4("Invoice Portal"));
		add(invoicePriceMaintainance);
		add(invoiceEntry);
		add(invoicePrinting);
		add(salesRegister);
	}

}
