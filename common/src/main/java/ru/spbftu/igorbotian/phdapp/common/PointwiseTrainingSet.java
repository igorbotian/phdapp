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
 * Исходные данные, предназначенные для обучения поточечного классификатора или какого-либо другого механизма для работы с ними.
 * Характеризуются наличием обучающей выборки, содержащей информацию о реальных классах классификации,
 * которым соответствуют объекты.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.DataClass
 * @see ru.spbftu.igorbotian.phdapp.common.PointwiseTrainingObject
 */
public interface PointwiseTrainingSet extends UnclassifiedData {

    /**
     * Получение обучающей выборки
     *
     * @return непустое неизменяемое множество объектов, для которых известны реальные классы классификации
     */
    Set<? extends PointwiseTrainingObject> objects();
}
