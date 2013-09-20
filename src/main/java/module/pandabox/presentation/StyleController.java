/*
 * @(#)StyleController.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Pedro Santos
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Pandabox Module.
 *
 *   The Pandabox Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Pandabox Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Pandabox Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.pandabox.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class StyleController extends VerticalLayout {
    public StyleController(final Component component, String... styles) {
	super();
	setCaption("Style");
	setSpacing(true);

	OptionGroup options = new OptionGroup();
	addComponent(options);
	options.setImmediate(true);
	options.setMultiSelect(true);
	for (String style : styles) {
	    options.addItem(style);
	}
	options.addListener(new Property.ValueChangeListener() {
	    @Override
	    public void valueChange(Property.ValueChangeEvent event) {
		Set<String> value = (Set<String>) event.getProperty().getValue();
		List<String> current = Arrays.asList(component.getStyleName().split(" "));
		for (String style : value) {
		    if (!current.contains(style)) {
			component.addStyleName(style);
		    }
		}
		for (String style : current) {
		    if (!value.contains(style)) {
			component.removeStyleName(style);
		    }
		}
	    }
	});

	HorizontalLayout custom = new HorizontalLayout();
	addComponent(custom);
	custom.setSpacing(true);
	final TextField txtStyle = new TextField();
	custom.addComponent(txtStyle);
	Button btApplyStyle = new Button("Apply Style", new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		final String value = (String) txtStyle.getValue();
		if (value != null) {
		    component.addStyleName(value);
		}
	    }
	});
	custom.addComponent(btApplyStyle);
    }
}
