package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Item;
import com.vikas.vaadindemo.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public void save(Item item) {
		itemRepository.save(item);
	}

	public void deleteById(int id) {
		itemRepository.deleteById(id);
	}

	public Item getItemById(int id) {
		Optional<Item> itemOpt = itemRepository.findById(id);
		return itemOpt.isPresent() ? itemOpt.get() : null;
	}

	public List<Item> getItemsById(List<Integer> ids) {
		return (List<Item>) itemRepository.findAllById(ids);
	}

	public List<Item> getAllItems() {
		return (List<Item>) itemRepository.findAll();
	}

}
