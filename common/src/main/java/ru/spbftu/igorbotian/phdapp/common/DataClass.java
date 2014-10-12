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
 * Класс, которому соответствует классифцируемый или ранжируемый объект.
 * Класс является потокобезопасным, а его объекты - неизменяемыми.
 *
 * @see DataObject, TrainingDataObject
 */
public class DataClass {

    /**
     * Название класса (его строковое представление)
     */
    private final String name;

    /**
     * Конструктор объекта
     *
     * @param name идентификатор класса (не может быть пустым)
     * @throws java.lang.IllegalArgumentException если идентификатор класса пустой или равен <code>null</code>
     */
    public DataClass(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.name = name;
    }

    /**
     * Получение идентификатора класса
     *
     * @return строковое представление названия класса
     */
    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof DataClass)) {
            return false;
        }

        return name.equals(((DataClass) obj).name);
    }

    @Override
    public String toString() {
        return name();
    }
}
