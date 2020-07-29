package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Buyer;
import com.vikas.vaadindemo.repository.BuyerRepository;

@Service
public class BuyerService {

	@Autowired
	private BuyerRepository buyerRepository;

	public void save(Buyer buyer) {
		buyerRepository.save(buyer);
	}

	public void deleteById(int id) {
		buyerRepository.deleteById(id);
	}

	public Buyer getBuyerById(int id) {
		Optional<Buyer> buyerOpt = buyerRepository.findById(id);
		return buyerOpt.isPresent() ? buyerOpt.get() : null;
	}

	public List<Buyer> getAllBuyers() {
		return (List<Buyer>) buyerRepository.findAll();
	}

}
