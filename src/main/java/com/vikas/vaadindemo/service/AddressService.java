package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Address;
import com.vikas.vaadindemo.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;

	public void save(Address address) {
		addressRepository.save(address);
	}

	public void deleteById(int id) {
		addressRepository.deleteById(id);
	}

	public Address getAddressById(int id) {
		Optional<Address> addressOpt = addressRepository.findById(id);
		return addressOpt.isPresent() ? addressOpt.get() : null;
	}

	public List<Address> getAllAddresses() {
		return (List<Address>) addressRepository.findAll();
	}

}
