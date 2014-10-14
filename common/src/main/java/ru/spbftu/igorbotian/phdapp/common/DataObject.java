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

import java.util.Collections;
import java.util.Set;

/**
 * Объект, предназначенный для классификации или проведения какого-либо другого дейстсвия.
 * Каждый объект характеризуется идентификатором и набором параметров.
 * Класс является потобезопасным, а его объекты - неизменяемыми.
 *
 * @see DataObjectParameter , Data
 */
public class DataObject {

    /**
     * Идентификатор объекта (необходим для быстрого различения одного объекта от другого)
     */
    private final String id;

    /**
     * Набор параметров объекта, которыми он характеризуется
     */
    private final Set<DataObjectParameter> parameters;

    /**
     * Конструктор класса
     *
     * @param id         строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param parameters множество параметров, которыми характеризуется объект (непустое)
     * @throws java.lang.NullPointerException     если множества параметров равно <code>null</code>
     * @throws java.lang.IllegalArgumentException если идентификатор объекта пустой или множество параметров
     *                                            не содержит ни одного элемента
     */
    public DataObject(String id, Set<DataObjectParameter> parameters) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("ID cannot be empty");
        }

        if (parameters == null) {
            throw new NullPointerException("Parameters cannot be null");
        }

        if (parameters.isEmpty()) {
            throw new IllegalArgumentException("At least one parameter should be");
        }

        this.id = id;
        this.parameters = Collections.unmodifiableSet(parameters);
    }

    /**
     * Получение идентификатора объекта
     *
     * @return строковое представление идентификатора объекта
     */
    public String id() {
        return id;
    }

    /**
     * Получение множества параметров, которыми характеризуется объект
     *
     * @return непустое неизменяемое множество параметров
     */
    public Set<DataObjectParameter> parameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        hashCode += id.hashCode() + 37;
        hashCode += parameters.hashCode() + 51;

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof DataObject)) {
            return false;
        }

        DataObject other = (DataObject) obj;
        return id.equals(other.id)
                && parameters.size() == other.parameters.size()
                && parameters.containsAll(other.parameters);
    }

    @Override
    public String toString() {
        return String.join(":", id, parameters.toString());
    }
}
