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

import java.util.Objects;

/**
 * Каркас для создания типов данных <code>DataType</code>
 *
 * @param <T> Java-класс, соответствующий типу данных
 * @see DataType
 */
public abstract class AbstractDataType<T> implements DataType<T> {

    /**
     * Идентификатор типа данных
     */
    private final String name;

    /**
     * Java-класс, соответствующий этому типу данных
     */
    private final Class<T> javaClass;

    /**
     * Конструктор класса
     *
     * @param name      идентификатор типа данных
     * @param javaClass Java-класс, соответствующий этому типу данных
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     */
    public AbstractDataType(String name, Class<T> javaClass) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(javaClass);

        this.name = name;
        this.javaClass = javaClass;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<T> javaClass() {
        return javaClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, javaClass.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof AbstractDataType)) {
            return false;
        }

        AbstractDataType other = (AbstractDataType) obj;
        return (name.equals(other.name) && javaClass.equals(other.javaClass));
    }

    @Override
    public String toString() {
        return String.join(":", name, javaClass.getName());
    }
}
