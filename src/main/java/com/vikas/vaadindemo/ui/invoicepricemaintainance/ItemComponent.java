package com.vikas.vaadindemo.ui.invoicepricemaintainance;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.ItemPrice;
import com.vikas.vaadindemo.service.ItemService;
import com.vikas.vaadindemo.util.InvoiceUtils;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class ItemComponent extends VerticalLayout implements InvoicePriceMaintainancePresenter.ItemDisplay {

	private static final long serialVersionUID = 1L;

	@Autowired
	ItemService itemService;

	private TextField itemNameEle;
	private ListBox<ItemPrice> itemListboxEle;
	private Select<String> unitEle;
	private NumberField priceEle;
	private TextArea priceHistoryEle;
	private Button saveEle;

	private Label itemTextEle;
	private HorizontalLayout horizontalLayout;
	private VerticalLayout verticalLayout1;
	private VerticalLayout verticalLayout2;
	private Div itemListboxDivEle;
	private HorizontalLayout saveContainerLayout;

	@PostConstruct
	public void init() {

		itemTextEle = new Label("Item");
		itemTextEle.setWidthFull();
		itemTextEle.getStyle().set("border", "1px solid black");
		itemTextEle.getStyle().set("background", "gray");
		itemTextEle.getStyle().set("color", "white");
		itemTextEle.getStyle().set("font-size", "18px");
		itemTextEle.getStyle().set("font-weight", "bold");
		itemTextEle.getStyle().set("text-align", "center");

		itemNameEle = new TextField();
		itemNameEle.setValueChangeMode(ValueChangeMode.EAGER);
		itemNameEle.setPlaceholder("Item Name");
		itemNameEle.setWidthFull();

		itemListboxEle = new ListBox<>();
		itemListboxEle.setRenderer(new ComponentRenderer<>(item -> {
			Div div = new Div();
			div.setText(item.getItem().getName());
			return div;
		}));
		itemListboxEle.setWidthFull();
		itemListboxEle.setHeight("350px");

		itemListboxDivEle = new Div(itemListboxEle);
		itemListboxDivEle.setWidthFull();
		itemListboxDivEle.getStyle().set("border", "1px solid black");

		verticalLayout1 = new VerticalLayout();
		verticalLayout1.add(itemNameEle, itemListboxDivEle);
//		verticalLayout1.getStyle().set("border", "1px solid black");

		unitEle = new Select<>();
		unitEle.setLabel("Unit");
		unitEle.setItems(InvoiceUtils.getItemUnits());
		priceEle = new NumberField("Price");
		priceHistoryEle = new TextArea("Price History");
		priceHistoryEle.setHeightFull();
		saveEle = new Button("Save");

		saveContainerLayout = new HorizontalLayout(saveEle);
		saveContainerLayout.setHeightFull();
		saveContainerLayout.setVerticalComponentAlignment(Alignment.END, saveEle);

		verticalLayout2 = new VerticalLayout();
		verticalLayout2.add(unitEle, priceEle, priceHistoryEle, saveContainerLayout);
//		verticalLayout2.getStyle().set("border", "1px solid black");
		verticalLayout2.setHorizontalComponentAlignment(Alignment.CENTER, saveContainerLayout);

		horizontalLayout = new HorizontalLayout(verticalLayout1, verticalLayout2);
		horizontalLayout.setWidthFull();

		add(itemTextEle, horizontalLayout);
		getStyle().set("border", "1px solid black");
		setWidthFull();
		setAlignItems(Alignment.CENTER);
		setPadding(false);
		setMargin(false);
		setSpacing(false);

		VaadinUtils.disable(itemNameEle, itemListboxEle, unitEle, priceEle, saveEle);

	}

	@Override
	public TextField getItemNameEle() {
		return itemNameEle;
	}

	@Override
	public ListBox<ItemPrice> getItemListboxEle() {
		return itemListboxEle;
	}

	@Override
	public Select<String> getUnitEle() {
		return unitEle;
	}

	@Override
	public NumberField getPriceEle() {
		return priceEle;
	}

	@Override
	public TextArea getPriceHistoryEle() {
		return priceHistoryEle;
	}

	@Override
	public Button getSaveEle() {
		return saveEle;
	}
}
