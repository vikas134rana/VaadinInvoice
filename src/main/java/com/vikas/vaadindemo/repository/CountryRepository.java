package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer>{
	
}
