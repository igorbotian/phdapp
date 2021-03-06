package ru.spbftu.igorbotian.phdapp.common;

/**
 * Copyright (c) 2014 Igor Botian
 * <p>
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

/**
 * Элемент обучающей выборки.
 * По своей сути является тем же исходым объектом, но для него известен реальный класс классификации,
 * которому он соответствует.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.DataClass
 * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject
 */
public interface PointwiseTrainingObject extends UnclassifiedObject {

    /**
     * Получение реального класса классификации, которому соответствует объект
     *
     * @return реальный класса классификации
     */
    DataClass realClass();
}
