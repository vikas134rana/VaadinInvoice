package com.vikas.vaadindemo.ui.invoiceentry;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Buyer;
import com.vikas.vaadindemo.entity.Export;
import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.entity.InvoiceItem;
import com.vikas.vaadindemo.entity.InvoicePdf;
import com.vikas.vaadindemo.entity.ItemPrice;
import com.vikas.vaadindemo.entity.Port;
import com.vikas.vaadindemo.entity.Seller;
import com.vikas.vaadindemo.entity.enums.InvoiceStatus;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceBuilder;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceBuilderFactory;
import com.vikas.vaadindemo.service.BuyerService;
import com.vikas.vaadindemo.service.InvoiceSeriesService;
import com.vikas.vaadindemo.service.InvoiceService;
import com.vikas.vaadindemo.service.ItemPriceService;
import com.vikas.vaadindemo.service.SellerService;
import com.vikas.vaadindemo.service.TaxRateService;
import com.vikas.vaadindemo.ui.common.InvoiceDialog;
import com.vikas.vaadindemo.ui.invoiceprinting.InvoicePrintingView;
import com.vikas.vaadindemo.util.InvoiceUtils;
import com.vikas.vaadindemo.util.Util;

@UIScope
@SpringComponent
public class InvoiceEntryView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	InvoicePrintingView invoicePrintingView;

	@Autowired
	AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Autowired
	InvoiceService invoiceService;

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

	@Autowired
	private BuyerDetailFormComponent buyerDetailFormComponent;

	@Autowired
	private BuyerAddressComponent buyerAddressComponent;

	@Autowired
	private ExportFormComponent exportFormComponent;

	private FormLayout formLayout;
	private ComboBox<Seller> sellerEle;
	private RadioButtonGroup<String> invoiceTypeEle;
	private HorizontalLayout buyerAndExportLayout;
	private VerticalLayout buyerDetailLayout;
	private VerticalLayout buyerAddressLayout;
	private VerticalLayout exportLayout;
	private Grid<InvoiceItem> itemGrid;
	private Button saveEle;
	private Button resetEle;
	private NumberField quantityEle;
	private List<InvoiceItem> items = new ArrayList<InvoiceItem>();

	private Export export;

	@PostConstruct
	public void init() {

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

			setBuyerDetailFormItem(invoiceType);

			if (invoiceType == 4) {
				autowireCapableBeanFactory.autowireBean(ExportFormComponent.class); // create new object through autowire
				exportLayout.add(exportFormComponent);
			} else {
				exportLayout.removeAll();
			}
		});

		buyerDetailFormComponent.getBuyerEle().addValueChangeListener(listener -> setBuyerAddressValues(listener.getValue()));

		buyerDetailLayout = new VerticalLayout();
		buyerDetailLayout.setWidth("50%");
		buyerDetailLayout.add(buyerDetailFormComponent);
		buyerDetailLayout.getStyle().set("border-right", "1px solid black");

		buyerAddressLayout = new VerticalLayout();
		buyerAddressLayout.setWidth("70%");
		buyerAddressLayout.add(buyerAddressComponent);
		buyerAddressLayout.getStyle().set("border-right", "1px solid black");

		exportLayout = new VerticalLayout();
		exportLayout.add(new Text(""));
