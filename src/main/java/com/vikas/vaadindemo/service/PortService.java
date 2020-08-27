package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Port;
import com.vikas.vaadindemo.repository.PortRepository;

@Service
public class PortService {

	@Autowired
	private PortRepository portRepository;

	public void save(Port port) {
		portRepository.save(port);
	}

	public void deleteById(int id) {
		portRepository.deleteById(id);
	}

	public Port getPortById(int id) {
		Optional<Port> portOpt = portRepository.findById(id);
		return portOpt.isPresent() ? portOpt.get() : null;
	}

	public List<Port> getAllPorts() {
		return (List<Port>) portRepository.findAll();
	}

}
