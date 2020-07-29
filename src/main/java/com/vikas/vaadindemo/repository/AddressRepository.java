package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer>{

}
