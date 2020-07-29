package com.vikas.vaadindemo.ui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Buyer;
import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.entity.InvoiceItem;
import com.vikas.vaadindemo.entity.InvoicePdf;
import com.vikas.vaadindemo.entity.ItemPrice;
import com.vikas.vaadindemo.entity.Seller;
import com.vikas.vaadindemo.entity.enums.InvoiceStatus;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceBuilder;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceBuilderFactory;
import com.vikas.vaadindemo.service.BuyerService;
import com.vikas.vaadindemo.service.InvoiceSeriesService;
import com.vikas.vaadindemo.service.ItemPriceService;
import com.vikas.vaadindemo.service.SellerService;
import com.vikas.vaadindemo.service.TaxRateService;
import com.vikas.vaadindemo.util.InvoiceUtils;
import com.vikas.vaadindemo.util.Util;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class InvoiceEntryView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	SellerService sellerService;

	@Autowired
	BuyerService buyerService;

	@Autowired
	ItemPriceService itemPriceService;

	@Autowired
	TaxRateService taxRateService;

	@Autowired
	InvoiceSeriesService invoiceSeriesService;

	private FormLayout formLayout;
	private ComboBox<Seller> sellerEle;
	private RadioButtonGroup<String> invoiceTypeEle;
	private HorizontalLayout buyerAndExportLayout;
	private VerticalLayout buyerDetailLayout;
	private FormLayout buyerDetailFormLayout;
	private ComboBox<Buyer> buyerEle;
	private TextField remarksEle;
	private TextField vehicleNumberEle;
	private TextField transporterNameEle;
	private VerticalLayout buyerAddressLayout;
	private FormLayout buyerAddressFormLayout;
	private Label buyerNameEle;
	private Label buyerAddressEle;
	private Label buyerPincodeEle;
	private Label buyerStateEle;
	private Label buyerCountryEle;
	private Label buyerGstinEle;
	private VerticalLayout exportLayout;
	private FormLayout exportFormLayout;
	private Select<String> currencyEle;
	private NumberField exchangeRateEle;
	private Select<String> portCodeEle;
	private RadioButtonGroup<String> igstTypeEle;
	private Grid<InvoiceItem> itemGrid;
	private Button saveEle;

	private NumberField quantityEle;

	private List<InvoiceItem> items = new ArrayList<InvoiceItem>();

	@PostConstruct
	public void init() {
//		add(new Text("InvoiceEntryView not implemented yet"));

		sellerEle = new ComboBox<>();
		sellerEle.setItems(sellerService.getAllSellers());
		sellerEle.setItemLabelGenerator(s -> s.getAddress().getName());
		sellerEle.addValueChangeListener(sellerEle -> setInvoiceItems());

		invoiceTypeEle = new RadioButtonGroup<String>();
		Collection<String> invoiceTypes = InvoiceUtils.invoiceTypeMap().values();
		invoiceTypeEle.setItems(invoiceTypes);
		invoiceTypeEle.addValueChangeListener(listener -> {
			String invoiceTypeStr = invoiceTypeEle.getValue();
			int invoiceType = InvoiceUtils.invoiceTypeInt(invoiceTypeStr);
			if (invoiceType == 4) {
				exportLayout.add(exportFormLayout);
			} else {
				exportLayout.removeAll();
			}
		});

		buyerEle = new ComboBox<>();
		buyerEle.setItems(buyerService.getAllBuyers());
		buyerEle.setItemLabelGenerator(b -> b.getAddress().getName());
		buyerEle.addValueChangeListener(listener -> {
			Buyer buyer = buyerEle.getValue();

			if (buyer != null) {
				buyerNameEle.setText(buyer.getAddress().getName());
				buyerAddressEle.setText(buyer.getAddress().getAddressDetail());
				buyerPincodeEle.setText(buyer.getAddress().getPincode());
				buyerStateEle.setText(buyer.getAddress().getState().getName());
				buyerCountryEle.setText(buyer.getAddress().getCountry().getName());
				buyerGstinEle.setText(buyer.getAddress().getGstin());
			} else {
				buyerNameEle.setText(null);
				buyerAddressEle.setText(null);
				buyerPincodeEle.setText(null);
				buyerStateEle.setText(null);
				buyerCountryEle.setText(null);
				buyerGstinEle.setText(null);
			}
		});

		remarksEle = new TextField();
		vehicleNumberEle = new TextField();
		transporterNameEle = new TextField();

		buyerDetailLayout = new VerticalLayout();
		buyerDetailLayout.setWidth("80%");
		buyerDetailFormLayout = new FormLayout();
		buyerDetailFormLayout.getStyle().set("border", "1px solid black");
		buyerDetailFormLayout.add(new H5("Buyer Detail"));
		buyerDetailFormLayout.addFormItem(buyerEle, "Buyer");
		buyerDetailFormLayout.addFormItem(remarksEle, "Remarks");
		buyerDetailFormLayout.addFormItem(vehicleNumberEle, "Vehicle Number");
		buyerDetailFormLayout.addFormItem(transporterNameEle, "Transporter Name");
		VaadinUtils.setFormLabelWidth(buyerDetailFormLayout, "150px");
		buyerDetailLayout.add(buyerDetailFormLayout);

		buyerNameEle = new Label();
		buyerAddressEle = new Label();
		buyerPincodeEle = new Label();
		buyerStateEle = new Label();
		buyerCountryEle = new Label();
		buyerGstinEle = new Label();

		buyerAddressLayout = new VerticalLayout();
		buyerAddressLayout.setHeightFull();
		buyerAddressFormLayout = new FormLayout();
		buyerAddressFormLayout.getStyle().set("border", "1px solid black");
		buyerAddressFormLayout.add(new H5("Buyer Address"));
		buyerAddressFormLayout.addFormItem(buyerNameEle, "Name");
		buyerAddressFormLayout.addFormItem(buyerAddressEle, "Address");
		buyerAddressFormLayout.addFormItem(buyerPincodeEle, "Pincode");
		buyerAddressFormLayout.addFormItem(buyerStateEle, "State");
		buyerAddressFormLayout.addFormItem(buyerCountryEle, "Country");
		buyerAddressFormLayout.addFormItem(buyerGstinEle, "Gstin");
		buyerAddressFormLayout.setHeightFull();
		buyerAddressLayout.add(buyerAddressFormLayout);

		currencyEle = new Select<>();
		currencyEle.setItems(InvoiceUtils.getCurrencies());
		exchangeRateEle = new NumberField();
		portCodeEle = new Select<>();
		igstTypeEle = new RadioButtonGroup<>();
		igstTypeEle.setItems(InvoiceUtils.getIgstTypes());

		exportLayout = new VerticalLayout();
		exportLayout.setHeightFull();
		exportFormLayout = new FormLayout();
		exportFormLayout.getStyle().set("border", "1px solid black");
		exportFormLayout.add(new H5("Export"));
		exportFormLayout.addFormItem(currencyEle, "Currency");
		exportFormLayout.addFormItem(exchangeRateEle, "Exchange Rate");
		exportFormLayout.addFormItem(portCodeEle, "Port Code");
		exportFormLayout.addFormItem(igstTypeEle, "Igst Type");

		buyerAndExportLayout = new HorizontalLayout();
		buyerAndExportLayout.getStyle().set("border", "1px solid black");
		buyerAndExportLayout.add(buyerDetailLayout);
		buyerAndExportLayout.add(buyerAddressLayout);
		buyerAndExportLayout.add(exportLayout);

		itemGrid = new Grid<>(InvoiceItem.class);
		itemGrid.setWidthFull();
		itemGrid.setItems();
		itemGrid.setColumns();
		itemGrid.addColumn(ii -> ii.getItem().getName()).setAutoWidth(true).setHeader("Item Name");
		itemGrid.addColumn(ii -> ii.getItem().getCategory().getName()).setWidth("80px").setHeader("Category");

//		itemGrid.addColumn(ii -> ii.getUnitPrice()).setHeader("Unit Price");
		itemGrid.addColumn(TemplateRenderer.<InvoiceItem>of("<div align='right'>[[item.unitPrice]]</div>").withProperty("unitPrice",
				ii -> Util.roundToTwoDigitStr(ii.getUnitPrice()))).setWidth("50px").setHeader("Unit Price");

		itemGrid.addComponentColumn(ii -> addQuantityElementAndListener(ii)).setWidth("40px").setHeader("Quantity");

//		itemGrid.addColumn(ii -> ii.getNetAmount()).setHeader("Net Amount");
		itemGrid.addColumn(TemplateRenderer.<InvoiceItem>of("<div align='right'>[[item.netAmount]]</div>").withProperty("netAmount",
				ii -> Util.roundToTwoDigitStr(ii.getNetAmount()))).setWidth("50px").setHeader("Net Amount");

		itemGrid.addColumn(ii -> ii.getCgstRate()).setWidth("20px").setHeader("CGST(%)");

//		itemGrid.addColumn(ii -> ii.getCgstAmount()).setHeader("CGST Amount");
		itemGrid.addColumn(TemplateRenderer.<InvoiceItem>of("<div align='right'>[[item.cgstAmount]]</div>").withProperty("cgstAmount",
				ii -> Util.roundToTwoDigitStr(ii.getCgstAmount()))).setWidth("50px").setHeader("Cgst Amount");

		itemGrid.addColumn(ii -> ii.getSgstRate()).setWidth("20px").setHeader("SGST(%)");

//		itemGrid.addColumn(ii -> ii.getSgstAmount()).setHeader("SGST Amount");
		itemGrid.addColumn(TemplateRenderer.<InvoiceItem>of("<div align='right'>[[item.sgstAmount]]</div>").withProperty("sgstAmount",
				ii -> Util.roundToTwoDigitStr(ii.getSgstAmount()))).setWidth("50px").setHeader("Sgst Amount");

//		itemGrid.addColumn(ii -> ii.getTotalAmount()).setHeader("Total Amount");
		itemGrid.addColumn(TemplateRenderer.<InvoiceItem>of("<div align='right'>[[item.totalAmount]]</div>").withProperty("totalAmount",
				ii -> Util.roundToTwoDigitStr(ii.getTotalAmount()))).setWidth("60px").setHeader("Total Amount");

		itemGrid.setHeight("300px");
		itemGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

		saveEle = new Button("Save");
		saveEle.setMaxWidth("150px");
		saveEle.addClickListener(click -> processForm());

		formLayout = new FormLayout();
		formLayout.addFormItem(sellerEle, "Seller");
		formLayout.addFormItem(invoiceTypeEle, "Invoice Type");
		formLayout.add(buyerAndExportLayout);
		formLayout.add(itemGrid);
		formLayout.add(saveEle);
		add(formLayout);

		formLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), new ResponsiveStep("600px", 1, LabelsPosition.ASIDE));

	}

	private void setInvoiceItems() {
		Seller seller = sellerEle.getValue();

		if (seller != null) {
			List<ItemPrice> itemPrices = itemPriceService.getItemPrices(seller);
			items.clear();

			for (ItemPrice itemPrice : itemPrices) {
				InvoiceItem invoiceItem = new InvoiceItem();
				invoiceItem.setItem(itemPrice.getItem());
				invoiceItem.setUnit(itemPrice.getItem().getUnit());
				invoiceItem.setUnitPrice(itemPrice.getPrice());
				invoiceItem.setCgstRate(taxRateService.getTaxRate(invoiceItem.getItem().getCategory()).getCgstRate());
				invoiceItem.setSgstRate(taxRateService.getTaxRate(invoiceItem.getItem().getCategory()).getSgstRate());
				items.add(invoiceItem);
			}
			itemGrid.setItems(items);

		} else {
			items.clear();
			itemGrid.setItems(items);
		}
	}

	private Component addQuantityElementAndListener(InvoiceItem invoiceItem) {
		quantityEle = new NumberField();
		quantityEle.setWidth("120px");
		quantityEle.setHasControls(true);
		quantityEle.setMin(0);

		quantityEle.addValueChangeListener(quantiyListener -> {
			// To avoid recursion
			if ("ProgramaticallyChanged".equals(quantityEle.getTitle())) {
				quantityEle.setTitle(null);
				return;
			}
			Double quantity = quantiyListener.getValue();
			generateBill(quantity.intValue(), invoiceItem);
			itemGrid.getDataProvider().refreshItem(invoiceItem);
			quantityEle.setTitle("ProgramaticallyChanged");
			quantityEle.setValue(quantity);
		});

		return quantityEle;
	}

	private void generateBill(int quantity, InvoiceItem invoiceItem) {

		invoiceItem.setQuantity(quantity);

		double netAmount = Util.roundToTwoDigit(invoiceItem.getUnitPrice() * quantity);
		invoiceItem.setNetAmount(netAmount);

		double cgstAmount = Util.roundToTwoDigit((invoiceItem.getCgstRate() * netAmount) / 100);
		invoiceItem.setCgstAmount(cgstAmount);

		double sgstAmount = Util.roundToTwoDigit((invoiceItem.getSgstRate() * netAmount) / 100);
		invoiceItem.setSgstAmount(sgstAmount);

		double totalAmount = Util.roundToTwoDigit(netAmount + cgstAmount + sgstAmount);
		invoiceItem.setTotalAmount(totalAmount);

	}

	private void processForm() {
		Seller seller = sellerEle.getValue();

		String invoiceTypeStr = invoiceTypeEle.getValue();
		int invoiceType = InvoiceUtils.invoiceTypeInt(invoiceTypeStr);

		Buyer buyer = buyerEle.getValue();

//		String vehicleNumber;
//		String transporterName;

		List<InvoiceItem> invoiceItems = this.items;

		int invoiceItemQuantity = 0;
		double invoiceNetAmount = 0;
		double invoiceCgstAmount = 0;
		double invoiceSgstAmount = 0;
		double invoiceIgstAmount = 0;
		double invoiceTotalAmount = 0;

		for (InvoiceItem invoiceItem : invoiceItems) {
			invoiceItemQuantity += invoiceItem.getQuantity();
			invoiceNetAmount += invoiceItem.getNetAmount();

			if (InvoiceUtils.isIGST(seller, buyer)) {
				invoiceItem.setIgstAmount(invoiceItem.getCgstAmount() + invoiceItem.getSgstAmount());
				invoiceItem.setIgstRate(invoiceItem.getCgstRate() + invoiceItem.getSgstRate());

				// Reset Sgst(Rate/Amount) and Sgst (Rate/Amount)
				invoiceItem.setCgstRate(0);
				invoiceItem.setCgstAmount(0);
				invoiceItem.setSgstRate(0);
				invoiceItem.setSgstAmount(0);

				invoiceIgstAmount += invoiceItem.getIgstAmount();
			} else {
				invoiceCgstAmount += invoiceItem.getCgstAmount();
				invoiceSgstAmount += invoiceItem.getSgstAmount();
			}
			invoiceTotalAmount += invoiceItem.getTotalAmount();
		}

		Invoice invoice = new Invoice();

		invoice.setCreationDate(LocalDateTime.now());
		invoice.setType(invoiceType);
		invoice.setSeller(seller);
		invoice.setBuyer(buyer);
		invoice.setStatusCode(InvoiceStatus.ACTIVE);
		invoice.setTotalQuantity(invoiceItemQuantity);
		invoice.setTotalNetAmount(invoiceNetAmount);

		if (InvoiceUtils.isIGST(seller, buyer)) {
			invoice.setTotalIgstAmount(invoiceIgstAmount);
		} else {
			invoice.setTotalCgstAmount(invoiceCgstAmount);
			invoice.setTotalSgstAmount(invoiceSgstAmount);
		}
		invoice.setTotalAmount(invoiceTotalAmount);
		invoice.setInvoiceNumber(invoiceSeriesService.generateInvoiceNumber(invoice));
		invoice.setInvoiceItems(invoiceItems);

		InvoiceBuilder invoiceBuilder = InvoiceBuilderFactory.getInvoiceBuilder(invoice);
		byte[] invoicePdfData = invoiceBuilder.createInvoicePdf();
		invoice.setInvoicePdf(new InvoicePdf(invoicePdfData, invoice));

		System.out.println(invoice);
	}

}
