package com.vikas.vaadindemo.features.invoicepdf.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.vikas.vaadindemo.entity.Address;
import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.entity.InvoiceItem;
import com.vikas.vaadindemo.entity.Item;
import com.vikas.vaadindemo.entity.enums.InvoiceStatus;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceBuilder;
import com.vikas.vaadindemo.features.invoicepdf.InvoiceStructure;
import com.vikas.vaadindemo.features.invoicepdf.PdfCopy;
import com.vikas.vaadindemo.service.ItemPriceService;
import com.vikas.vaadindemo.util.InvoiceUtils;
import com.vikas.vaadindemo.util.PdfUtils;
import com.vikas.vaadindemo.util.Util;

public class GenericInvoicePDF implements InvoiceStructure, InvoiceBuilder {

	@Autowired
	ItemPriceService itemPriceService;

	private Document doc;
	private Invoice invoice;

	private static final float[] itemTableColumnWidths = { 4, 16, 10, 7, 7, 8, 5, 8, 5, 8, 5, 8, 9 };

	public GenericInvoicePDF(Invoice invoice) {
		this.invoice = invoice;
	}

	@Override
	public byte[] createInvoicePdf() {
		System.out.println("Generic createInvoicePdf");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(bos));
		this.doc = new Document(pdfDoc, PageSize.A4);
		this.doc.setMargins(10, 10, 10, 10);

		createNewPage(PdfCopy.SELLER);
		createNewPage(PdfCopy.RECEPIENT);
		createNewPage(PdfCopy.TRANSPORT);
		createNewPage(PdfCopy.EXTRA);

		this.doc.close();

		System.out.println("Invoice Created: " + invoice.getInvoiceNumber());

