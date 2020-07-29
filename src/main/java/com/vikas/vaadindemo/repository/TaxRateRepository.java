package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.TaxRate;

@Repository
public interface TaxRateRepository extends CrudRepository<TaxRate, Integer>{

}
