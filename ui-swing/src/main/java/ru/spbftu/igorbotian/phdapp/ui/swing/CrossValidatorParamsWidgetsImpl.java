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

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
@Singleton
class CrossValidatorParamsWidgetsImpl implements CrossValidatorParamsWidgets {

    private final UIHelper uiHelper;

    private DoubleSpinner preciseConstantCostParamSpinner;
    private DoubleRangeSpinner intervalConstantCostParamSpinner;
    private DoubleSpinner preciseGaussianKernelParamSpinner;
    private DoubleRangeSpinner intervalGaussianKernelParamSpinner;
    private IntegerSpinner samplesToGenerateCountSpinner;
    private IntegerSpinner preciseSampleSizeSpinner;
    private IntegerRangeSpinner intervalSampleSizeSpinner;
    private IntegerSpinner preciseTrainingTestingSetsSizeRatioSpinner;
    private IntegerRangeSpinner intervalTrainingTestingSetsSizeRatioSpinner;
    private IntegerSpinner precisePreciseIntervalJudgmentsCountRatioSpinner;
    private IntegerRangeSpinner intervalPreciseIntervalJudgmentsCountRatioSpinner;

    @Inject
    public CrossValidatorParamsWidgetsImpl(UIHelper uiHelper) {
        this.uiHelper = Objects.requireNonNull(uiHelper);

        CrossValidatorParamsFrameDirector director = uiHelper.crossValidatorParamsFrameDirector();

        preciseConstantCostParamSpinner = preciseDoubleSpinner(director.constantCostParameter());
        intervalConstantCostParamSpinner = doubleRangeSpinner(director.constantCostParameter());

        preciseGaussianKernelParamSpinner = preciseDoubleSpinner(director.gaussianKernelParameter());
        intervalGaussianKernelParamSpinner = doubleRangeSpinner(director.gaussianKernelParameter());

        samplesToGenerateCountSpinner = preciseIntegerSpinner(director.samplesToGenerateCount());

        preciseSampleSizeSpinner = preciseIntegerSpinner(director.sampleSize());
        intervalSampleSizeSpinner = integerRangeSpinner(director.sampleSize());

        preciseTrainingTestingSetsSizeRatioSpinner = preciseIntegerSpinner(director.trainingTestingSetsSizeRatio());
        intervalTrainingTestingSetsSizeRatioSpinner = integerRangeSpinner(director.trainingTestingSetsSizeRatio());

        precisePreciseIntervalJudgmentsCountRatioSpinner
                = preciseIntegerSpinner(director.preciseIntervalJudgmentsCountRatio());
        intervalPreciseIntervalJudgmentsCountRatioSpinner
                = integerRangeSpinner(director.preciseIntervalJudgmentsCountRatio());
    }

    private IntegerSpinner preciseIntegerSpinner(MutableCrossValidatorParameter<Integer> parameter) {
        IntegerSpinner spinner = new IntegerSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.value().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                parameter.stepSize().value()
        );
        spinner.addChangeListener(e -> parameter.value().setValue(spinner.getValue()));
        return spinner;
    }

    private DoubleSpinner preciseDoubleSpinner(MutableCrossValidatorParameter<Double> parameter) {
        DoubleSpinner spinner = new DoubleSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.value().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                parameter.stepSize().value()
        );
        spinner.addChangeListener(e -> parameter.value().setValue(spinner.getValue()));
        return spinner;
    }

    private IntegerRangeSpinner integerRangeSpinner(MutableCrossValidatorParameter<Integer> parameter) {

        IntegerRangeSpinner spinner = new IntegerRangeSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.lowerBound().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                parameter.upperBound().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                parameter.stepSize().value()
        );
        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMinValue()));
        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMaxValue()));

        return spinner;
    }

    private DoubleRangeSpinner doubleRangeSpinner(MutableCrossValidatorParameter<Double> parameter) {

        DoubleRangeSpinner spinner = new DoubleRangeSpinner(
                uiHelper.getLabel(parameter.name()),
                parameter.lowerBound().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                parameter.upperBound().value(),
                parameter.value().minValue(),
                parameter.value().maxValue(),
                parameter.stepSize().value()
        );
        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMinValue()));
        spinner.addMinValueChangeListener(e -> parameter.lowerBound().setValue(spinner.getMaxValue()));

        return spinner;
    }

    @Override
    public DoubleSpinner preciseConstantCostParamSpinner() {
        return preciseConstantCostParamSpinner;
    }

    @Override
    public DoubleRangeSpinner intervalConstantCostParamSpinner() {
        return intervalConstantCostParamSpinner;
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
