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

import com.google.inject.AbstractModule;
import ru.spbftu.igorbotian.phdapp.locale.Localization;

/**
 * Модуль локализации на основе стандартных средств локализации Java
 *
 * @see ru.spbftu.igorbotian.phdapp.locale.java.JavaI18NLocalization
 */
public class JavaI18NLocalizationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Localization.class).to(JavaI18NLocalization.class);
    }
}
