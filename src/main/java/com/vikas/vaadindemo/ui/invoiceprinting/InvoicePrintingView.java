package com.vikas.vaadindemo.ui.invoiceprinting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
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
import com.vikas.vaadindemo.entity.enums.InvoiceStatus;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceBuilder;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceBuilderFactory;
import com.vikas.vaadindemo.service.BuyerService;
import com.vikas.vaadindemo.service.InvoiceService;
import com.vikas.vaadindemo.service.SellerService;
import com.vikas.vaadindemo.util.InvoiceUtils;

@UIScope
@SpringComponent
public class InvoicePrintingView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	ModifyInvoiceDialog modifyDialog;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	SellerService sellerService;

	@Autowired
	BuyerService buyerService;

	private ComboBox<Seller> sellerEle;
	private ComboBox<Buyer> buyerEle;
	private TextField invoiceNumberEle;
	private ComboBox<String> invoiceTypeEle;
	private DatePicker startDateEle;
	private DatePicker endDateEle;
	private Anchor hiddenDownloadEle;
	private Button searchEle;
	private FormLayout formLayout;
	private Grid<Invoice> grid;

	private List<Invoice> invoices = new ArrayList<>();

	private static final String downloadId = "downloadId";

	public Grid<Invoice> getGrid() {
		return grid;
	}

	@PostConstruct
	public void init() {
		addSearchInvoiceForm();
		addInvoicesGrid();
	}

	private void addSearchInvoiceForm() {

		sellerEle = new ComboBox<>();
		sellerEle.setItems(sellerService.getAllSellers());
		sellerEle.setItemLabelGenerator(s -> s.getAddress().getName());
		sellerEle.setLabel("Seller");

		buyerEle = new ComboBox<>();
		buyerEle.setItems(buyerService.getAllBuyers());
		buyerEle.setItemLabelGenerator(b -> b.getAddress().getName());
		buyerEle.setLabel("Buyer");

		invoiceNumberEle = new TextField();
		invoiceNumberEle.setLabel("Invoice Number");

		invoiceTypeEle = new ComboBox<String>();
		Collection<String> invoiceTypes = InvoiceUtils.invoiceTypeMap().values();
		invoiceTypeEle.setItems(invoiceTypes);
		invoiceTypeEle.setLabel("Invoice Type");

		startDateEle = new DatePicker(LocalDate.now());
		startDateEle.setLabel("Start Date");

		endDateEle = new DatePicker(LocalDate.now());
		endDateEle.setLabel("End Date");

		searchEle = new Button("Search");
		searchEle.addClickListener(click -> {
			invoices = getInvoicesUsingForm();
			getGrid().setItems(invoices);
		});

		formLayout = new FormLayout();
		formLayout.add(sellerEle, buyerEle, invoiceNumberEle, invoiceTypeEle, startDateEle, endDateEle, searchEle);
		// @formatter:off
		formLayout.setResponsiveSteps(
				new ResponsiveStep("150px", 1), 
				new ResponsiveStep("150px", 2), 
				new ResponsiveStep("150px", 3),
				new ResponsiveStep("150px", 4), 
				new ResponsiveStep("100px", 5), 
				new ResponsiveStep("100px", 6), 
				new ResponsiveStep("100px", 7));
		// @formatter:on
		add(formLayout);
	}

	private void addInvoicesGrid() {
		grid = new Grid<>(Invoice.class);
		getGrid().setItems(invoices);
		getGrid().setColumns(); // if not used grid Column will contain all data present in Invoice class
		getGrid().addColumn(item -> "").setKey("rowIndex").setHeader("S.No").setAutoWidth(true);// S.No
		// S.No Logic
		getGrid().addAttachListener(event -> {
			Column<Invoice> serialNoColumn = getGrid().getColumnByKey("rowIndex");
			serialNoColumn.getElement().executeJs("this.renderer = function(root, column, rowData) {root.textContent = rowData.index + 1}");
		});
		getGrid().addColumn(i -> i.getInvoiceNumber()).setHeader("Invoice Number").setAutoWidth(true); // InvoiceNumber
		getGrid().addColumn(i -> i.getStatusCode()).setHeader("Status").setAutoWidth(true); // Status
		getGrid().addColumn(i -> i.getCreationDate()).setHeader("Creation Date").setAutoWidth(true); // CreationDate
		getGrid().addColumn(i -> i.getSeller().getAddress().getName()).setHeader("Seller"); // Seller
		getGrid().addColumn(i -> i.getBuyer().getAddress().getName()).setHeader("Buyer"); // Buyer
		getGrid().addColumn(i -> i.getArDocNumber()).setHeader("AR Doc Number").setAutoWidth(true); // AR Doc Number
		getGrid().addColumn(i -> i.getApDocNumber()).setHeader("AP Doc Number").setAutoWidth(true); // AP Doc Number
		getGrid().addColumn(i -> i.getTotalAmount()).setHeader("Total Amount").setAutoWidth(true); // Total Amount
		getGrid().addComponentColumn(invoice -> {
			return actionButtons(invoice);
		}).setAutoWidth(true).setHeader("Actions"); // Action

		getGrid().setSelectionMode(SelectionMode.SINGLE);
		getGrid().addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		getGrid().setHeight("700px");

		hiddenDownloadEle = new Anchor();
		hiddenDownloadEle.setId(downloadId);

		add(getGrid());
		add(hiddenDownloadEle);
		add(modifyDialog);
	}

	private Component actionButtons(Invoice invoice) {
		HorizontalLayout layout = new HorizontalLayout();

		Button downloadEle = new Button("Download");
		Button modifyEle = new Button("Modify");
		Button cancelEle = new Button("Cancel");

		downloadEle.addClickListener(click -> {
			byte[] pdfData = invoice.getInvoicePdf().getPdfData();
			downloadInvoice(pdfData, invoice);
		});

		modifyEle.addClickListener(click -> {
			modifyInvoice(invoice);
		});

		cancelEle.addClickListener(click -> {
			try {
				cancelInvoice(invoice);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		layout.add(downloadEle);
		layout.add(modifyEle);
		layout.add(cancelEle);

		return layout;
	}

	@SuppressWarnings("deprecation")
	private void downloadInvoice(byte[] pdfData, Invoice invoice) {

		String fileName = "Invoice " + invoice.getInvoiceNumber() + ".pdf";
		StreamResource streamResource = new StreamResource(fileName, () -> new ByteArrayInputStream(pdfData));
		hiddenDownloadEle.setHref(streamResource);

		Page page = UI.getCurrent().getPage();
		page.executeJavaScript("document.getElementById('" + downloadId + "').click();");
	}

	private void cancelInvoice(Invoice invoice) throws IOException {
		try {
			InvoiceBuilder invoiceBuilder = InvoiceBuilderFactory.getInvoiceBuilder(invoice);
			byte[] pdfData = invoiceBuilder.cancelInvoicePdf();
			invoice.getInvoicePdf().setPdfData(pdfData);
			invoice.setStatusCode(InvoiceStatus.CANCELED);

			invoiceService.save(invoice); // update pdfdata in InvoicePdf table
			getGrid().getDataProvider().refreshAll();
			downloadInvoice(pdfData, invoice);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private void modifyInvoice(Invoice invoice) {
		modifyDialog.updateForm(invoice);
		modifyDialog.open();
	}

	private List<Invoice> getInvoicesUsingForm() {

		Integer sellerId = sellerEle.getValue() == null ? 0 : sellerEle.getValue().getId();
		Integer buyerId = buyerEle.getValue() == null ? 0 : buyerEle.getValue().getId();
		String invoiceNumber = invoiceNumberEle.getValue();
		String invoiceTypeStr = invoiceTypeEle.getValue();
		int invoiceType = InvoiceUtils.invoiceTypeInt(invoiceTypeStr);
		LocalDate startDate = startDateEle.getValue();
		LocalDate endDate = endDateEle.getValue();

		List<Invoice> invoices = invoiceService.searchInvoice(invoiceNumber, sellerId, buyerId, invoiceType, startDate, endDate);
		invoices.forEach(i -> System.out.println(i));
		return invoices;
	}

}
