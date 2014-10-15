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

package ru.spbftu.igorbotian.phdapp.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.DataObjectParameter
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
class DataObjectParameterImpl implements DataObjectParameter {

    /**
     * Название (строковый идентификатор) объекта
     */
    private final String name;

    /**
     * Значение параметра
     */
    private final String value;

    public DataObjectParameterImpl(String name, String value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);

        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        this.name = name;
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
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

        if (obj == null || !(obj instanceof DataObjectParameter)) {
            return false;
        }

        DataObjectParameterImpl other = (DataObjectParameterImpl) obj;
        return (name.equals(other.name) && value.equals(other.value));
    }

    @Override
    public String toString() {
        return String.join(":", name, value);
    }
}