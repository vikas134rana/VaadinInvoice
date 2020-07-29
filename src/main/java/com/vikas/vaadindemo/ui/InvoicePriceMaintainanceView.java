package com.vikas.vaadindemo.ui;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class InvoicePriceMaintainanceView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
		add(new Text("InvoicePriceMaintainanceView not implemented yet"));
	}
}
