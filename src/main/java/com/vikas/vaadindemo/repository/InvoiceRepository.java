package com.vikas.vaadindemo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.Invoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Integer>{

	@Query("SELECT i FROM Invoice i WHERE i.creationDate BETWEEN :startDate AND :endDate ")
	public List<Invoice> getInvoicesBetweenDate(LocalDateTime startDate, LocalDateTime endDate);
}
