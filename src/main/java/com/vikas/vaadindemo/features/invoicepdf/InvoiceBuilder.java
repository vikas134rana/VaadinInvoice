package com.vikas.vaadindemo.features.invoicepdf;

import java.io.IOException;

public interface InvoiceBuilder {

	public byte[] createInvoicePdf();

	public byte[] cancelInvoicePdf() throws IOException;
}
