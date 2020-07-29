package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.InvoiceItem;

@Repository
public interface InvoiceItemRepository extends CrudRepository<InvoiceItem, Integer>{

}
