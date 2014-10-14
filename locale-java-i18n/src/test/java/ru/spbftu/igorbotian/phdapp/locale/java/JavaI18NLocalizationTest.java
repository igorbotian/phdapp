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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.locale.Localization;

import java.util.Locale;

/**
 * Модульные тесты для класса <code>JavaI18NLocalization</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.locale.java.JavaI18NLocalization
 */
public class JavaI18NLocalizationTest {

    /**
     * Локаль, которая будет использоваться в тестах (переводы для неё обязательно должны существовать)
     */
    private static final Locale TEST_LOCALE = Locale.forLanguageTag("ru-RU");

    /**
     * Локаль, для которой заведомо нет переводов (см. файлы локализации <code>Labels</code>)
     */
    private static final Locale LOCALE_WITHOUT_TRANSLATIONS = Locale.KOREA;

    /**
     * Любая существующая строка, перевод для которой существует для текущей локали
     * (см. файлы локализации <code>Labels</code>)
     */
    private static final String LOCALIZED_LABEL = "about";

    /**
     * Перевод заданной заведомо существующей строки с идентификатором <code>LOCALIZED_LABEL</code>
     * с учётом заданной локали <code>TEST_LOCALE</code>
     */
    private static final String LOCALIZED_LABEL_TRANSLATION = "О программе";

    /**
     * Перевод заданной заведомо существующей строки с идентификатором <code>LOCALIZED_LABEL</code>
     * для локали по умолчанию
     */
    private static final String DEFAULT_LABEL_TRANSLATION = "About";

    /**
     * Любая существующая строка, перевода для которой не существует для текущей локали
     * (см. файлы локализации <code>Labels</code>)
     */
    private static final String NON_LOCALIZED_LABEL = "nonLocalizedLabel";

    /**
     * Объект тестируемого класса
     */
    private Localization localization;

    /**
     * В тестах изменяется текущая локаль, поэтому по их завершению её значение должно быть вернуто к исходному
     */
    private Locale defaultLocale;

    @Before
    public void testUp() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(TEST_LOCALE);
        localization = new JavaI18NLocalization();
    }

    @After
    public void tearDown() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void testGetLocalizedLabel() {
        Assert.assertEquals(LOCALIZED_LABEL_TRANSLATION, localization.getLabel(LOCALIZED_LABEL));
    }

    @Test
    public void testGetNonLocalizedLabel() {
        Assert.assertEquals(NON_LOCALIZED_LABEL, localization.getLabel(NON_LOCALIZED_LABEL));
    }

    @Test
    public void testGetDefaultLocalization() {
        Locale.setDefault(LOCALE_WITHOUT_TRANSLATIONS);
        Localization localization = new JavaI18NLocalization();
        Assert.assertEquals(DEFAULT_LABEL_TRANSLATION, localization.getLabel(LOCALIZED_LABEL));
    }
}
