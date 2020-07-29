package com.vikas.vaadindemo.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vikas.vaadindemo.service.SellerService;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	SellerService sellerService;

	@Autowired
	Header header;

	@Autowired
	InvoicePriceMaintainanceView invoicePriceMaintainanceView;
	
	@Autowired
	InvoiceEntryView invoiceEntryView;
	
	@Autowired
	InvoicePrintingView invoicePrintingView;

	@Autowired
	SalesRegisterView salesRegister;

	@PostConstruct
	public void init() {

		add(header);

		addView(header.invoicePriceMaintainance, invoicePriceMaintainanceView);
		addView(header.invoiceEntry, invoiceEntryView);
		addView(header.invoicePrinting, invoicePrintingView);
		addView(header.salesRegister, salesRegister);

	}
	
	// add view when button is clicked
	public void addView(Button button, Component view) {
		
		button.addClickListener(click -> {
			removeAll();
			add(header);
			add(view);
		});
		
	}
}
