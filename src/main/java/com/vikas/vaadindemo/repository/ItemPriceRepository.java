package com.vikas.vaadindemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.ItemPrice;
import com.vikas.vaadindemo.entity.Seller;

@Repository
public interface ItemPriceRepository extends CrudRepository<ItemPrice, Integer> {

	@Query("SELECT ip FROM ItemPrice ip WHERE ip.seller = :seller")
	List<ItemPrice> getItemPrices(Seller seller);
	
}
