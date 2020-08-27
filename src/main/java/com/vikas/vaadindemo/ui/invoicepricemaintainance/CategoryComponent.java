package com.vikas.vaadindemo.ui.invoicepricemaintainance;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vikas.vaadindemo.entity.Category;
import com.vikas.vaadindemo.service.CategoryService;
import com.vikas.vaadindemo.util.VaadinUtils;

@UIScope
@SpringComponent
public class CategoryComponent extends VerticalLayout implements InvoicePriceMaintainancePresenter.CategoryDisplay {

	private static final long serialVersionUID = 1L;

	@Autowired
	CategoryService categoryService;

	private TextField categoryNameEle;
	private ListBox<Category> categoryListboxEle;
	private NumberField hsnEle;
	private NumberField cgstRateEle;
	private NumberField sgstRateEle;
	private Button saveEle;

	private Label categoryText;
	private HorizontalLayout horizontalLayout;
	private VerticalLayout verticalLayout1;
	private VerticalLayout verticalLayout2;
	private Div categoryListboxDiv;
	private HorizontalLayout saveContainerLayout;

	@PostConstruct
	public void init() {

		categoryText = new Label("Category");
		categoryText.setWidthFull();
		categoryText.getStyle().set("border", "1px solid black");
		categoryText.getStyle().set("background", "gray");
		categoryText.getStyle().set("color", "white");
		categoryText.getStyle().set("font-size", "18px");
		categoryText.getStyle().set("font-weight", "bold");
		categoryText.getStyle().set("text-align", "center");

		categoryNameEle = new TextField();
		categoryNameEle.setValueChangeMode(ValueChangeMode.EAGER);
		categoryNameEle.setWidthFull();
		categoryNameEle.setPlaceholder("Category Name");

		categoryListboxEle = new ListBox<>();
		categoryListboxEle.setHeight("350px");
		categoryListboxEle.setWidthFull();
		categoryListboxEle.setRenderer(new ComponentRenderer<>(category -> {
			Div div = new Div();
			div.setText(category.getName());
			return div;
		}));
		categoryListboxDiv = new Div(categoryListboxEle);
		categoryListboxDiv.getStyle().set("border", "1px solid black");
		categoryListboxDiv.setWidthFull();
		verticalLayout1 = new VerticalLayout();
		verticalLayout1.add(categoryNameEle, categoryListboxDiv);
//		verticalLayout1.getStyle().set("border", "1px solid black");

		hsnEle = new NumberField("HSN/SAP");
		cgstRateEle = new NumberField("CGST Rate");
		sgstRateEle = new NumberField("SGST Rate");
		saveEle = new Button("Save");

		saveContainerLayout = new HorizontalLayout(saveEle);
		saveContainerLayout.setHeightFull();
		saveContainerLayout.setVerticalComponentAlignment(Alignment.END, saveEle);

		verticalLayout2 = new VerticalLayout();
		verticalLayout2.add(hsnEle, cgstRateEle, sgstRateEle, saveContainerLayout);
//		verticalLayout2.getStyle().set("border", "1px solid black");
		verticalLayout2.setHorizontalComponentAlignment(Alignment.CENTER, saveContainerLayout);

		horizontalLayout = new HorizontalLayout(verticalLayout1, verticalLayout2);
		horizontalLayout.setWidthFull();

		add(categoryText, horizontalLayout);
		getStyle().set("border", "1px solid black");
		setWidthFull();
		setAlignItems(Alignment.CENTER);
		setPadding(false);
		setMargin(false);
		setSpacing(false);

		VaadinUtils.disable(categoryNameEle, categoryListboxEle, hsnEle, cgstRateEle, sgstRateEle, saveEle);

	}

	@Override
	public TextField getCategoryNameEle() {
		return categoryNameEle;
	}

	@Override
	public ListBox<Category> getCategoryListboxEle() {
		return categoryListboxEle;
	}

	@Override
	public NumberField getHsnEle() {
		return hsnEle;
	}

	@Override
	public NumberField getCgstRateEle() {
		return cgstRateEle;
	}

	@Override
	public NumberField getSgstRateEle() {
		return sgstRateEle;
	}

	@Override
	public Button getSaveEle() {
		return saveEle;
	}
}
