package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.Buyer;

@Repository
public interface BuyerRepository extends CrudRepository<Buyer, Integer> {

}
