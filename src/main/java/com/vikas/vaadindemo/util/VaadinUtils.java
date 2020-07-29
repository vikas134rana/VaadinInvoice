package com.vikas.vaadindemo.util;

import com.vaadin.flow.component.formlayout.FormLayout;

public class VaadinUtils {

	public static void setFormLabelWidth(FormLayout formLayout, String width) {
		formLayout.getElement().getChildren().forEach(item -> item.getStyle().set("--vaadin-form-item-label-width", width));
	}

}
