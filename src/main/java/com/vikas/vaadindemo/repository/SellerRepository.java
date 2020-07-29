package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.Seller;

@Repository
public interface SellerRepository extends CrudRepository<Seller, Integer> {

}
