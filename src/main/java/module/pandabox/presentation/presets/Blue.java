/*
 * @(#)Blue.java
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
package module.pandabox.presentation.presets;

import java.awt.Color;
import java.io.Serializable;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class Blue implements StylePreset, Serializable {

    private static final long serialVersionUID = -6710443221545734020L;

    private Color backgroundColor = Color.decode("0xf9f9f9");
    private String fontSize = "13px";
    private Color fontColor = Color.decode("0x222222");
    private Color baseColor = Color.decode("0xc9c9c9");
    private Color alternateColor = Color.decode("0x287ece");

    public String getPresetName() {
        return "Blue";
    }

    public Color getAlternateColor() {
        return alternateColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public Color getBaseFontColor() {
        return fontColor;
    }

    public String getBaseFontSize() {
        return fontSize;
    }

}
