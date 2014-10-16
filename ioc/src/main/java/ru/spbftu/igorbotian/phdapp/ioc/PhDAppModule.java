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

package ru.spbftu.igorbotian.phdapp.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Базовый класс для модуля приложений, предоставлющий дополнительные возможности
 */
public abstract class PhDAppModule extends AbstractModule {

    private final Map<Class, Multibinder> multibinders = new HashMap<>();

    /*
     * Для абстракций, для которых есть предоставляется только одна конкретная реализация
     */
    protected <T> void bind(Class<T> interfaceClass, Class<? extends T> implementationClass) {
        Objects.requireNonNull(interfaceClass);
        Objects.requireNonNull(implementationClass);

        bind(interfaceClass).to(implementationClass);
    }

    /*
     * Для абстракций, для которых может существовать множество реализаций, используемых всех вместе
     */
    @SuppressWarnings("unchecked")
    protected <T> void multiBind(Class<T> interfaceClass, Class<? extends T> implementationClass) {
        Objects.requireNonNull(interfaceClass);
        Objects.requireNonNull(implementationClass);

        if (!multibinders.containsKey(interfaceClass)) {
            multibinders.put(interfaceClass, Multibinder.newSetBinder(binder(), interfaceClass));
        }

        multibinders.get(interfaceClass).addBinding().to(implementationClass);
    }
}
