package com.vikas.vaadindemo.ui.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
public class InvoiceDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	public InvoiceDialog(String title, String msg) {
		init(title, msg);
	}

	void init(String title, String msg) {
		VerticalLayout verticalLayout = new VerticalLayout();

		Label titleEle = new Label(title);
		Label msgEle = new Label(msg);
		Button closeButton = new Button("Close");

		msgEle.setMinWidth("300px");
		msgEle.setMaxWidth("500px");
		msgEle.setMinHeight("60px");
		titleEle.getStyle().set("font-weight", "bold"); // bold title
		verticalLayout.add(titleEle);
		verticalLayout.add(msgEle);
		verticalLayout.add(closeButton);
		verticalLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, closeButton);

		add(verticalLayout);
		open();

		closeButton.addClickListener(listener -> this.close());
	}

}
