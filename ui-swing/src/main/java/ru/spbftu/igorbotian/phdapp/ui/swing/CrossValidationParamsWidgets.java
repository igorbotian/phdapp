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

import ru.spbftu.igorbotian.phdapp.ui.swing.widget.DoubleRangeSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.DoubleSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.IntegerRangeSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.IntegerSpinner;

import javax.swing.*;

/**
 * Средство для формирования виджетов для задания параметров средства кросс-валидации.
 * Введённые пользователем значения сохраняются по завершению работы программы
 * и задаются по умолчанию при следующей работе приложения.
 */
public interface CrossValidationParamsWidgets {

    /**
     * Величина изменения шага для штрафного параметра
     */
    double PENALTY_PARAMETER_STEP_SIZE = 0.1;

    /**
     * Величина изменения шага для параметра Гауссова ядра
     */
    double GAUSSIAN_KERNEL_PARAMETER_STEP_SIZE = 0.1;

    /**
     * Величина изменения шага для параметра, задающего количество генерируемых выборок
     */
    int SAMPLES_TO_GENERATE_COUNT_STEP_SIZE = 1;

    /**
     * Величина изменения шага для параметра, задающего количество объектов в генерируемой выборке
     */
    int SAMPLE_SIZE_STEP_SIZE = 2;

    /**
     * Величина изменения шага для параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках
     */
    int TRAINING_TESTING_SETS_SIZE_RATIO_STEP_SIZE = 1;

    /**
     * Величина изменения шага для параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_STEP_SIZE = 1;

    /**
     * Заданное значение максимально допустимого размера множеств объектов в экспертной оценке
     */
    IntegerSpinner maxJudgementGroupSizeParamSpinner();

    /**
     * Заданное значение штрафного параметра
     */
    DoubleSpinner precisePenaltyParamSpinner();

    /**
     * Заданный диапазон значений штрафного параметра
     */
    DoubleRangeSpinner intervalPenaltyParamSpinner();

    /**
     * Заданное значение параметра Гауссова ядра
     */
    DoubleSpinner preciseGaussianKernelParamSpinner();

    /**
     * Заданный диапазон значений параметра Гауссова ядра
     */
    DoubleRangeSpinner intervalGaussianKernelParamSpinner();

    /**
     * Заданное значение количества итераций
     */
    IntegerSpinner preciseSamplesToGenerateCountSpinner();

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
    IntegerSpinner preciseTrainingTestingSetsSizeRatioSpinner();

    /**
     * Заданный диапазон значений количества элементов выборки в процентном соотношении,
     * для которых задано хотя бы одно предпочтение
     */
    IntegerRangeSpinner intervalTrainingTestingSetsSizeRatioSpinner();

    /**
     * Заданное значение процентного соотношения точных/интервальных предпочтений для элементов выборки
     */
    IntegerSpinner precisePreciseIntervalJudgmentsCountRatioSpinner();

    /**
     * Заданный диапазон значений процентного соотношения точных/интервальных предпочтений для элементов выборки
     */
    IntegerRangeSpinner intervalPreciseIntervalJudgmentsCountRatioSpinner();

    /**
     * Галочка задания необходимости остановки процесса кросс-валидации при ошибке
     */
    JCheckBox stopCrossValidationOnErrorCheckBox();
}
