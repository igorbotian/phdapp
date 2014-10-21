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

package ru.spbftu.igorbotian.phdapp.input;

import ru.spbftu.igorbotian.phdapp.common.DataException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реестр адаптеров сериализации для заданных типов данных
 * @see ru.spbftu.igorbotian.phdapp.common.DataValueType
 * @see ru.spbftu.igorbotian.phdapp.input.DataValueTypeAdapter
 */
public enum DataValueTypeAdapterRegistry {

    /**
     * Объект для доступа к реестру
     */
    INSTANCE;

    /**
     * Контейнер для хранения связок "тип - адаптер сериализации/десериализации"
     */
    private final Map<String, DataValueTypeAdapter<?>> typeAdapters = new ConcurrentHashMap<>();

    private DataValueTypeAdapterRegistry() {
        registerTypeAdapter(new IntegerDataValueTypeAdapter());
        registerTypeAdapter(new RealDataValueTypeAdapter());
        registerTypeAdapter(new StringDataValueTypeAdapter());
    }

    /**
     * Регистрация адаптера сериализации в реестре
     *
     * @param adapter адаптер сериализации
     * @param <T>     тип, для сериализации объектов которого предназначен данный адаптер
     * @throws java.lang.NullPointerException если адаптер не задан
     */
    public final <T> void registerTypeAdapter(DataValueTypeAdapter<T> adapter) {
        typeAdapters.put(Objects.requireNonNull(adapter).targetType().name(), adapter);
    }

    /**
     * Получение адаптера сериализации для заданного типа данных
     *
     * @param name идентификатора типа данных
     * @return адаптер сериализации/десериализации
     * @throws java.lang.NullPointerException                   если тип данных не задан
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если адаптера для заданного типа данных не зарегистрировано
     */
    public final DataValueTypeAdapter<?> getTypeAdapterNamedAs(String name) throws DataException {
        return typeAdapters.get(Objects.requireNonNull(name));
    }
}
