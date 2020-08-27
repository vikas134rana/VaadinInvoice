package com.vikas.vaadindemo.ui.invoicepricemaintainance;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Seller;

@UIScope
@SpringComponent
public class InvoicePriceMaintainanceView extends VerticalLayout implements InvoicePriceMaintainancePresenter.Display {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CategoryComponent categoryComponent;

	@Autowired
	private ItemComponent itemComponent;

	private ComboBox<Seller> sellerEle;
	private HorizontalLayout categoryAndItemLayout;

	@PostConstruct
	public void init() {

		sellerEle = new ComboBox<>();
		sellerEle.setItemLabelGenerator(s -> s.getAddress().getName());
		sellerEle.setLabel("Seller");

		categoryAndItemLayout = new HorizontalLayout();
		categoryAndItemLayout.setWidthFull();
		categoryAndItemLayout.setHeightFull();
//		categoryAndItemLayout.getStyle().set("border", "1px solid black");
		categoryAndItemLayout.add(categoryComponent);
		categoryAndItemLayout.add(itemComponent);

		add(sellerEle);
		add(categoryAndItemLayout);
	}

	@Override
	public ComboBox<Seller> getSellerEle() {
		return sellerEle;
	}

	@Override
	public CategoryComponent getCategoryComponent() {
		return categoryComponent;
	}

	@Override
	public ItemComponent getItemComponent() {
		return itemComponent;
	}

}
