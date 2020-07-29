package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Country;
import com.vikas.vaadindemo.repository.CountryRepository;

@Service
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;

	public void save(Country country) {
		countryRepository.save(country);
	}

	public void deleteById(int id) {
		countryRepository.deleteById(id);
	}

	public Country getCountryById(int id) {
		Optional<Country> countryOpt = countryRepository.findById(id);
		return countryOpt.isPresent() ? countryOpt.get() : null;
	}

	public List<Country> getAllCountries() {
		return (List<Country>) countryRepository.findAll();
	}

}