//		exportLayout.getStyle().set("border", "1px solid black");

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

		resetEle = new Button("Reset");
		resetEle.getStyle().set("margin-right", "20px");
		resetEle.setMaxWidth("150px");
		resetEle.addClickListener(click -> reset());

		saveEle = new Button("Save");
		saveEle.setMaxWidth("150px");
		saveEle.addClickListener(click -> processForm());

		formLayout = new FormLayout();
		formLayout.addFormItem(sellerEle, "Seller");
		formLayout.addFormItem(invoiceTypeEle, "Invoice Type");
		formLayout.add(buyerAndExportLayout);
		formLayout.add(itemGrid);
		formLayout.add(resetEle);
		formLayout.add(saveEle);
		add(formLayout);

		formLayout.setResponsiveSteps(new ResponsiveStep("0px", 1, LabelsPosition.ASIDE));

	}

	private void setBuyerDetailFormItem(int invoiceType) {

		FormItem vehicleNumberFormItem = buyerDetailFormComponent.getVehicleNumberFormItem();
		FormItem transporterNameFormItem = buyerDetailFormComponent.getTransporterNameFormItem();
		FormItem invoiceDateFormItem = buyerDetailFormComponent.getInvoiceDateFormItem();

		if (invoiceType == 1 || invoiceType == 5 || invoiceType == 6) {
			vehicleNumberFormItem.setVisible(true);
			transporterNameFormItem.setVisible(true);
			invoiceDateFormItem.setVisible(false);

		} else if (invoiceType == 2 || invoiceType == 4 || invoiceType == 7 || invoiceType == 8) {
			vehicleNumberFormItem.setVisible(false);
			transporterNameFormItem.setVisible(false);
			invoiceDateFormItem.setVisible(false);

		} else if (invoiceType == 3) {
			vehicleNumberFormItem.setVisible(false);
			transporterNameFormItem.setVisible(false);
			invoiceDateFormItem.setVisible(true);

		}

	}

	private void setBuyerAddressValues(Buyer buyer) {

		if (buyer != null) {
			// set buyer address
			buyerAddressComponent.getBuyerNameEle().setText(buyer.getAddress().getName());
			buyerAddressComponent.getBuyerAddressEle().setText(buyer.getAddress().getAddressDetail());
			buyerAddressComponent.getBuyerPincodeEle().setText(buyer.getAddress().getPincode());
			buyerAddressComponent.getBuyerStateEle().setText(buyer.getAddress().getState().getName());
			buyerAddressComponent.getBuyerCountryEle().setText(buyer.getAddress().getCountry().getName());
			buyerAddressComponent.getBuyerGstinEle().setText(buyer.getAddress().getGstin());
		} else {
			// reset buyer address
			buyerAddressComponent.getBuyerNameEle().setText(null);
			buyerAddressComponent.getBuyerAddressEle().setText(null);
			buyerAddressComponent.getBuyerPincodeEle().setText(null);
			buyerAddressComponent.getBuyerStateEle().setText(null);
			buyerAddressComponent.getBuyerCountryEle().setText(null);
			buyerAddressComponent.getBuyerGstinEle().setText(null);
		}
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
			if (!quantiyListener.isFromClient())
				return;
			Double quantity = quantiyListener.getValue();
			generateBill(quantity.intValue(), invoiceItem);
			itemGrid.getDataProvider().refreshItem(invoiceItem);
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

		Invoice invoice = new Invoice();

		Seller seller = sellerEle.getValue();
		if (seller == null) {
			add(new InvoiceDialog("Validation Fail", "Please Select Seller"));
			return;
		}

		String invoiceTypeStr = invoiceTypeEle.getValue();
		int invoiceType = InvoiceUtils.invoiceTypeInt(invoiceTypeStr);
		if (invoiceType == 0) {
			add(new InvoiceDialog("Validation Fail", "Please Select InvoiceType"));
			return;
		}

		Buyer buyer = buyerDetailFormComponent.getBuyerEle().getValue();
		if (buyer == null) {
			add(new InvoiceDialog("Validation Fail", "Please Select Buyer"));
			return;
		}

		String remarks = buyerDetailFormComponent.getRemarksEle().getValue();
		String vehicleNumber = "";
		String transporterName = "";
		LocalDateTime invoiceDate = LocalDateTime.now();

		TextField vehicleNumberEle = buyerDetailFormComponent.getVehicleNumberEle();
		TextField transporterNameEle = buyerDetailFormComponent.getTransporterNameEle();
		DatePicker invoiceDateEle = buyerDetailFormComponent.getInvoiceDateEle();

		if (invoiceType == 1 || invoiceType == 5 || invoiceType == 6) {
			vehicleNumber = vehicleNumberEle.getValue();
			transporterName = transporterNameEle.getValue();

		} else if (invoiceType == 2 || invoiceType == 4 || invoiceType == 7 || invoiceType == 8) {
			// Buyer and Remarks (value already stored)

		} else if (invoiceType == 3) {
			if (invoiceDateEle.getValue() != null)
				invoiceDate = invoiceDateEle.getValue().atTime(LocalTime.now());
		}

		if (invoiceType == 4) {

			String currency = exportFormComponent.getCurrencyEle().getValue();
			if (currency == null) {
				add(new InvoiceDialog("Validation Fail", "Please select currency"));
				return;
			}

			Double exchangeRate = exportFormComponent.getExchangeRateEle().getValue();
			if (exchangeRate == null) {
				add(new InvoiceDialog("Validation Fail", "Please select exchange rate"));
				return;
			}

			Port port = exportFormComponent.getPortCodeEle().getValue();
			if (port == null) {
				add(new InvoiceDialog("Validation Fail", "Please select port code"));
				return;
			}

			String igstTypeStr = exportFormComponent.getIgstTypeEle().getValue();
			int igstType = InvoiceUtils.getIgstType(igstTypeStr);
			if (igstType == 0) {
				add(new InvoiceDialog("Validation Fail", "Please select igst type"));
				return;
			}

			export = new Export();
			export.setCurrency(currency);
			export.setExchangeRate(exchangeRate.intValue());
			export.setPort(port);
			export.setIgstType(igstType);
			export.setInvoice(invoice);
		}

		List<InvoiceItem> invoiceItems = this.items;

		int invoiceItemQuantity = 0;
		double invoiceNetAmount = 0;
		double invoiceCgstAmount = 0;
		double invoiceSgstAmount = 0;
		double invoiceIgstAmount = 0;
		double invoiceTotalAmount = 0;

		// calculate invoice total Quantity and (Net/CGST/SGST/IGST/Total)amount
		for (InvoiceItem invoiceItem : invoiceItems) {

			invoiceItem.setInvoice(invoice);

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

		if (invoiceItemQuantity <= 0) {
			add(new InvoiceDialog("Validation Fail", "Please select atleast one Item"));
			return;
		}

		invoice.setCreationDate(invoiceDate);
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
		invoice.setVehicleNumber(vehicleNumber);
		invoice.setTransporterName(transporterName);
		invoice.setInvoiceNumber(invoiceSeriesService.generateInvoiceNumber(invoice));
		invoice.setInvoiceItems(invoiceItems);

		invoice.setExport(export);
		invoice.setRemarks(remarks);

		InvoiceBuilder invoiceBuilder = InvoiceBuilderFactory.getInvoiceBuilder(invoice);
		byte[] invoicePdfData = invoiceBuilder.createInvoicePdf();
		invoice.setInvoicePdf(new InvoicePdf(invoicePdfData, invoice));

		System.out.println(invoice);
		invoiceService.save(invoice);

		add(new InvoiceDialog("Success", "Invoice created " + invoice.getInvoiceNumber()));
		reset();
	}

	// TODO: implement InvoiceEntry form reset logic
	private void reset() {
	}

}
