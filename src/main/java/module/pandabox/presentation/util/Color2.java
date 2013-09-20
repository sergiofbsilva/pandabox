/*
 * @(#)Color2.java
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
package module.pandabox.presentation.util;

import java.awt.Color;

/**
 * 
 * @author Pedro Santos
 * 
 */
class Color2 extends Color {

    public Color2(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color
                .getAlpha());
    }

    @Override
    public Color2 brighter() {
        float[] hsb = Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);

        // Lower saturation
        hsb[1] = (float) (hsb[1] - hsb[1] * 0.7);
        // Raise brightness
        hsb[2] = (float) (hsb[2] + (1 - hsb[2]) * 0.4);

        return new Color2(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    }

    @Override
    public Color2 darker() {
        float[] hsb = Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);

        // Lower saturation, if not below threshold
        if (hsb[1] > 0.2) {
            hsb[1] = (float) (hsb[1] - hsb[1] * 0.1);
        }
        // Lower brightness
        hsb[2] = (float) (hsb[2] - hsb[2] * 0.2);

        return new Color2(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    }

    public Color2 saturate() {
        float[] hsb = Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);
        hsb[1] = (float) (hsb[1] + (1 - hsb[1]) * 0.2);
        return new Color2(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    }

    public String toRGBString() {
        return "rgb(" + getRed() + "," + getGreen() + "," + getBlue() + ")";
    }

    public double luminance() {
        return 0.2126 * getRed() + 0.7152 * getGreen() + 0.0722 * getBlue();
    }
}
