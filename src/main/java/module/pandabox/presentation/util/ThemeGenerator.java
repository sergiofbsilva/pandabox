/*
 * @(#)ThemeGenerator.java
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
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class ThemeGenerator {

    private static final String OPTIONAL_BACKGROUND = "optional-background";
    private static final String OPTIONAL_BACKGROUND_COLOR = "optional-background-color";
    private static final String OPTIONAL_GENERATED_BACKGROUND_COLOR = "optional-generated-background-color";
    private static final String GENERATED_BORDER_COLOR = "generated-border-color";
    private static final String OPTIONAL_GENERATED_SHADOW = "optional-generated-shadow";

    private static final String FONT_SIZE = "font-size";

    private static final String FONT_COLOR = "font-color";

    private static final String BASE_BACKGROUND_COLOR = "base-background-color";
    private static final String GENERATED_BASE_BACKGROUND_DARKER_COLOR = "generated-base-background-darker-color";
    private static final String GENERATED_BASE_BORDER_COLOR = "generated-base-border-color";
    private static final String GENERATED_BASE_BORDER_SECONDARY_COLOR = "generated-base-border-secondary-color";
    private static final String GENERATED_BASE_BORDER_FOCUS_COLOR = "generated-base-border-focus-color";
    private static final String GENERATED_BASE_BORDER_LIGHTER_COLOR = "generated-base-border-lighter-color";
    private static final String GENERATED_BASE_SHADOW = "generated-base-shadow";
    private static final String GENERATED_DARK_SHADOW = "generated-dark-shadow";
    private static final String OPTIONAL_GENERATED_BASE_GRADIENT = "optional-generated-base-gradient";

    private static final String ALTERNATE_COLOR = "alternate-color";
    private static final String GENERATED_ALTERNATE_SECONDARY_COLOR = "generated-alternate-secondary-color";
    private static final String GENERATED_ALTERNATE_DARKER_COLOR = "generated-alternate-darker-color";
    private static final String GENERATED_ALTERNATE_BORDER_COLOR = "generated-alternate-border-color";
    private static final String GENERATED_ALTERNATE_BORDER_SECONDARY_COLOR = "generated-alternate-border-secondary-color";
    private static final String GENERATED_ALTERNATE_BORDER_FOCUS_COLOR = "generated-alternate-border-focus-color";
    private static final String GENERATED_ALTERNATE_FONT_COLOR = "generated-alternate-font-color";
    private static final String GENERATED_ALTERNATE_SHADOW = "generated-alternate-shadow";
    private static final String GENERATED_ALTERNATE_SHADOW2 = "generated-alternate-shadow2";

    private static class StyleTemplate {
        private String value;

        public StyleTemplate(String template) {
            value = template;
        }

        public void setStyle(String slot, String value) {
            this.value = this.value.replaceAll("@" + slot + "@", value);
        }

        public String getValue() {
            return value;
        }
    }

    public static String generateTheme(String path, Color baseColor,
            boolean appBackground, Color backgroundColor, Color alternateColor,
            Color fontColor, String fontSize) {
        StyleTemplate t = new StyleTemplate(getTemplate());

        Color2 baseColor2 = new Color2(baseColor);
        Color2 backgroundColor2 = new Color2(backgroundColor);
        Color2 alternateColor2 = new Color2(alternateColor);
        Color2 fontColor2 = new Color2(fontColor);

        // Application background (and overlay backgrounds)
        if (backgroundColor2 != null) {
            if (appBackground) {
                t.setStyle(OPTIONAL_BACKGROUND, "background: "
                        + backgroundColor2.toRGBString() + ";");
            } else {
                t.setStyle(OPTIONAL_BACKGROUND, "");
            }
            t.setStyle(OPTIONAL_BACKGROUND_COLOR, "background-color: "
                    + backgroundColor2.toRGBString() + ";");
            t.setStyle(
                    OPTIONAL_GENERATED_BACKGROUND_COLOR,
                    "background-color: " + backgroundColor2.toRGBString()
                            + ";\n\tbackground-color: rgba("
                            + backgroundColor2.getRed() + ","
                            + backgroundColor2.getGreen() + ","
                            + backgroundColor2.getBlue() + ",.85);");
            t.setStyle(GENERATED_BORDER_COLOR,
                    backgroundColor2.luminance() > 122 ? backgroundColor2
                            .darker().toRGBString() : backgroundColor2
                            .brighter().toRGBString()
                            + ";\n\tborder-color: rgba("
                            + backgroundColor2.brighter().getRed()
                            + ","
                            + backgroundColor2.brighter().getGreen()
                            + ","
                            + backgroundColor2.brighter().getBlue() + ",.3);");
            if (backgroundColor2.luminance() < 100) {
                t.setStyle(OPTIONAL_GENERATED_SHADOW, "text-shadow: 0 1px 0 "
                        + backgroundColor2.darker().darker().toRGBString());
            }
            t.setStyle(GENERATED_DARK_SHADOW, "0 1px 0 "
                    + backgroundColor2.darker().darker().toRGBString());
        } else {
            t.setStyle(OPTIONAL_BACKGROUND_COLOR, "");
            t.setStyle(OPTIONAL_GENERATED_BACKGROUND_COLOR, "");
            t.setStyle(GENERATED_BORDER_COLOR, "");
            t.setStyle(GENERATED_DARK_SHADOW, "");
        }

        // Fonts
        t.setStyle(FONT_COLOR, fontColor2.toRGBString());
        t.setStyle(FONT_SIZE, fontSize);

        // Base colors
        if (baseColor2.luminance() > 122) {
            t.setStyle(BASE_BACKGROUND_COLOR, baseColor2.toRGBString());
            t.setStyle(OPTIONAL_GENERATED_BASE_GRADIENT, "");
        } else {
            t.setStyle(BASE_BACKGROUND_COLOR, baseColor2.toRGBString());
            t.setStyle(
                    OPTIONAL_GENERATED_BASE_GRADIENT,
                    "background-image: url("
                            + path
                            + "VAADIN/themes/chameleon/img/grad-light-top2.png);\n\tbackground-position: left top;");
        }
        t.setStyle(GENERATED_BASE_BACKGROUND_DARKER_COLOR, baseColor2.darker()
                .toRGBString());
        t.setStyle(GENERATED_BASE_BORDER_COLOR, baseColor2.darker().darker()
                .toRGBString());
        t.setStyle(GENERATED_BASE_BORDER_LIGHTER_COLOR, baseColor2.darker()
                .toRGBString());
        t.setStyle(GENERATED_BASE_BORDER_SECONDARY_COLOR, baseColor2.darker()
                .darker().darker().toRGBString());
        t.setStyle(GENERATED_BASE_BORDER_FOCUS_COLOR, baseColor2.darker()
                .darker().darker().darker().toRGBString());
        t.setStyle(GENERATED_BASE_SHADOW, baseColor2.luminance() > 122
                ? ("0 1px 0 " + baseColor2.brighter().toRGBString())
                : ("0 -1px 0 " + baseColor2.darker().darker().toRGBString()));

        // Alternate colors
        t.setStyle(ALTERNATE_COLOR, alternateColor2.toRGBString());
        if (fontColor2.luminance() < 122) {
            t.setStyle(GENERATED_ALTERNATE_SECONDARY_COLOR, alternateColor2
                    .brighter().brighter().toRGBString());
        } else {
            t.setStyle(GENERATED_ALTERNATE_SECONDARY_COLOR, alternateColor2
                    .darker().darker().darker().darker().darker().toRGBString());
        }
        t.setStyle(GENERATED_ALTERNATE_DARKER_COLOR, alternateColor2.darker()
                .toRGBString());
        if (backgroundColor2 != null
                && alternateColor2.luminance() > backgroundColor2.luminance()) {
            t.setStyle(GENERATED_ALTERNATE_BORDER_COLOR, alternateColor2
                    .darker().darker().darker().toRGBString());
        } else {
            t.setStyle(GENERATED_ALTERNATE_BORDER_COLOR, alternateColor2
                    .darker().darker().toRGBString());
        }
        t.setStyle(GENERATED_ALTERNATE_BORDER_SECONDARY_COLOR, alternateColor2
                .darker().toRGBString());
        t.setStyle(GENERATED_ALTERNATE_BORDER_FOCUS_COLOR, alternateColor2
                .darker().darker().darker().darker().toRGBString());
        if (alternateColor2.luminance() > 180) {
            t.setStyle(GENERATED_ALTERNATE_FONT_COLOR, alternateColor2.darker()
                    .darker().darker().darker().saturate().saturate()
                    .saturate().saturate().toRGBString());
            t.setStyle(GENERATED_ALTERNATE_SHADOW, "0 1px 0 "
                    + alternateColor2.brighter().toRGBString());
            t.setStyle(GENERATED_ALTERNATE_SHADOW2, "0 1px 0 "
                    + alternateColor2.darker().toRGBString());
        } else {
            t.setStyle(GENERATED_ALTERNATE_FONT_COLOR, alternateColor2
                    .brighter().brighter().brighter().brighter().toRGBString());
            t.setStyle(GENERATED_ALTERNATE_SHADOW, "0 -1px 0 "
                    + alternateColor2.darker().darker().toRGBString());
            t.setStyle(GENERATED_ALTERNATE_SHADOW2, "0 1px 0 "
                    + alternateColor2.darker().darker().toRGBString());
        }

        return t.getValue();
    }

    public static String getTemplate() {
        try {
            return readFileAsString("theme-template.css");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String readFileAsString(String filePath)
            throws java.io.IOException {
        byte[] buffer = new byte[20480];
        BufferedInputStream f = new BufferedInputStream(
                ThemeGenerator.class.getResourceAsStream(filePath));
        int read = f.read(buffer);
        byte[] next = new byte[read];
        for (int i = 0; i < read; i++) {
            next[i] = buffer[i];
        }
        return new String(next);
    }
}
