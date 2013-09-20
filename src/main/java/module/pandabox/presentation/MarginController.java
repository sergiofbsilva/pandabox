/*
 * @(#)MarginController.java
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

import java.util.Set;

import com.vaadin.data.Property;
import com.vaadin.ui.Layout.MarginHandler;
import com.vaadin.ui.Layout.MarginInfo;
import com.vaadin.ui.OptionGroup;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class MarginController extends OptionGroup {
    final String MARGIN_LEFT = "LEFT";
    final String MARGIN_TOP = "TOP";
    final String MARGIN_RIGHT = "RIGHT";
    final String MARGIN_BOTTOM = "BOTTOM";

    public MarginController(final MarginHandler layout) {
	super("Margins");

	setImmediate(true);
	setMultiSelect(true);
	addStyleName("horizontal");
	addItem(MARGIN_TOP);
	addItem(MARGIN_RIGHT);
	addItem(MARGIN_BOTTOM);
	addItem(MARGIN_LEFT);
	select(MARGIN_TOP);
	select(MARGIN_RIGHT);
	select(MARGIN_BOTTOM);
	select(MARGIN_LEFT);
	addListener(new Property.ValueChangeListener() {
	    @Override
	    public void valueChange(Property.ValueChangeEvent event) {
		Set<String> value = (Set<String>) event.getProperty().getValue();
		boolean topEnabled = value.contains(MARGIN_TOP);
		boolean bottomEnabled = value.contains(MARGIN_BOTTOM);
		boolean rightEnabled = value.contains(MARGIN_RIGHT);
		boolean leftEnabled = value.contains(MARGIN_LEFT);
		layout.setMargin(new MarginInfo(topEnabled, rightEnabled, bottomEnabled, leftEnabled));
	    }
	});
	layout.setMargin(new MarginInfo(true));
    }
}
