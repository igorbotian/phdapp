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

import java.util.Set;

/**
 * Набор для для классификации или выполнения какого-либо другого действия.
 * Состоит из набора классов классификации и множества объектов, которые необходимо классифицировать.
 * Класс является потокобезопасным, а его объекты - неизменяемыми.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.DataClass
 * @see ru.spbftu.igorbotian.phdapp.common.DataObject
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingData
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingDataBuilder
 */
public interface Data {

    /**
     * Получение набора классов классификации
     *
     * @return непустое неизменяемое множество классов классификации
     */
    Set<? extends DataClass> classes();

    /**
     * Получение множества объектов, предназначенных для классификации
     *
     * @return непустое неизменяемое множество объектов
     */
    Set<? extends DataObject> objects();
}
