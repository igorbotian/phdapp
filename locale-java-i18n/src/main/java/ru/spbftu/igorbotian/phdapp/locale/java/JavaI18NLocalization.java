/**
 * Copyright (c) 2014 Igor Botian
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.locale.java;

import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.locale.Localization;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Реализация
 *
 * @see ru.spbftu.igorbotian.phdapp.locale.Localization
 */
@Singleton
class JavaI18NLocalization implements Localization {

    private static final Logger LOGGER = Logger.getLogger(JavaI18NLocalization.class);

    /**
     * Префикс для файлов локализации
     */
    private static final String LABELS_BUNDLE_BASENAME = "Labels";

    /**
     * Путь к файлам локализации
     */
    private static final String LABELS_BUNDLE_NAME = String.join(".",
            JavaI18NLocalization.class.getPackage().getName(), LABELS_BUNDLE_BASENAME);

    /**
     * Стандартное средство локализации Java
     */
    private final ResourceBundle labels;

    public JavaI18NLocalization() {
        labels = initTranslations();
    }

    private ResourceBundle initTranslations() {
        LOGGER.debug("Loading translations for locale: " + Locale.getDefault().toString());

        try {
            return ResourceBundle.getBundle(LABELS_BUNDLE_NAME, Locale.getDefault());
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to get translations for '%s' locale", Locale.getDefault().toString()), e);
        }

        return null;
    }

    @Override
    public String getLabel(String label) {
        Objects.requireNonNull(label);

        if (StringUtils.isEmpty(label)) {
            throw new IllegalArgumentException("Label cannot be empty");
        }

        if (labels == null) {
            LOGGER.warn("Requesting translation for non-localized label: " + label);
            return label;
        }

        LOGGER.debug("Requesting translation for label: " + label);
        return getBundledLabel(label);
    }

    private String getBundledLabel(String label) {
        assert (StringUtils.isNotEmpty(label));
        assert (labels != null);

        try {
            // файлы локализации имеют кодировку UTF-8, а встроенные средства локализации Java работают с ними
            // в кодировке ISO-8869-1
            return new String(labels.getString(label).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (MissingResourceException e) {
            LOGGER.warn(String.format("There is no translation for label '%s' for locale '%s'",
                    label, Locale.getDefault().toString()), e);
        }

        return label;
    }
}
