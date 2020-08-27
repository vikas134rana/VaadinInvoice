package com.vikas.vaadindemo.ui.common;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
@CssImport("./style/header.css")
public class HeaderComponent extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	public Label logo;
	public Button invoicePriceMaintainance;
	public Button invoiceEntry;
	public Button invoicePrinting;
	public Button salesRegister;

	@PostConstruct
	public void init() {

		logo = new Label("Invoice Portal");

		invoicePriceMaintainance = new Button("Invoice Price Maintainance");
		invoiceEntry = new Button("Invoice Entry");
		invoicePrinting = new Button("Invoice Printing");
		salesRegister = new Button("Sales Register");

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.add(logo);
		horizontalLayout.add(invoicePriceMaintainance);
		horizontalLayout.add(invoiceEntry);
		horizontalLayout.add(invoicePrinting);
		horizontalLayout.add(salesRegister);
		horizontalLayout.setSizeFull();

		add(horizontalLayout);

		// styling
		addClassName("header");
		horizontalLayout.addClassName("header-menu");
		horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		logo.addClassName("header-logo");
		invoicePriceMaintainance.addClassName("header-menuitem");
		invoiceEntry.addClassName("header-menuitem");
		invoicePrinting.addClassName("header-menuitem");
		salesRegister.addClassName("header-menuitem");
		setSizeFull();

	}

}
