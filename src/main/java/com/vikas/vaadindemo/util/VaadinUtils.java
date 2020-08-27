package com.vikas.vaadindemo.util;

import java.util.ArrayList;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.ListBox;

public class VaadinUtils {

	public static void setFormLabelWidth(FormLayout formLayout, String width) {
		formLayout.getElement().getChildren().forEach(item -> item.getStyle().set("--vaadin-form-item-label-width", width));
	}

	public static void enable(Component... components) {
		setEnable(true, components);
	}

	public static void disable(Component... components) {
		setEnable(false, components);
	}

	private static void setEnable(boolean enable, Component... components) {
		for (Component component : components) {
			((HasEnabled) component).setEnabled(enable);
		}
	}

	public static void clear(Component... components) {
		for (Component component : components) {

			// clear
			((HasValue<?, ?>) component).clear();

			// To clear ListBox (above clear method doesn't work on Listbox)
			if (component instanceof ListBox<?>)
				((ListBox<?>) component).setItems(new ArrayList<>());

		}
	}

	public static void setWidthFull(Component... components) {

		for (Component component : components) {
			if (component instanceof HasStyle) {
				((HasSize) component).setWidthFull();
			}
		}
	}

}
