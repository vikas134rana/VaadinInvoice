package com.vikas.vaadindemo.ui.invoiceprinting;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.service.InvoiceService;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class ModifyInvoiceDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	@Autowired
	InvoiceService invoiceService;

	private H4 heading;
	private TextField vehicleNumberEle;
	private TextField transporterNameEle;
	private TextField arDocNumberEle;
	private TextField apDocNumberEle;
	private Button saveButton;
	private Notification notification;

	@PostConstruct
	public void init() {
		setWidth("500px");
		setHeight("380px");

		notification = new Notification("", 5000);

		heading = new H4("Modify Invoice: ");

		vehicleNumberEle = new TextField();
		vehicleNumberEle.setWidthFull();

		transporterNameEle = new TextField();
		transporterNameEle.setWidthFull();

		arDocNumberEle = new TextField();
		arDocNumberEle.setWidthFull();

		apDocNumberEle = new TextField();
		apDocNumberEle.setWidthFull();

		FormLayout modifyFormLayout = new FormLayout();
		modifyFormLayout.addFormItem(vehicleNumberEle, "Vehicle Number");
		modifyFormLayout.addFormItem(transporterNameEle, "Transporter Name");
		modifyFormLayout.addFormItem(arDocNumberEle, "AR Doc Number");
		modifyFormLayout.addFormItem(apDocNumberEle, "AP Doc Number");
		modifyFormLayout.setResponsiveSteps(new ResponsiveStep("0px", 1, LabelsPosition.ASIDE));
		VaadinUtils.setFormLabelWidth(modifyFormLayout, "150px");
		modifyFormLayout.getStyle().set("margin", "20px");

		Button closeButton = new Button("Close");
		closeButton.getStyle().set("color", "red");
		saveButton = new Button("Save");

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.add(closeButton);
		buttonsLayout.add(saveButton);
		buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Right Align Buttons
		buttonsLayout.getStyle().set("margin-top", "20px");
		modifyFormLayout.add(buttonsLayout);

		add(heading);
		add(modifyFormLayout);

		closeButton.addClickListener(click -> {
			close();
		});

	}

	public void updateForm(Invoice invoice) {

		heading.setText("Modify Invoice: " + invoice.getInvoiceNumber());

		String vehicleNumber = invoice.getVehicleNumber() != null ? invoice.getVehicleNumber() : "";
		vehicleNumberEle.setValue(vehicleNumber);

		String transporterName = invoice.getTransporterName() != null ? invoice.getTransporterName() : "";
		transporterNameEle.setValue(transporterName);

		String arDocNumber = invoice.getArDocNumber() != null ? invoice.getArDocNumber() : "";
		arDocNumberEle.setValue(arDocNumber);

		String apDocNumber = invoice.getApDocNumber() != null ? invoice.getApDocNumber() : "";
		apDocNumberEle.setValue(apDocNumber);

		saveButton.addClickListener(click -> {

			invoice.setVehicleNumber(vehicleNumberEle.getValue());
			invoice.setTransporterName(transporterNameEle.getValue());
			invoice.setArDocNumber(arDocNumberEle.getValue());
			invoice.setApDocNumber(apDocNumberEle.getValue());

			invoiceService.save(invoice); // Update Invoice
			close();

			notification.setText("Invoice(" + invoice.getInvoiceNumber() + ") modified successfully");
			notification.open();
			InvoicePrintingView invoicePrintingView = (InvoicePrintingView) this.getParent().get();

			invoicePrintingView.add(notification);
			invoicePrintingView.getGrid().getDataProvider().refreshAll();

		});
	}

}
