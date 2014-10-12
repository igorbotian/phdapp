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

/**
 * Параметр объекта, подлежащего класссификации или другому действию.
 * Параметр характеризуется названием и значением.
 * Класс является потокобезопасным, а его объекты - неизменяемыми.
 * <p>
 * * @see DataObject
 */
public class DataObjectParameter {

    /**
     * Название (строковый идентификатор) объекта
     */
    private final String name;

    /**
     * Значение параметра
     */
    private final String value;

    /**
     * Конструктор класса
     *
     * @param name  название объекта (не может быть <code>null</code> или пустым)
     * @param value значение объекта (не может быть <code>null</code>)
     * @throws java.lang.IllegalArgumentException если название объекта пустое или равно <code>null</code>
     * @throws java.lang.NullPointerException     если значение объекта равно <code>null</code>
     */
    public DataObjectParameter(String name, String value) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }

        this.name = name;
        this.value = value;
    }

    /**
     * Получение названия параметра
     *
     * @return строковое представление названия параметра
     */
    public String name() {
        return name;
    }

    /**
     * Получение значения параметра
     *
     * @return значение параметра заданного класса; не может быть null
     */
    public String value() {
        return value;
    }

    @Override
    public int hashCode() {
        int hashCode = 37;

        hashCode += name.hashCode() + 17;
        hashCode += value.hashCode() + 31;

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof DataObjectParameter)) {
            return false;
        }

        DataObjectParameter other = (DataObjectParameter) obj;
        return (name.equals(other.name) && value.equals(other.value));
    }

    @Override
    public String toString() {
        return String.format("%s:%s", name, value);
    }
}
