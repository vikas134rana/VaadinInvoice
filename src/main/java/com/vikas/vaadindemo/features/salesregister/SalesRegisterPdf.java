package com.vikas.vaadindemo.features.salesregister;

import java.util.List;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.util.PdfUtils;

public class SalesRegisterPdf implements SalesRegister {

	private List<Invoice> invoices;
	private Document doc;

	public SalesRegisterPdf(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	@Override
	public byte[] generate() {
		System.out.println("Generic createInvoicePdf");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(bos));
		this.doc = new Document(pdfDoc, PageSize.A4);
		this.doc.setMargins(10, 10, 10, 10);

		Style Times_N_9 = PdfUtils.fontStyle(StandardFonts.TIMES_ROMAN, 9);
		Style Times_B_9 = PdfUtils.fontStyle(StandardFonts.TIMES_BOLD, 9);

		Table pageTable = new Table(8).setWidth(UnitValue.createPercentValue(100));
		pageTable.addCell(new Paragraph("S.No").addStyle(Times_B_9));
		pageTable.addCell(new Paragraph("Invoice Number").addStyle(Times_B_9));
		pageTable.addCell(new Paragraph("Creation Date").addStyle(Times_B_9));
		pageTable.addCell(new Paragraph("Status").addStyle(Times_B_9));
		pageTable.addCell(new Paragraph("Seller").addStyle(Times_B_9));
		pageTable.addCell(new Paragraph("Buyer").addStyle(Times_B_9));
		pageTable.addCell(new Paragraph("Total Quantity").addStyle(Times_B_9));
		pageTable.addCell(new Paragraph("Total Amount").addStyle(Times_B_9));

		int rowNum = 0;
		for (Invoice invoice : invoices) {
			pageTable.addCell(new Paragraph("" + (++rowNum)));
			pageTable.addCell(new Paragraph(invoice.getInvoiceNumber()));
			pageTable.addCell(new Paragraph("" + invoice.getCreationDate()));
			pageTable.addCell(new Paragraph("" + invoice.getStatusCode()));
			pageTable.addCell(new Paragraph(invoice.getSeller().getAddress().getName()));
			pageTable.addCell(new Paragraph(invoice.getBuyer().getAddress().getName()));
			pageTable.addCell(new Paragraph("" + invoice.getTotalQuantity()));
			pageTable.addCell(new Paragraph("" + invoice.getTotalAmount()));
			pageTable.addStyle(Times_N_9);
		}

		this.doc.add(pageTable);

		this.doc.close();
		return bos.toByteArray();
	}

}
