/*
 * @(#)SpacingController.java
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

import com.vaadin.data.Property;
import com.vaadin.ui.Layout.SpacingHandler;
import com.vaadin.ui.OptionGroup;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class SpacingController extends OptionGroup {
    public SpacingController(final SpacingHandler layout) {
	super("Spacing");

	setImmediate(true);
	addStyleName("horizontal");
	addItem("yes");
	addItem("no");
	addListener(new Property.ValueChangeListener() {
	    @Override
	    public void valueChange(Property.ValueChangeEvent event) {
		String value = (String) event.getProperty().getValue();
		layout.setSpacing("yes".equals(value));
	    }
	});
	select("yes");
    }
}
