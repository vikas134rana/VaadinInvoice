package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.InvoicePdf;

@Repository
public interface InvoicePdfRepository extends CrudRepository<InvoicePdf, Integer>{

}
