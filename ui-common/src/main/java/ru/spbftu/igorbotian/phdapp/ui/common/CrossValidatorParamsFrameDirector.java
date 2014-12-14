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

package ru.spbftu.igorbotian.phdapp.ui.common;

import ru.spbftu.igorbotian.phdapp.svm.validation.MutableCrossValidatorParameter;

/**
 * Модель окна, отображающего компоненты для задания параметров кросс-валидации
 */
public interface CrossValidatorParamsFrameDirector {

    /**
     * Получение значения параметра постоянной стоимости
     *
     * @return параметр классификатора со значением вещественного типа
     */
    MutableCrossValidatorParameter<Double> constantCostParameter();

    /**
     * Получение значения параметра Гауссова ядра
     *
     * @return параметр классификатора со значением вещественного типа
     */
    MutableCrossValidatorParameter<Double> gaussianKernelParameter();

    /**
     * Получение параметра, задающего количество генерируемых выборок
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> samplesToGenerateCount();

    /**
     * Получение параметра, задающего количество объектов в генерируемой выборке, т.е. её размер
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> sampleSize();

    /**
     * Получение параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> trainingTestingSetsSizeRatio();

    /**
     * Получение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio();
}
