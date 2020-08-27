package com.vikas.vaadindemo.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vikas.vaadindemo.service.SellerService;
import com.vikas.vaadindemo.ui.common.HeaderComponent;
import com.vikas.vaadindemo.ui.invoiceentry.InvoiceEntryView;
import com.vikas.vaadindemo.ui.invoicepricemaintainance.InvoicePriceMaintainancePresenter;
import com.vikas.vaadindemo.ui.invoicepricemaintainance.InvoicePriceMaintainanceView;
import com.vikas.vaadindemo.ui.invoiceprinting.InvoicePrintingView;
import com.vikas.vaadindemo.ui.salesregister.SalesRegisterView;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	SellerService sellerService;

	@Autowired
	HeaderComponent header;

	@Autowired
	InvoicePriceMaintainanceView invoicePriceMaintainanceView;
	@Autowired
	InvoicePriceMaintainancePresenter invoicePriceMaintainancePresenter;

	@Autowired
	InvoiceEntryView invoiceEntryView;

	@Autowired
	InvoicePrintingView invoicePrintingView;

	@Autowired
	SalesRegisterView salesRegister;

	@PostConstruct
	public void init() {

		add(header);

		addViewOnClick(header.invoicePriceMaintainance, invoicePriceMaintainanceView);
		addViewOnClick(header.invoiceEntry, invoiceEntryView);
		addViewOnClick(header.invoicePrinting, invoicePrintingView);
		addViewOnClick(header.salesRegister, salesRegister);

	}

	// add view when button is clicked
	public void addViewOnClick(Button button, Component view) {

		button.addClickListener(click -> {
			removeAll();
			add(header);
			addView(view);
		});

	}

	// add view
	public void addView(Component view) {
		add(view);
	}
}