		return bos.toByteArray();
	}

	@Override
	public byte[] cancelInvoicePdf() throws IOException {
		System.out.println("Generic cancelInvoicePdf");

		if (invoice.getStatusCode() == InvoiceStatus.CANCELLED) {
			throw new IllegalStateException("Cannot cancel already cancelled invoice " + invoice.getInvoiceNumber());
		}

		byte[] invoicePdfData = invoice.getInvoicePdf().getPdfData();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		// Add Watermark to pdf
		// (URL:https://kb.itextpdf.com/home/it7kb/examples/watermark-examples)
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(invoicePdfData)), new PdfWriter(bos));
		Document doc = new Document(pdfDoc);

		Resource resource = new ClassPathResource("static\\images\\cancel.jpg");
		ImageData img = ImageDataFactory.create(resource.getFile().getAbsolutePath());

		float w = img.getWidth();
		float h = img.getHeight();

		PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.5f);

		// Implement transformation matrix usage in order to scale image
		for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {

			PdfPage pdfPage = pdfDoc.getPage(i);
			Rectangle pageSize = pdfPage.getPageSize();

			float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
			float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
			PdfCanvas over = new PdfCanvas(pdfPage);
			over.saveState();
			over.setExtGState(gs1);
			over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2), false);
			over.restoreState();
		}

		doc.close();

		return bos.toByteArray();
	}

	@Override
	public void createNewPage(PdfCopy pdfCopy) {
		System.out.println("Generic createNewPage");

		int currentPageNumber = this.doc.getPdfDocument().getNumberOfPages();

		if (currentPageNumber >= 1)
			this.doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE)); // add new Page

		Table pageTable = new Table(1);
		pageTable.setWidth(UnitValue.createPercentValue(100));

		addPageHeader(pageTable, pdfCopy);
		addInvoiceType(pageTable);
		addAddress(pageTable);
		addItems(pageTable);
		addSummary(pageTable);
		addPageFooter(pageTable);

		this.doc.add(pageTable);
	}

	@Override
	public void addPageHeader(Table pageTable, PdfCopy pdfCopy) {
		System.out.println("Generic pageHeader");

		Table pagHeaderTable = new Table(2).setWidth(UnitValue.createPercentValue(100));

//		Cell cell1 = new Cell().add(new Paragraph("SAMSUNG"));
		Resource resource = new ClassPathResource("static\\images\\samsung_logo.png");
		ImageData imageData = null;
		try {
			imageData = ImageDataFactory.create(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Image samsungLogo = new Image(imageData);
		samsungLogo.scaleToFit(100, 33);

		Cell cell1 = new Cell().add(samsungLogo);
		Cell cell2 = new Cell().add(new Paragraph(pdfCopy.getValue())).addStyle(TIMES_N_9()).setTextAlignment(TextAlignment.RIGHT);

		pagHeaderTable.addCell(cell1);
		pagHeaderTable.addCell(cell2);

		pageTable.addCell(pagHeaderTable);
	}

	@Override
	public void addInvoiceType(Table pageTable) {
		System.out.println("Generic invoiceHeader");

		Table invoiceTypeTable = new Table(1).setWidth(UnitValue.createPercentValue(100));
		Cell cell1 = new Cell().add(new Paragraph(InvoiceUtils.invoiceTypeValue(invoice.getType()))).addStyle(TIMES_B_9())
				.setTextAlignment(TextAlignment.CENTER);
		invoiceTypeTable.addCell(cell1);

		pageTable.addCell(invoiceTypeTable);
	}

	@Override
	public void addAddress(Table pageTable) {
		System.out.println("Generic address");

		Table addressTable = new Table(3).setWidth(UnitValue.createPercentValue(100)).setFixedLayout();

		// Seller Address
		Address sellerAddress = invoice.getSeller().getAddress();
		Table sellerAddressTable = new Table(UnitValue.createPercentArray(new float[] { 1, 4 })).setWidth(UnitValue.createPercentValue(100)).setFixedLayout();

		Cell sellerAddressHeaderCell = new Cell(1, 2).add(new Paragraph("Seller Address")).addStyle(TIMES_B_9());
		sellerAddressTable.addCell(sellerAddressHeaderCell);

		Cell sellerNameCell = new Cell().add(new Paragraph("Name: "));
		Cell sellerNameValueCell = new Cell().add(new Paragraph(sellerAddress.getName()));
		sellerAddressTable.addCell(sellerNameCell).addCell(sellerNameValueCell);

		Cell sellerAddressCell = new Cell().add(new Paragraph("Address: "));
		Cell sellerAddressValueCell = new Cell().add(new Paragraph(sellerAddress.getAddressDetail()));
		sellerAddressTable.addCell(sellerAddressCell).addCell(sellerAddressValueCell);

		Cell sellerPincodeCell = new Cell().add(new Paragraph("Pincode: "));
		Cell sellerPincodeValueCell = new Cell().add(new Paragraph(sellerAddress.getPincode()));
		sellerAddressTable.addCell(sellerPincodeCell).addCell(sellerPincodeValueCell);

		Cell sellerStateCell = new Cell().add(new Paragraph("State: "));
		Cell sellerStateValueCell = new Cell().add(new Paragraph(sellerAddress.getState().getName()));
		sellerAddressTable.addCell(sellerStateCell).addCell(sellerStateValueCell);

		Cell sellerCountryCell = new Cell().add(new Paragraph("Country: "));
		Cell sellerCountryValueCell = new Cell().add(new Paragraph(sellerAddress.getCountry().getName()));
		sellerAddressTable.addCell(sellerCountryCell).addCell(sellerCountryValueCell);

		Cell sellerGstinCell = new Cell().add(new Paragraph("GSTIN: "));
		Cell sellerGstinValueCell = new Cell().add(new Paragraph(sellerAddress.getGstin()));
		sellerAddressTable.addCell(sellerGstinCell).addCell(sellerGstinValueCell);

		addressTable.addCell(sellerAddressTable).addStyle(TIMES_N_9());

		// Buyer Address
		Address buyerAddress = invoice.getBuyer().getAddress();
		Table buyerAddressTable = new Table(UnitValue.createPercentArray(new float[] { 1, 4 })).setWidth(UnitValue.createPercentValue(100)).setFixedLayout();

		Cell buyerAddressHeaderCell = new Cell(1, 2).add(new Paragraph("Buyer Address")).addStyle(TIMES_B_9());
		buyerAddressTable.addCell(buyerAddressHeaderCell);

		Cell buyerNameCell = new Cell().add(new Paragraph("Name: "));
		Cell buyerNameValueCell = new Cell().add(new Paragraph(buyerAddress.getName()));
		buyerAddressTable.addCell(buyerNameCell).addCell(buyerNameValueCell);

		Cell buyerAddressCell = new Cell().add(new Paragraph("Address: "));
		Cell buyerAddressValueCell = new Cell().add(new Paragraph(buyerAddress.getAddressDetail()));
		buyerAddressTable.addCell(buyerAddressCell).addCell(buyerAddressValueCell);

		Cell buyerPincodeCell = new Cell().add(new Paragraph("Pincode: "));
		Cell buyerPincodeValueCell = new Cell().add(new Paragraph(buyerAddress.getPincode()));
		buyerAddressTable.addCell(buyerPincodeCell).addCell(buyerPincodeValueCell);

		Cell buyerStateCell = new Cell().add(new Paragraph("State: "));
		Cell buyerStateValueCell = new Cell().add(new Paragraph(buyerAddress.getState().getName()));
		buyerAddressTable.addCell(buyerStateCell).addCell(buyerStateValueCell);

		Cell buyerCountryCell = new Cell().add(new Paragraph("Country: "));
		Cell buyerCountryValueCell = new Cell().add(new Paragraph(buyerAddress.getCountry().getName()));
		buyerAddressTable.addCell(buyerCountryCell).addCell(buyerCountryValueCell);

		Cell buyerGstinCell = new Cell().add(new Paragraph("GSTIN: "));
		Cell buyerGstinValueCell = new Cell().add(new Paragraph(buyerAddress.getGstin()));
		buyerAddressTable.addCell(buyerGstinCell).addCell(buyerGstinValueCell);

		addressTable.addCell(buyerAddressTable).addStyle(TIMES_N_9());

		// Place of Supply
		Table placeOfSupplyTable = new Table(UnitValue.createPercentArray(new float[] { 1, 4 })).setWidth(UnitValue.createPercentValue(100)).setFixedLayout();

		Paragraph placeOfSupplyHeadPara = new Paragraph("Place of Supply").addStyle(TIMES_B_9());
		Cell placeOfSupplyHeadCell = new Cell(1, 2).add(placeOfSupplyHeadPara);
		placeOfSupplyTable.addCell(placeOfSupplyHeadCell);

		Paragraph placeOfSupplyDetailPara = new Paragraph("Place of Supply Details").addStyle(TIMES_N_9());
		Cell placeOfSupplyDetailCell = new Cell(1, 2).add(placeOfSupplyDetailPara);
		placeOfSupplyTable.addCell(placeOfSupplyDetailCell);

		addressTable.addCell(placeOfSupplyTable);

		pageTable.addCell(addressTable);
	}

	@Override
	public void addItems(Table pageTable) {
		System.out.println("Generic items");

		Table itemTableHead = new Table(UnitValue.createPercentArray(itemTableColumnWidths)).setWidth(UnitValue.createPercentValue(100)).setFixedLayout();

		List<InvoiceItem> invoiceItems = invoice.getInvoiceItems();

		itemTableHead.addCell(new Paragraph("S.No"));
		itemTableHead.addCell(new Paragraph("Item"));
		itemTableHead.addCell(new Paragraph("Category"));
		itemTableHead.addCell(new Paragraph("Price"));
		itemTableHead.addCell(new Paragraph("Quantity"));
		itemTableHead.addCell(new Paragraph("Net Amount"));
		itemTableHead.addCell(new Paragraph("CGST Rate"));
		itemTableHead.addCell(new Paragraph("CGST Amount"));
		itemTableHead.addCell(new Paragraph("SGST Rate"));
		itemTableHead.addCell(new Paragraph("CGST Amount"));
		itemTableHead.addCell(new Paragraph("IGST Rate"));
		itemTableHead.addCell(new Paragraph("IGST Amount"));
		itemTableHead.addCell(new Cell().add(new Paragraph("Total Amount")).setTextAlignment(TextAlignment.RIGHT));
		itemTableHead.addStyle(TIMES_B_8());

		Table itemTableData = new Table(UnitValue.createPercentArray(itemTableColumnWidths)).setWidth(UnitValue.createPercentValue(100)).setFixedLayout();
		for (int i = 0; i < invoiceItems.size(); i++) {
			InvoiceItem invoiceItem = invoiceItems.get(i);
			Item item = invoiceItem.getItem();

			String srNo = "" + (i + 1);
			itemTableData.addCell(new Paragraph(srNo)); // SR NO.

			String itemName = invoiceItem.getItem().getName();
			itemTableData.addCell(new Paragraph(itemName)); // Item

			String categoryName = item.getCategory().getName();
			itemTableData.addCell(new Paragraph(categoryName)); // Category

			String price = Util.roundToTwoDigitStr(invoiceItem.getUnitPrice());
			itemTableData.addCell(new Paragraph(price).setTextAlignment(TextAlignment.RIGHT)); // Price

			String quantity = "" + invoiceItem.getQuantity();
			itemTableData.addCell(new Paragraph(quantity).setTextAlignment(TextAlignment.RIGHT)); // Quantity

			String netAmount = "" + Util.roundToTwoDigitStr(invoiceItem.getNetAmount());
			itemTableData.addCell(new Paragraph(netAmount).setTextAlignment(TextAlignment.RIGHT)); // Net Amount

			String cgstRate = "" + invoiceItem.getCgstRate();
			itemTableData.addCell(new Paragraph(cgstRate).setTextAlignment(TextAlignment.RIGHT)); // Cgst Rate

			String cgstAmount = Util.roundToTwoDigitStr(invoiceItem.getCgstAmount());
			itemTableData.addCell(new Paragraph(cgstAmount).setTextAlignment(TextAlignment.RIGHT)); // Cgst Amount

			String sgstRate = "" + invoiceItem.getSgstRate();
			itemTableData.addCell(new Paragraph(sgstRate).setTextAlignment(TextAlignment.RIGHT)); // Sgst Rate

			String sgstAmount = Util.roundToTwoDigitStr(invoiceItem.getSgstAmount());
			itemTableData.addCell(new Paragraph(sgstAmount).setTextAlignment(TextAlignment.RIGHT)); // Sgst Amount

			String igstRate = "" + invoiceItem.getIgstRate();
			itemTableData.addCell(new Paragraph(igstRate).setTextAlignment(TextAlignment.RIGHT)); // Igst Rate

			String igstAmount = Util.roundToTwoDigitStr(invoiceItem.getIgstAmount());
			itemTableData.addCell(new Paragraph(igstAmount).setTextAlignment(TextAlignment.RIGHT)); // Igst Amount

			String totalAmount = Util.roundToTwoDigitStr(invoiceItem.getTotalAmount());
			itemTableData.addCell(new Cell().add(new Paragraph(totalAmount)).setTextAlignment(TextAlignment.RIGHT)); // Total Amount Price
			itemTableData.addStyle(TIMES_N_8());
		}

		pageTable.addCell(itemTableHead);
		pageTable.addCell(itemTableData);

	}

	@Override
	public void addSummary(Table pageTable) {
		System.out.println("Generic summary");

		Table summaryTable = new Table(UnitValue.createPercentArray(itemTableColumnWidths)).setWidth(UnitValue.createPercentValue(100)).setFixedLayout();

		int firstColSpan = 9;
		int secondColSpan = itemTableColumnWidths.length - firstColSpan;

		// Grand Total
		Paragraph totalQuantityPara = new Paragraph("" + invoice.getTotalQuantity()).setTextAlignment(TextAlignment.RIGHT);
		Paragraph totalNetAmountPara = new Paragraph(Util.roundToTwoDigitStr(invoice.getTotalNetAmount())).setTextAlignment(TextAlignment.RIGHT);
		Paragraph totalCgstAmountPara = new Paragraph(Util.roundToTwoDigitStr(invoice.getTotalCgstAmount())).setTextAlignment(TextAlignment.RIGHT);
		Paragraph totalSgstAmountPara = new Paragraph(Util.roundToTwoDigitStr(invoice.getTotalSgstAmount())).setTextAlignment(TextAlignment.RIGHT);
		Paragraph totalIgstAmountPara = new Paragraph(Util.roundToTwoDigitStr(invoice.getTotalIgstAmount())).setTextAlignment(TextAlignment.RIGHT);
		Paragraph totalAmountPara = new Paragraph(Util.roundToTwoDigitStr(invoice.getTotalAmount())).setTextAlignment(TextAlignment.RIGHT);

		summaryTable.addCell(new Cell(1, 4).add(new Paragraph("GRAND TOTAL")));
		summaryTable.addCell(totalQuantityPara);
		summaryTable.addCell(totalNetAmountPara);
		summaryTable.addCell("").addCell(totalCgstAmountPara);
		summaryTable.addCell("").addCell(totalSgstAmountPara);
		summaryTable.addCell("").addCell(totalIgstAmountPara);
		summaryTable.addCell(totalAmountPara);

		// Total Amount
		double totalAmount = invoice.getTotalAmount();
		totalAmountPara = new Paragraph("Total Amount In Words: " + Util.convertToWordsIndianCurrency(totalAmount));
		Cell blankCell = new Cell(1, firstColSpan).add(totalAmountPara);

		Text totalAmountText = new Text("Total Amount:  ").setTextAlignment(TextAlignment.LEFT);
		Text totalAmountValueText = new Text("" + Util.roundToTwoDigitStr(totalAmount)).setTextAlignment(TextAlignment.RIGHT).setFont(TIMES_B());
		Cell totalAmountCell = new Cell(1, secondColSpan).add(new Paragraph().add(totalAmountText).add(totalAmountValueText))
				.setTextAlignment(TextAlignment.RIGHT);

		summaryTable.addCell(blankCell).addCell(totalAmountCell);

		// Amount in Words and Transport Detail
		Cell gstDetailCell = new Cell(1, firstColSpan);

		if (InvoiceUtils.isIGST(invoice.getSeller(), invoice.getBuyer())) {
			double totalIgstAmount = invoice.getTotalIgstAmount();
			Paragraph igstPara = new Paragraph("IGST in Words: " + Util.convertToWordsIndianCurrency(totalIgstAmount));
			gstDetailCell.add(igstPara);

		} else {
			double totalCgstAmount = invoice.getTotalCgstAmount();
			Paragraph cgstPara = new Paragraph("CGST in Words: " + Util.convertToWordsIndianCurrency(totalCgstAmount));
			double totalSgstAmount = invoice.getTotalSgstAmount();
			Paragraph sgstPara = new Paragraph("SGST in Words: " + Util.convertToWordsIndianCurrency(totalSgstAmount));
			gstDetailCell.add(cgstPara).add(sgstPara);
		}

		Paragraph reverseChargePara = new Paragraph("Whether reverse charge applicable: NO");
		gstDetailCell.add(reverseChargePara);

		Paragraph vehicleCell = new Paragraph("Vehicle Number: ");
		Paragraph transporterCell = new Paragraph("Transporter: ");
		Cell transportDetailCell = new Cell(1, secondColSpan).add(vehicleCell).add(transporterCell);

		summaryTable.addCell(gstDetailCell).addCell(transportDetailCell);

		// Remark and Signature
		Cell remarksCell = new Cell(1, firstColSpan).add(new Paragraph("Remarks: "));
		Cell authorizedSignatureCell = new Cell(1, secondColSpan).add(new Paragraph("\nAuthorized Signature: "));
		summaryTable.addCell(remarksCell).addCell(authorizedSignatureCell);

		pageTable.addCell(summaryTable).addStyle(TIMES_N_8());
	}

	@Override
	public void addPageFooter(Table pageTable) {
		System.out.println("Generic pageFooter");

		Table footerTable = new Table(1).setWidth(UnitValue.createPercentValue(100));

		String regOfficeText = "Reg. Office: Building, shop no. G31 Celekon, Jasola Vihar, New Delhi, Delhi 110025";
		Paragraph regOfficePara = new Paragraph(regOfficeText).setFont(TIMES_N());

		String websiteText = "Website: https://www.samsung.com/in";
		Paragraph websitePara = new Paragraph(websiteText).setFont(TIMES_N());

		String computerGeneratedText = "This is computer generated invoice";
		Paragraph computerGeneratedPara = new Paragraph(computerGeneratedText).setFont(TIMES_B());

		Cell footerCell = new Cell().add(regOfficePara).add(websitePara).add(computerGeneratedPara);
		footerTable.addCell(footerCell).setTextAlignment(TextAlignment.CENTER).setFontSize(7);

		pageTable.addCell(footerTable);
	}

	// ===============================================================================

	protected PdfFont TIMES_N() {
		return PdfUtils.font(StandardFonts.TIMES_ROMAN);
	}

	protected PdfFont TIMES_B() {
		return PdfUtils.font(StandardFonts.TIMES_BOLD);
	}

	protected Style TIMES_N_8() {
		return PdfUtils.fontStyle(StandardFonts.TIMES_ROMAN, 8);
	}

	protected Style TIMES_B_8() {
		return PdfUtils.fontStyle(StandardFonts.TIMES_BOLD, 8);
	}

	protected Style TIMES_N_9() {
		return PdfUtils.fontStyle(StandardFonts.TIMES_ROMAN, 9);
	}

	protected Style TIMES_B_9() {
		return PdfUtils.fontStyle(StandardFonts.TIMES_BOLD, 9);
	}

}
