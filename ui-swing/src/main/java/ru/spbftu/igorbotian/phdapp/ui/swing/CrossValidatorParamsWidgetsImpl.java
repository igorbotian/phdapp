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

package ru.spbftu.igorbotian.phdapp.ui.swing;

import ru.spbftu.igorbotian.phdapp.svm.validation.MutableCrossValidatorParameter;
import ru.spbftu.igorbotian.phdapp.ui.common.CrossValidatorParamsFrameDirector;
import ru.spbftu.igorbotian.phdapp.ui.common.UIHelper;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.DoubleRangeSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.DoubleSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.IntegerRangeSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.IntegerSpinner;

import java.util.Objects;

/**
 * Вспомогательный класс для формирования виджетов для задания параметров классификации.
 * Введённые пользователем значения сохраняются по завершению работы программы
 * и задаются по умолчанию при следующей работе приложения.
 */
class CrossValidatorParamsWidgetsImpl implements CrossValidatorParamsWidgets {

    private final UIHelper uiHelper;

    private DoubleSpinner precisePenaltyParamSpinner;
    private DoubleRangeSpinner intervalPenaltyParamSpinner;
    private DoubleSpinner preciseGaussianKernelParamSpinner;
    private DoubleRangeSpinner intervalGaussianKernelParamSpinner;
    private IntegerSpinner samplesToGenerateCountSpinner;
    private IntegerSpinner preciseSampleSizeSpinner;
    private IntegerRangeSpinner intervalSampleSizeSpinner;
    private IntegerSpinner preciseTrainingTestingSetsSizeRatioSpinner;
    private IntegerRangeSpinner intervalTrainingTestingSetsSizeRatioSpinner;
    private IntegerSpinner precisePreciseIntervalJudgmentsCountRatioSpinner;
    private IntegerRangeSpinner intervalPreciseIntervalJudgmentsCountRatioSpinner;

    public CrossValidatorParamsWidgetsImpl(UIHelper uiHelper) {
        this.uiHelper = Objects.requireNonNull(uiHelper);

        CrossValidatorParamsFrameDirector director = uiHelper.crossValidatorParamsFrameDirector();

        precisePenaltyParamSpinner = preciseDoubleSpinner(director.penaltyParameter(), PENALTY_PARAMETER_STEP_SIZE);
        intervalPenaltyParamSpinner = doubleRangeSpinner(director.penaltyParameter(), PENALTY_PARAMETER_STEP_SIZE);

        preciseGaussianKernelParamSpinner = preciseDoubleSpinner(director.gaussianKernelParameter(),
                GAUSSIAN_KERNEL_PARAMETER_STEP_SIZE);
        intervalGaussianKernelParamSpinner = doubleRangeSpinner(director.gaussianKernelParameter(),
                GAUSSIAN_KERNEL_PARAMETER_STEP_SIZE);

        samplesToGenerateCountSpinner = preciseIntegerSpinner(director.samplesToGenerateCount(),
                SAMPLES_TO_GENERATE_COUNT_STEP_SIZE);

        preciseSampleSizeSpinner = preciseIntegerSpinner(director.sampleSize(),
                SAMPLE_SIZE_STEP_SIZE);
        intervalSampleSizeSpinner = integerRangeSpinner(director.sampleSize(),
                SAMPLES_TO_GENERATE_COUNT_STEP_SIZE);

        preciseTrainingTestingSetsSizeRatioSpinner = preciseIntegerSpinner(director.trainingTestingSetsSizeRatio(),
                TRAINING_TESTING_SETS_SIZE_RATIO_STEP_SIZE);
        intervalTrainingTestingSetsSizeRatioSpinner = integerRangeSpinner(director.trainingTestingSetsSizeRatio(),
                TRAINING_TESTING_SETS_SIZE_RATIO_STEP_SIZE);

        precisePreciseIntervalJudgmentsCountRatioSpinner
                = preciseIntegerSpinner(director.preciseIntervalJudgmentsCountRatio(),
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_STEP_SIZE);
        intervalPreciseIntervalJudgmentsCountRatioSpinner
                = integerRangeSpinner(director.preciseIntervalJudgmentsCountRatio(),
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_STEP_SIZE);
    }

