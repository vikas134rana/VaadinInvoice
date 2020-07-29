package com.vikas.vaadindemo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.vaadindemo.entity.Category;
import com.vikas.vaadindemo.entity.Item;
import com.vikas.vaadindemo.entity.ItemPrice;
import com.vikas.vaadindemo.entity.Seller;
import com.vikas.vaadindemo.repository.ItemPriceRepository;

@Service
public class ItemPriceService {

	@Autowired
	private ItemPriceRepository itemPriceRepository;

	public void save(ItemPrice itemPrice) {
		itemPriceRepository.save(itemPrice);
	}

	public void deleteById(int id) {
		itemPriceRepository.deleteById(id);
	}

	public ItemPrice getItemPriceById(int id) {
		Optional<ItemPrice> itemPriceOpt = itemPriceRepository.findById(id);
		return itemPriceOpt.isPresent() ? itemPriceOpt.get() : null;
	}

	public List<ItemPrice> getItemPricesById(List<Integer> ids) {
		return (List<ItemPrice>) itemPriceRepository.findAllById(ids);
	}

	public List<ItemPrice> getAllItemPrices() {
		return (List<ItemPrice>) itemPriceRepository.findAll();
	}

	public ItemPrice getItemPrice(Item item, Seller seller) {
		ItemPrice itemPrice = item.getItemPrices().stream().filter(ip -> ip.getSeller().getId() == seller.getId()).findFirst().orElse(null);
		return itemPrice;
	}

	public List<ItemPrice> getItemPrices(Seller seller) {
		return itemPriceRepository.getItemPrices(seller);
	}

	public List<ItemPrice> getItemPrices(Category category, Seller seller) {
		List<ItemPrice> itemPrices = getItemPrices(seller).stream().filter(s -> s.getItem().getCategory().getId() == category.getId())
				.collect(Collectors.toList());
		return itemPrices;
	}
}
