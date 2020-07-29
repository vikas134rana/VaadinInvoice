package com.vikas.vaadindemo.features.invoicepdf;

import com.itextpdf.layout.element.Table;

public interface InvoiceStructure {

	public void createNewPage(PdfCopy pdfCopy);

	public void addPageHeader(Table pageTable, PdfCopy pdfCopy);

	public void addInvoiceType(Table pageTable);

	public void addAddress(Table pageTable);

	public void addItems(Table pageTable);

	public void addSummary(Table pageTable);

	public void addPageFooter(Table pageTable);

}
