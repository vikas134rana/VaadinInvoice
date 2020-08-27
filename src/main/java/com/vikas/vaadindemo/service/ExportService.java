package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Export;
import com.vikas.vaadindemo.repository.ExportRepository;

@Service
public class ExportService {

	@Autowired
	private ExportRepository exportRepository;

	public void save(Export export) {
		exportRepository.save(export);
	}

	public void deleteById(int id) {
		exportRepository.deleteById(id);
	}

	public Export getExportById(int id) {
		Optional<Export> exportOpt = exportRepository.findById(id);
		return exportOpt.isPresent() ? exportOpt.get() : null;
	}

	public List<Export> getAllExportes() {
		return (List<Export>) exportRepository.findAll();
	}

}
