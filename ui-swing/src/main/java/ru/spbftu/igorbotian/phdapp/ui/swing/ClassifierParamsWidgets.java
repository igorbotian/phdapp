/*
 * Copyright (c) 2014 Igor Botian
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General License
 * for more details. You should have received a copy of the GNU General
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.ui.swing;

import ru.spbftu.igorbotian.phdapp.svm.analytics.SampleGenerator;

import javax.swing.*;

/**
 * Средство для формирования виджетов для задания параметров классификации.
 * Введённые пользователем значения сохраняются по завершению работы программы
 * и задаются по умолчанию при следующей работе приложения.
 */
interface ClassifierParamsWidgets {

    /**
     * Заданное значение коэффициента С
     */
    DoubleSpinner preciseCParamSpinner();

    /**
     * Заданный диапазон значений коэффициента С
     */
    DoubleRangeSpinner intervalCParamSpinner();

    /**
     * Заданное значение коэффициента Сигма
     */
    DoubleSpinner preciseSigmaParamSpinner();

    /**
     * Заданный диапазон значений коэффициента Сигма
     */
    DoubleRangeSpinner intervalSigmaParamSpinner();

    /**
     * Заданное значение количества итераций
     */
    IntegerSpinner preciseNumberOfIterationsSpinner();

    /**
     * Заданный диапазон значений количества итераций
     */
    IntegerRangeSpinner intervalNumberOfIterationsSpinner();

    /**
     * Заданное значение количества элементов в выборке
     */
    IntegerSpinner preciseSampleSizeSpinner();

    /**
     * Заданный диапазон значений количества элементов в выборке
     */
    IntegerRangeSpinner intervalSampleSizeSpinner();

    /**
     * Заданное значение количества элементов выборки в процентном отношении,
     * для которых задано хотя бы одно предпочтение
     */
    DoubleSpinner precisePercentOfJudgedSampleItemsSpinner();

    /**
     * Заданный диапазон значений количества элементов выборки в процентном соотношении,
     * для которых задано хотя бы одно предпочтение
     */
    DoubleRangeSpinner intervalPercentOfJudgedSampleItemsSpinner();

    /**
     * Заданное значение процентного соотношения точных/интервальных предпочтений для элементов выборки
     */
    DoubleSpinner precisePreciseIntervalJudgedSampleItemsRatioSpinner();

    /**
     * Заданный диапазон значений процентного соотношения точных/интервальных предпочтений для элементов выборки
     */
    DoubleRangeSpinner intervalPreciseIntervalJudgedSampleItemsRatioSpinner();

    /**
     * Кнопка для открытия диалогового окна, отображающего сгенерированную для классификатора выборку
     */
    JButton sampleViewButton(SampleGenerator sampleGenerator);
}
