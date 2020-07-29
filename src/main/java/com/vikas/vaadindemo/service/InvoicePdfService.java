package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.InvoicePdf;
import com.vikas.vaadindemo.repository.InvoicePdfRepository;

@Service
public class InvoicePdfService {

	@Autowired
	private InvoicePdfRepository invoicePdfRepository;

	public void save(InvoicePdf invoicePdf) {
		invoicePdfRepository.save(invoicePdf);
	}

	public void deleteById(int id) {
		invoicePdfRepository.deleteById(id);
	}

	public InvoicePdf getInvoicePdfById(int id) {
		Optional<InvoicePdf> invoicePdfOpt = invoicePdfRepository.findById(id);
		return invoicePdfOpt.isPresent() ? invoicePdfOpt.get() : null;
	}

	public List<InvoicePdf> getAllInvoicePdfes() {
		return (List<InvoicePdf>) invoicePdfRepository.findAll();
	}

}
