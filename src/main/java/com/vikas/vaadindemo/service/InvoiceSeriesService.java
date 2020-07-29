package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.entity.InvoiceSeries;
import com.vikas.vaadindemo.entity.Seller;
import com.vikas.vaadindemo.repository.InvoiceSeriesRepository;
import com.vikas.vaadindemo.util.InvoiceUtils;

@Service
public class InvoiceSeriesService {

	@Autowired
	private InvoiceSeriesRepository generatedInvoiceNumberRepository;

	public void save(InvoiceSeries generatedInvoiceNumber) {
		generatedInvoiceNumberRepository.save(generatedInvoiceNumber);
	}

	public void deleteById(int id) {
		generatedInvoiceNumberRepository.deleteById(id);
	}

	public InvoiceSeries getGeneratedInvoiceNumberById(int id) {
		Optional<InvoiceSeries> generatedInvoiceNumberOpt = generatedInvoiceNumberRepository.findById(id);
		return generatedInvoiceNumberOpt.isPresent() ? generatedInvoiceNumberOpt.get() : null;
	}

	public List<InvoiceSeries> getAllGeneratedInvoiceNumbers() {
		return (List<InvoiceSeries>) generatedInvoiceNumberRepository.findAll();
	}

	public InvoiceSeries getGeneratedInvoiceNumberBySequence(String sequence) {
		return generatedInvoiceNumberRepository.findBySequence(sequence);
	}

	public String generateInvoiceNumber(Invoice invoice) {
		Seller seller = invoice.getSeller();
		String sellerStateNumber = seller.getAddress().getState().getStateNumber();

		String sequence = sellerStateNumber + InvoiceUtils.invoiceTypeChar(invoice.getType());
		String invoiceNumber = sequence + invoiceNumberCounting(sequence);
		return invoiceNumber;
	}

	private String invoiceNumberCounting(String sequence) {
		
		InvoiceSeries generatedInvoiceNumber = getGeneratedInvoiceNumberBySequence(sequence);

		if (generatedInvoiceNumber == null) {
			// add new GeneratedInvoiceNumber in table
			generatedInvoiceNumber = new InvoiceSeries();
			generatedInvoiceNumber.setSequence(sequence);
			generatedInvoiceNumber.setCount(0);
		}

		generatedInvoiceNumber.setCount(generatedInvoiceNumber.getCount() + 1);

		save(generatedInvoiceNumber);

		String counting = "" + generatedInvoiceNumber.getCount();

		// add extra digits to make counting length equal to 7
		int loops = 7 - counting.length();
		for (int i = 0; i < loops; i++) {
			counting = "0" + counting;
		}

		return counting;
	}

}
