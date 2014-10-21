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
import ru.spbftu.igorbotian.phdapp.common.DataValueType;

/**
 * Адаптер для серилазиции и десериализации объектов заданного типа в и из строкового представления
 *
 * @param <T> тип сериализуемых объектов
 */
public interface DataValueTypeAdapter<T> {

    /**
     * Получение типа данных, с которым работает адаптер
     *
     * @return объект типа <code>DataValueType</code>
     */
    DataValueType<T> targetType();

    /**
     * Получение заданного объекта заданного в строковом представлении
     *
     * @param value исходный объект
     * @return строковое представление заданного объекта
     */
    String toString(T value);

    /**
     * Десериализация объекта заданного типа из строкового представления
     *
     * @param str строкое представление объекта заданного типа
     * @return десериализованный объект
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если заданная строка имеет значение,
     *                                                          которое не содержит объектов заданного типа
     *                                                          в строковом представлении
     */
    T fromString(String str) throws DataException;
}
