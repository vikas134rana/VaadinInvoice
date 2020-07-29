package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.State;
import com.vikas.vaadindemo.repository.StateRepository;

@Service
public class StateService {

	@Autowired
	private StateRepository stateRepository;

	public void save(State state) {
		stateRepository.save(state);
	}

	public void deleteById(int id) {
		stateRepository.deleteById(id);
	}

	public State getStateById(int id) {
		Optional<State> stateOpt = stateRepository.findById(id);
		return stateOpt.isPresent() ? stateOpt.get() : null;
	}

	public List<State> getAllStates() {
		return (List<State>) stateRepository.findAll();
	}

}
