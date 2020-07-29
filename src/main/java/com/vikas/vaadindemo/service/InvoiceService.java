package com.vikas.vaadindemo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.vaadindemo.entity.Invoice;
import com.vikas.vaadindemo.repository.InvoiceRepository;

@Service
@Transactional
public class InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;

	public Invoice save(Invoice invoice) {
		return invoiceRepository.save(invoice);
	}

	public void deleteById(int id) {
		invoiceRepository.deleteById(id);
	}

	public Invoice getInvoiceById(int id) {
		Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
		return invoiceOpt.isPresent() ? invoiceOpt.get() : null;
	}

	public List<Invoice> getInvoicesById(List<Integer> ids) {
		List<Invoice> invoices = (List<Invoice>) invoiceRepository.findAllById(ids);
		return invoices;
	}

	public List<Invoice> getAllInvoices() {
		return (List<Invoice>) invoiceRepository.findAll();
	}

	public List<Invoice> getInvoicesBetweenDate(LocalDate startDate, LocalDate endDate) {
		return (List<Invoice>) invoiceRepository.getInvoicesBetweenDate(startDate.atStartOfDay(), endDate.atTime(23, 59, 59, 999999999));
	}

	public List<Invoice> searchInvoice(String invoiceNumber, Integer sellerId, Integer buyerId, Integer invoiceType, LocalDate startDate, LocalDate endDate) {

		if (startDate == null || endDate == null)
			throw new IllegalArgumentException("Start Date and End Date is mandatory.");

		List<Invoice> invoices = getInvoicesBetweenDate(startDate, endDate);
		System.out.println("getInvoicesBetweenDate() Size: " + invoices.size());

		invoices = invoices.stream().filter(i -> {

			// ignore invoice where creation date is not in start and end date range
			/*- LocalDate invoiceCreationDate = i.getCreationDate().toLocalDate(); 
			if (!Util.isDateInRange(invoiceCreationDate, startDate, endDate))
				return false; */

			// ignore invoice where sellerId doesn't match
			if (sellerId != null && sellerId != 0 && i.getSeller().getId() != sellerId)
				return false;

			// ignore invoice where invoice type doesn't match
			if (invoiceType != null && invoiceType != 0 && invoiceType != i.getType())
				return false;

			// ignore invoice where buyerId doesn't match
			if (buyerId != null && buyerId != 0 && i.getBuyer().getId() != buyerId)
				return false;

			// ignore invoice where invoiceNumber doesn't match
			if (invoiceNumber != null && !invoiceNumber.trim().isEmpty() && !i.getInvoiceNumber().equals(invoiceNumber))
				return false;

			return true;
		}).collect(Collectors.toList());

		return invoices;
	}

}
