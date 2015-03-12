/*
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
 * Предпочтение одного набора исходных объектов над другим.
 * Используется в классификаторах на основе попарного обучения.
 * Объекты, содержащиеся в этих наборах, должны быть разделяемы, то есть относиться к разным классам.
 * Как вывод, один и тот же объект не может находиться одновременно сразу в двух наборах.
 */
public interface Judgement {

    /**
     * Получение набора исходных объектов, имеющего предпочтение над другим
     *
     * @return набора исходных объектов
     */
    Set<? extends UnclassifiedObject> preferable();

    /**
     * Получение набора исходных объектов, над которым другой набор имеет предпочтение
     *
     * @return набора исходных объектов
     */
    Set<? extends UnclassifiedObject> inferior();
}
