package com.vikas.vaadindemo.ui.invoicepricemaintainance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Category;
import com.vikas.vaadindemo.entity.Item;
import com.vikas.vaadindemo.entity.ItemPrice;
import com.vikas.vaadindemo.entity.Seller;
import com.vikas.vaadindemo.entity.TaxRate;
import com.vikas.vaadindemo.service.CategoryService;
import com.vikas.vaadindemo.service.ItemPriceService;
import com.vikas.vaadindemo.service.ItemService;
import com.vikas.vaadindemo.service.SellerService;
import com.vikas.vaadindemo.service.TaxRateService;
import com.vikas.vaadindemo.ui.common.InvoiceDialog;
import com.vikas.vaadindemo.util.Util;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class InvoicePriceMaintainancePresenter {

	interface Display {
		ComboBox<Seller> getSellerEle();

		CategoryComponent getCategoryComponent();

		ItemComponent getItemComponent();
	}

	interface CategoryDisplay {

		TextField getCategoryNameEle();

		ListBox<Category> getCategoryListboxEle();

		NumberField getHsnEle();

		NumberField getCgstRateEle();

		NumberField getSgstRateEle();

		Button getSaveEle();
	}

	interface ItemDisplay {

		TextField getItemNameEle();

		ListBox<ItemPrice> getItemListboxEle();

		Select<String> getUnitEle();

		NumberField getPriceEle();

		TextArea getPriceHistoryEle();

		Button getSaveEle();
	}

	@Autowired
	InvoicePriceMaintainanceView invoicePriceMaintainanceView;

	@Autowired
	private SellerService sellerService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TaxRateService taxRateService;
	@Autowired
	private ItemPriceService itemPriceService;
	@Autowired
	private ItemService itemService;

	private ComboBox<Seller> sellerEle;
	private CategoryComponent categoryComponent;
	private ItemComponent itemComponent;

	// Category
	private TextField categoryNameEle;
	private ListBox<Category> categoryListboxEle;
	private NumberField hsnEle;
	private NumberField cgstRateEle;
	private NumberField sgstRateEle;
	private Button categorySaveEle;

	// Item
	private TextField itemNameEle;
	private ListBox<ItemPrice> itemListboxEle;
	private Select<String> unitEle;
	private NumberField priceEle;
	private TextArea priceHistoryEle;
	private Button itemSaveEle;

	@PostConstruct
	public void init() {

		sellerEle = invoicePriceMaintainanceView.getSellerEle();
		categoryComponent = invoicePriceMaintainanceView.getCategoryComponent();
		itemComponent = invoicePriceMaintainanceView.getItemComponent();

		// Category
		categoryNameEle = categoryComponent.getCategoryNameEle();
		categoryListboxEle = categoryComponent.getCategoryListboxEle();
		hsnEle = categoryComponent.getHsnEle();
		cgstRateEle = categoryComponent.getCgstRateEle();
		sgstRateEle = categoryComponent.getSgstRateEle();
		categorySaveEle = categoryComponent.getSaveEle();

		// Item
		itemNameEle = itemComponent.getItemNameEle();
		itemListboxEle = itemComponent.getItemListboxEle();
		unitEle = itemComponent.getUnitEle();
		priceEle = itemComponent.getPriceEle();
		priceHistoryEle = itemComponent.getPriceHistoryEle();
		itemSaveEle = itemComponent.getSaveEle();

		actionListener();

	}

	public void actionListener() {

		// =============== Seller ===============
		List<Seller> sellers = sellerService.getAllSellers();
		sellerEle.setItems(sellers);

		sellerEle.addValueChangeListener(listener -> {
			Seller selectedSeller = listener.getValue();
			if (selectedSeller != null) {
				VaadinUtils.enable(categoryNameEle, categoryListboxEle);
				List<Category> categories = categoryService.getAllCategories();
				categoryListboxEle.setItems(categories);
			} else {
				VaadinUtils.clear(categoryNameEle, categoryListboxEle);
				VaadinUtils.disable(categoryNameEle, categoryListboxEle);
			}
		});

		// =============== CategoryName ===============
		categoryNameEle.addValueChangeListener(listener -> {

			if (!listener.isFromClient())
				return;

			String categorySearch = listener.getValue();
			List<Category> categories = categoryService.getAllCategories();
			List<Category> filteredCategories = categories.stream().filter(c -> c.getName().toLowerCase().contains(categorySearch.toLowerCase()))
					.collect(Collectors.toList());
			categoryListboxEle.setItems(filteredCategories);

			// No existing Category Matches
			if (filteredCategories.isEmpty()) {
				VaadinUtils.enable(hsnEle, cgstRateEle, sgstRateEle);
			} else {
				VaadinUtils.clear(hsnEle, cgstRateEle, sgstRateEle);
				VaadinUtils.disable(hsnEle, cgstRateEle, sgstRateEle);
			}
		});

		// =============== CategoryListbox ===============
		categoryListboxEle.addValueChangeListener(listener -> {

			Seller selectedSeller = sellerEle.getValue();
			Category selectedCategory = listener.getValue();

			// Category Selected
			if (selectedCategory != null) {

				TaxRate taxRate = taxRateService.getTaxRate(selectedCategory);
				VaadinUtils.enable(hsnEle, cgstRateEle, sgstRateEle, itemNameEle, itemListboxEle);
				VaadinUtils.disable(categorySaveEle);

				categoryNameEle.setValue(selectedCategory.getName());
				hsnEle.setValue(Double.parseDouble(selectedCategory.getHsnCode()));
				cgstRateEle.setValue(taxRate.getCgstRate());
				sgstRateEle.setValue(taxRate.getSgstRate());

				List<ItemPrice> itemPrices = itemPriceService.getItemPrices(selectedCategory, selectedSeller);
				itemListboxEle.setItems(itemPrices);

			}
			// No Category Selected
			else {
				VaadinUtils.clear(hsnEle, cgstRateEle, sgstRateEle, itemNameEle, itemListboxEle);
				VaadinUtils.disable(hsnEle, cgstRateEle, sgstRateEle, categorySaveEle, itemNameEle, itemListboxEle);

			}
		});

		// =============== HSN/CGST/SGST Update ===============
		hsnEle.addValueChangeListener(listener -> {
			if (!listener.isFromClient())
				return;
			enableCategorySaveIfApplicable();
		});

		cgstRateEle.addValueChangeListener(listener -> {
			if (!listener.isFromClient())
				return;
			enableCategorySaveIfApplicable();
		});

		sgstRateEle.addValueChangeListener(listener -> {
			if (!listener.isFromClient())
				return;
			enableCategorySaveIfApplicable();
		});

		// =============== Category Save ===============
		categorySaveEle.addClickListener(listener -> saveCategory());

		// =============== ItemName ===============
		itemNameEle.addValueChangeListener(listener -> {

			if (!listener.isFromClient())
				return;

			Seller selectedSeller = sellerEle.getValue();
			Category selectedCategory = categoryListboxEle.getValue();
			List<ItemPrice> itemPrices = itemPriceService.getItemPrices(selectedCategory, selectedSeller);

			String itemSearch = listener.getValue();
			List<ItemPrice> filteredItems = itemPrices.stream().filter(ip -> ip.getItem().getName().toLowerCase().contains(itemSearch.toLowerCase()))
					.collect(Collectors.toList());
			itemListboxEle.setItems(filteredItems);

			// No existing Item Matches
			if (filteredItems.isEmpty()) {
				VaadinUtils.enable(unitEle, priceEle);
			} else {
				VaadinUtils.clear(unitEle, priceEle);
				VaadinUtils.disable(unitEle, priceEle);
			}

		});

		// =============== ItemListbox ===============
		itemListboxEle.addValueChangeListener(listener -> {

			ItemPrice selectedItemPrice = listener.getValue();

			if (selectedItemPrice != null) {

				VaadinUtils.enable(unitEle, priceEle);
				VaadinUtils.disable(itemSaveEle);

				itemNameEle.setValue(selectedItemPrice.getItem().getName());
				unitEle.setValue(selectedItemPrice.getItem().getUnit());
				priceEle.setValue(selectedItemPrice.getPrice());

			} else {
				VaadinUtils.clear(itemNameEle, unitEle, priceEle);
				VaadinUtils.disable(unitEle, priceEle, itemSaveEle);

			}
		});

		// =============== Item Unit/Price Update ===============
		unitEle.addValueChangeListener(listener -> {
			if (!listener.isFromClient())
				return;
			enableItemSaveIfApplicable();
		});

		priceEle.addValueChangeListener(listener -> {
			if (!listener.isFromClient())
				return;
			enableItemSaveIfApplicable();
		});

		// =============== Item Save ===============
		itemSaveEle.addClickListener(listener -> saveItem());

	}

	private void enableCategorySaveIfApplicable() {
		if (!hsnEle.isEmpty() && !cgstRateEle.isEmpty() && !sgstRateEle.isEmpty()) {
			categorySaveEle.setEnabled(true);
		} else {
			categorySaveEle.setEnabled(false);
		}
	}

	private void enableItemSaveIfApplicable() {
		if (!unitEle.isEmpty() && !priceEle.isEmpty()) {
			itemSaveEle.setEnabled(true);
		} else {
			itemSaveEle.setEnabled(false);
		}
	}

	private void saveCategory() {
		String categoryName = categoryNameEle.getValue();
		String hsn = hsnEle.getValue().intValue() + "";
		double cgstRate = Util.roundToTwoDigit(cgstRateEle.getValue());
		double sgstRate = Util.roundToTwoDigit(sgstRateEle.getValue());

		Category selectedCategory = categoryListboxEle.getValue();
		Category category;
		System.out.println(categoryName + " " + hsn + " " + cgstRate + " " + sgstRate);

		// Update
		if (selectedCategory != null) {
			System.out.println("=========== Updating Category ===========");

			category = categoryService.getCategoryById(selectedCategory.getId());
			category.setHsnCode(hsn);

			TaxRate taxRate = taxRateService.getTaxRate(category);
			taxRate.setCgstRate(cgstRate);
			taxRate.setSgstRate(sgstRate);
			taxRate.setStartDate(LocalDateTime.now());

			category.setTaxRate(taxRate);
		}
		// Create
		else {
			System.out.println("=========== Creating Category ===========");

			category = new Category();
			category.setName(categoryName);
			category.setHsnCode(hsn);

			TaxRate taxRate = new TaxRate();
			taxRate.setCategory(category);
			taxRate.setCgstRate(cgstRate);
			taxRate.setSgstRate(sgstRate);
			taxRate.setStartDate(LocalDateTime.now());

			category.setTaxRate(taxRate);
		}

		categoryService.save(category); // update/create Category
		invoicePriceMaintainanceView.add(new InvoiceDialog("Successfull", "Category saved - " + category.getName()));
	}

	private void saveItem() {
		String itemName = itemNameEle.getValue();
		String unit = unitEle.getValue();
		double price = Util.roundToTwoDigit(priceEle.getValue());

		Seller selectedSeller = sellerEle.getValue();
		Category selectedCategory = categoryListboxEle.getValue();
		ItemPrice selectedItemPrice = itemListboxEle.getValue();

		Item item;
		// Update
		if (selectedItemPrice != null) {

			System.out.println("=========== Updating Item ===========");

			item = selectedItemPrice.getItem();
			item.setUnit(unit);

			selectedItemPrice.setSeller(selectedSeller);
			selectedItemPrice.setPrice(price);
			selectedItemPrice.setStart_date(LocalDateTime.now());

			item.setItemPrices(new ArrayList<>(Arrays.asList(selectedItemPrice)));
		}
		// Create
		else {

			System.out.println("=========== Creating Item ===========");

			item = new Item();
			item.setName(itemName);
			item.setUnit(unit);
			item.setCategory(selectedCategory);

			ItemPrice itemPrice = new ItemPrice();
			itemPrice.setItem(item);
			itemPrice.setPrice(price);
			itemPrice.setSeller(selectedSeller);
			itemPrice.setStart_date(LocalDateTime.now());

			item.setItemPrices(new ArrayList<>(Arrays.asList(itemPrice)));
		}

		itemService.save(item); // update/create Item

		invoicePriceMaintainanceView.add(new InvoiceDialog("Successfull", "Item saved - " + item.getName()));
	}

}
