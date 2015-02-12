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

package ru.spbftu.igorbotian.phdapp.common.impl;

import ru.spbftu.igorbotian.phdapp.common.DataType;
import ru.spbftu.igorbotian.phdapp.common.Parameter;

import java.util.Objects;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.Parameter
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
class ParameterImpl<V> implements Parameter<V> {

    /**
     * Название (строковый идентификатор) объекта
     */
    private final String name;

    /**
     * Значение параметра
     */
    private final V value;

    /**
     * Класс значения параметра
     */
    private final DataType<V> valueType;

    public ParameterImpl(String name, V value, DataType<V> valueType) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        Objects.requireNonNull(valueType);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        this.name = name;
        this.value = value;
        this.valueType = valueType;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public DataType<V> valueType() {
        return valueType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Parameter)) {
            return false;
        }

        ParameterImpl other = (ParameterImpl) obj;
        return name.equals(other.name) && value.equals(other.value) && valueType.equals(other.valueType);
    }

    @Override
    public String toString() {
        return String.join(":", name, value.toString(), valueType.name());
    }
}
