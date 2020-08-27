package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.Export;

@Repository
public interface ExportRepository extends CrudRepository<Export, Integer> {

}
