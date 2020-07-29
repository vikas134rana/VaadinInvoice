package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Seller;
import com.vikas.vaadindemo.repository.SellerRepository;

@Service
public class SellerService {

	@Autowired
	private SellerRepository sellerRepository;

	public void save(Seller seller) {
		sellerRepository.save(seller);
	}

	public void deleteById(int id) {
		sellerRepository.deleteById(id);
	}
	
	public Seller getSellerById(int id) {
		Optional<Seller> sellerOpt = sellerRepository.findById(id);
		return sellerOpt.isPresent() ? sellerOpt.get() : null;
	}

	public List<Seller> getAllSellers() {
		return (List<Seller>) sellerRepository.findAll();
	}

}
