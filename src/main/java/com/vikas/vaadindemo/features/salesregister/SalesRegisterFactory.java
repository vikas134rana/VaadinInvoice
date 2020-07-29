package com.vikas.vaadindemo.features.salesregister;

import java.util.List;

import com.vikas.vaadindemo.entity.Invoice;

public class SalesRegisterFactory {

	public static SalesRegister getSalesRegister(List<Invoice> invoices, SalesRegisterFormat format) {

		if (format == SalesRegisterFormat.EXCEL)
			return new SalesRegisterExcel(invoices);

		if (format == SalesRegisterFormat.PDF)
			return new SalesRegisterPdf(invoices);

		return null;
	}

}
