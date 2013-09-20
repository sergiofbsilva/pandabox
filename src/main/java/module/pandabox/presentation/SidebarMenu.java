/*
 * @(#)SidebarMenu.java
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

import java.util.Iterator;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
/**
 * 
 * @author Pedro Santos
 * 
 */
public class SidebarMenu extends CssLayout {

    public SidebarMenu() {
        addStyleName("sidebar-menu");
    }

    public SidebarMenu addButton(NativeButton b) {
        addComponent(b);
        b.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                updateButtonStyles();
                event.getButton().addStyleName("selected");
            }
        });
        return this;
    }

    private void updateButtonStyles() {
        for (Iterator<Component> iterator = getComponentIterator(); iterator
                .hasNext();) {
            Component c = iterator.next();
            c.removeStyleName("selected");
        }
    }

    public void setSelected(NativeButton b) {
        updateButtonStyles();
        b.addStyleName("selected");
    }
}