    private IntegerSpinner preciseIntegerSpinner(MutableCrossValidatorParameter<Integer> parameter, int uiStepSize) {
        IntegerSpinner spinner = new IntegerSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.value().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                uiStepSize
        );
        spinner.addChangeListener(e -> parameter.value().setValue(spinner.getValue()));
        return spinner;
    }

    private DoubleSpinner preciseDoubleSpinner(MutableCrossValidatorParameter<Double> parameter, double uiStepSize) {
        DoubleSpinner spinner = new DoubleSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.value().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                uiStepSize
        );
        spinner.addChangeListener(e -> parameter.value().setValue(spinner.getValue()));
        return spinner;
    }

    private IntegerRangeSpinner integerRangeSpinner(MutableCrossValidatorParameter<Integer> parameter, int uiStepSize) {
        IntegerRangeSpinner spinner = new IntegerRangeSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.lowerBound().value(),
                parameter.lowerBound().minValue(),
                parameter.lowerBound().maxValue(),
                parameter.upperBound().value(),
                parameter.upperBound().minValue(),
                parameter.upperBound().maxValue(),
                parameter.stepSize().value(),
                parameter.stepSize().minValue(),
                parameter.stepSize().maxValue(),
                uiStepSize
        );

        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMinValue()));
        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMaxValue()));
        spinner.addStepSizeChangeListener(e -> parameter.stepSize().setValue(spinner.getStepSize()));

        return spinner;
    }

    private DoubleRangeSpinner doubleRangeSpinner(MutableCrossValidatorParameter<Double> parameter, double uiStepSize) {
        DoubleRangeSpinner spinner = new DoubleRangeSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.lowerBound().value(),
                parameter.lowerBound().minValue(),
                parameter.lowerBound().maxValue(),
                parameter.upperBound().value(),
                parameter.upperBound().minValue(),
                parameter.upperBound().maxValue(),
                parameter.stepSize().value(),
                parameter.stepSize().minValue(),
                parameter.stepSize().maxValue(),
                uiStepSize
        );

        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMinValue()));
        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMaxValue()));
        spinner.addStepSizeChangeListener(e -> parameter.stepSize().setValue(spinner.getStepSize()));

        return spinner;
    }

    @Override
    public DoubleSpinner precisePenaltyParamSpinner() {
        return precisePenaltyParamSpinner;
    }

    @Override
    public DoubleRangeSpinner intervalPenaltyParamSpinner() {
        return intervalPenaltyParamSpinner;
    }

    @Override
    public DoubleSpinner preciseGaussianKernelParamSpinner() {
        return preciseGaussianKernelParamSpinner;
    }

    @Override
    public DoubleRangeSpinner intervalGaussianKernelParamSpinner() {
        return intervalGaussianKernelParamSpinner;
    }

    @Override
    public IntegerSpinner preciseSamplesToGenerateCountSpinner() {
        return samplesToGenerateCountSpinner;
    }

    @Override
    public IntegerSpinner preciseSampleSizeSpinner() {
        return preciseSampleSizeSpinner;
    }

    @Override
    public IntegerRangeSpinner intervalSampleSizeSpinner() {
        return intervalSampleSizeSpinner;
    }

    @Override
    public IntegerSpinner preciseTrainingTestingSetsSizeRatioSpinner() {
        return preciseTrainingTestingSetsSizeRatioSpinner;
    }

    @Override
    public IntegerRangeSpinner intervalTrainingTestingSetsSizeRatioSpinner() {
        return intervalTrainingTestingSetsSizeRatioSpinner;
    }

    @Override
    public IntegerSpinner precisePreciseIntervalJudgmentsCountRatioSpinner() {
        return precisePreciseIntervalJudgmentsCountRatioSpinner;
    }

    @Override
    public IntegerRangeSpinner intervalPreciseIntervalJudgmentsCountRatioSpinner() {
        return intervalPreciseIntervalJudgmentsCountRatioSpinner;
    }
}
