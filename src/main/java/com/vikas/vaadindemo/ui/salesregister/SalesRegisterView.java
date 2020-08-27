package com.vikas.vaadindemo.ui.salesregister;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Buyer;
import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.entity.Seller;
import com.vikas.vaadindemo.features.salesregister.SalesRegister;
import com.vikas.vaadindemo.features.salesregister.SalesRegisterFactory;
import com.vikas.vaadindemo.features.salesregister.SalesRegisterFormat;
import com.vikas.vaadindemo.service.BuyerService;
import com.vikas.vaadindemo.service.InvoiceService;
import com.vikas.vaadindemo.service.SellerService;
import com.vikas.vaadindemo.util.InvoiceUtils;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class SalesRegisterView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	SellerService sellerService;

	@Autowired
	BuyerService buyerService;

	@Autowired
	InvoiceService invoiceService;

	private ComboBox<Seller> sellerEle;
	private ComboBox<Buyer> buyerEle;
	private TextField invoiceNumberEle;
	private ComboBox<String> invoiceTypeEle;
	private DatePicker startDateEle;
	private DatePicker endDateEle;
	private Button excelEle;
	private Button pdfEle;
	private Anchor hiddenDownloadButton;
	private FormLayout formLayout;

	private static final String downloadId = "downloadFileId";

	@PostConstruct
	public void init() {
		sellerEle = new ComboBox<>();
		sellerEle.setItems(sellerService.getAllSellers());
		sellerEle.setItemLabelGenerator(s -> s.getAddress().getName());

		buyerEle = new ComboBox<>();
		buyerEle.setItems(buyerService.getAllBuyers());
		buyerEle.setItemLabelGenerator(b -> b.getAddress().getName());

		invoiceNumberEle = new TextField();

		invoiceTypeEle = new ComboBox<String>();
		Collection<String> invoiceTypes = InvoiceUtils.invoiceTypeMap().values();
		invoiceTypeEle.setItems(invoiceTypes);

		startDateEle = new DatePicker(LocalDate.now());
		endDateEle = new DatePicker(LocalDate.now());

		excelEle = new Button("Download Excel");
		excelEle.addClickListener(click -> downloadSalesRegister(SalesRegisterFormat.EXCEL));

		pdfEle = new Button("Download PDF");
		pdfEle.addClickListener(click -> downloadSalesRegister(SalesRegisterFormat.PDF));

		VaadinUtils.setWidthFull(sellerEle, buyerEle, invoiceNumberEle, invoiceTypeEle, startDateEle, endDateEle);

		hiddenDownloadButton = new Anchor();
		hiddenDownloadButton.setId(downloadId);
		hiddenDownloadButton.getElement().getStyle().set("display", "none");
		hiddenDownloadButton.getElement().setAttribute("download", true);

		formLayout = new FormLayout();
		formLayout.addFormItem(sellerEle, "Seller");
		formLayout.addFormItem(buyerEle, "Buyer");
		formLayout.addFormItem(invoiceNumberEle, "Invoice Number");
		formLayout.addFormItem(invoiceTypeEle, "Invoice Type");
		formLayout.addFormItem(startDateEle, "Start Date");
		formLayout.addFormItem(endDateEle, "End Date");
		formLayout.add(hiddenDownloadButton);

		formLayout.setWidthFull();
		formLayout.setResponsiveSteps(new ResponsiveStep("0px", 1, LabelsPosition.ASIDE));

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.add(excelEle);
		buttonsLayout.add(pdfEle);
		buttonsLayout.getStyle().set("margin-top", "20px");
		buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		formLayout.add(buttonsLayout);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth("450px");
		horizontalLayout.add(formLayout);
		horizontalLayout.getStyle().set("border", "1px solid black");
		horizontalLayout.getStyle().set("border-radius", "10px");
		horizontalLayout.getStyle().set("padding", "20px");

		add(horizontalLayout);
		setAlignItems(Alignment.CENTER);
	}

	private List<Invoice> getInvoicesUsingForm() {
		Integer sellerId = sellerEle.getValue() == null ? 0 : sellerEle.getValue().getId();
		System.out.println("seller: " + sellerId);

		Integer buyerId = buyerEle.getValue() == null ? 0 : buyerEle.getValue().getId();
		System.out.println("buyer: " + buyerId);

		String invoiceNumber = invoiceNumberEle.getValue();
		System.out.println("invoiceNumber: " + invoiceNumber);

		String invoiceTypeStr = invoiceTypeEle.getValue();
		int invoiceType = InvoiceUtils.invoiceTypeInt(invoiceTypeStr);

		LocalDate startDate = startDateEle.getValue();
		System.out.println("startDate: " + startDate);

		LocalDate endDate = endDateEle.getValue();
		System.out.println("endDate: " + endDate);

		List<Invoice> invoices = invoiceService.searchInvoice(invoiceNumber, sellerId, buyerId, invoiceType, startDate, endDate);
		invoices.forEach(i -> System.out.println(i));
		return invoices;
	}

	@SuppressWarnings("deprecation")
	private void downloadSalesRegister(SalesRegisterFormat format) {
		List<Invoice> invoices = getInvoicesUsingForm();
		SalesRegister salesRegister = SalesRegisterFactory.getSalesRegister(invoices, format);

		try {
			String fileExtension = (format == SalesRegisterFormat.EXCEL) ? ".xlsx" : ".pdf";
			String fileName = "SalesRegister " + LocalDateTime.now() + fileExtension;
			byte[] pdfData = salesRegister.generate();

			StreamResource streamResource = new StreamResource(fileName, () -> new ByteArrayInputStream(pdfData));
			hiddenDownloadButton.setHref(streamResource);

			Page page = UI.getCurrent().getPage();
			page.executeJavaScript("document.getElementById('" + downloadId + "').click();");

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}
	}

}
