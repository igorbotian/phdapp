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
import ru.spbftu.igorbotian.phdapp.common.Range;
import ru.spbftu.igorbotian.phdapp.ui.common.ClassifierParamsFrameDirector;
import ru.spbftu.igorbotian.phdapp.ui.common.UIHelper;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.DoubleRangeSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.DoubleSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.IntegerRangeSpinner;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.IntegerSpinner;

import javax.swing.event.ChangeListener;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Вспомогательный класс для формирования виджетов для задания параметров классификации.
 * Введённые пользователем значения сохраняются по завершению работы программы
 * и задаются по умолчанию при следующей работе приложения.
 */
@Singleton
class ClassifierParamsWidgetsImpl implements ClassifierParamsWidgets {

    /* Идентификаторы параметров (каждый их них используется для получения перевода и значения из конфигурации */

    private final String CONSTANT_COST_PARAM_LABEL = "constantCostParameter";
    private final String GAUSSIAN_KERNEL_PARAM_LABEL = "gaussianKernelParameter";
    private final String SAMPLES_TO_GENERATE_COUNT_LABEL = "samplesToGenerateCount";
    private final String SAMPLE_SIZE_LABEL = "sampleSize";
    private final String LEARNING_TESTING_SETS_SIZE_RATIO_LABEL = "learningTestingSetsSizeRatio";
    private final String PRECISE_INTERVAL_JUDGEMENTS_RATIO_LABEL = "preciseIntervalJudgementsRatio";

    /* Внешние ресурсы */

    private final UIHelper uiHelper;

    /* Виджеты */

    private DoubleSpinner preciseConstantCostParamSpinner;
    private DoubleRangeSpinner intervalConstantCostParamSpinner;
    private DoubleSpinner preciseGaussianKernelParamSpinner;
    private DoubleRangeSpinner intervalGaussianKernelParamSpinner;
    private IntegerSpinner samplesToGenerateCountSpinner;
    private IntegerSpinner preciseSampleSizeSpinner;
    private IntegerRangeSpinner intervalSampleSizeSpinner;
    private IntegerSpinner precisePercentOfJudgedSampleItemsSpinner;
    private IntegerRangeSpinner intervalPercentOfJudgedSampleItemsSpinner;
    private IntegerSpinner precisePreciseIntervalJudgementsRatioSpinner;
    private IntegerRangeSpinner intervalPreciseIntervalJudgementsRatioSpinner;

    @Inject
    public ClassifierParamsWidgetsImpl(UIHelper uiHelper) {
        this.uiHelper = Objects.requireNonNull(uiHelper);
        ClassifierParamsFrameDirector director = uiHelper.classifierParamsFrameDirector();

        preciseConstantCostParamSpinner = preciseDoubleSpinner(
                CONSTANT_COST_PARAM_LABEL,
                director::constantCostParameter,
                director::setConstantCostParameter,
                director::constantCostParameterMin,
                director::constantCostParameterMax,
                director::intervalConstantCostParameterStepSize
        );

        intervalConstantCostParamSpinner = doubleRangeSpinner(
                CONSTANT_COST_PARAM_LABEL,
                director::intervalConstantCostParameter,
                director::setIntervalConstantCostParameter,
                director::constantCostParameterMin,
                director::constantCostParameterMax,
                director::intervalConstantCostParameterStepSize
        );

        preciseGaussianKernelParamSpinner = preciseDoubleSpinner(
                GAUSSIAN_KERNEL_PARAM_LABEL,
                director::gaussianKernelParameter,
                director::setGaussianKernelParameter,
                director::gaussianKernelParameterMin,
                director::gaussianKernelParameterMax,
                director::intervalGaussianKernelParameterStepSize
        );

        intervalGaussianKernelParamSpinner = doubleRangeSpinner(
                GAUSSIAN_KERNEL_PARAM_LABEL,
                director::intervalGaussianKernelParameter,
                director::setIntervalGaussianKernelParameter,
                director::gaussianKernelParameterMin,
                director::gaussianKernelParameterMax,
                director::intervalGaussianKernelParameterStepSize
        );

        samplesToGenerateCountSpinner = preciseIntegerSpinner(
                SAMPLES_TO_GENERATE_COUNT_LABEL,
                director::samplesToGenerateCount,
                director::setSamplesToGenerateCount,
                director::samplesToGenerateCountMin,
                director::samplesToGenerateCountMax,
                director::intervalSamplesToGenerateCountStepSize
        );

        preciseSampleSizeSpinner = preciseIntegerSpinner(
                SAMPLE_SIZE_LABEL,
                director::sampleSize,
                director::setSampleSize,
                director::sampleSizeMin,
                director::sampleSizeMax,
                director::intervalSampleSizeStepSize
        );

        intervalSampleSizeSpinner = integerRangeSpinner(
                SAMPLE_SIZE_LABEL,
                director::intervalSampleSize,
                director::setIntervalSampleSize,
                director::sampleSizeMin,
                director::sampleSizeMax,
                director::intervalSampleSizeStepSize
        );

        precisePercentOfJudgedSampleItemsSpinner = preciseIntegerSpinner(
                LEARNING_TESTING_SETS_SIZE_RATIO_LABEL,
                director::learningTrainingSetsSizeRatio,
                director::setLearningTrainingSetsSizeRatio,
                director::learningTrainingSetsSizeRatioMin,
                director::learningTrainingSetsSizeRatioMax,
                director::intervalLearningTrainingSetsSizeRatioStepSize
        );

        intervalPercentOfJudgedSampleItemsSpinner = integerRangeSpinner(
                LEARNING_TESTING_SETS_SIZE_RATIO_LABEL,
                director::intervalLearningTrainingSetsSizeRatio,
                director::setIntervalLearningTrainingSetsSizeRatio,
                director::learningTrainingSetsSizeRatioMin,
                director::learningTrainingSetsSizeRatioMax,
                director::intervalLearningTrainingSetsSizeRatioStepSize
        );

        precisePreciseIntervalJudgementsRatioSpinner = preciseIntegerSpinner(
                PRECISE_INTERVAL_JUDGEMENTS_RATIO_LABEL,
                director::preciseIntervalJudgementsRatio,
                director::setPreciseIntervalJudgementsRatio,
                director::preciseIntervalJudgementsRatioMin,
                director::preciseIntervalJudgementsRatioMax,
                director::intervalPreciseIntervalJudgementsRatioStepSize
        );

        intervalPreciseIntervalJudgementsRatioSpinner = integerRangeSpinner(
                PRECISE_INTERVAL_JUDGEMENTS_RATIO_LABEL,
                director::intervalPreciseIntervalJudgementsRatio,
                director::setIntervalPreciseIntervalJudgementsRatio,
                director::preciseIntervalJudgementsRatioMin,
                director::preciseIntervalJudgementsRatioMax,
                director::intervalPreciseIntervalJudgementsRatioStepSize
        );
    }

    private IntegerSpinner preciseIntegerSpinner(String label, Supplier<Integer> getter, Consumer<Integer> setter,
                                                 Supplier<Integer> minGetter, Supplier<Integer> maxGetter,
                                                 Supplier<Integer> stepSizeGetter) {

        IntegerSpinner spinner = new IntegerSpinner(uiHelper.getLabel(label), getter.get(), minGetter.get(),
                maxGetter.get(), stepSizeGetter.get());
        spinner.addChangeListener(e -> setter.accept(spinner.getValue()));
        return spinner;
    }

    private DoubleSpinner preciseDoubleSpinner(String label, Supplier<Double> getter, Consumer<Double> setter,
                                               Supplier<Double> minGetter, Supplier<Double> maxGetter,
                                               Supplier<Double> stepSizeGetter) {

        DoubleSpinner spinner = new DoubleSpinner(uiHelper.getLabel(label), getter.get(), minGetter.get(),
                maxGetter.get(), stepSizeGetter.get());
        spinner.addChangeListener(e -> setter.accept(spinner.getValue()));
        return spinner;
    }

    private IntegerRangeSpinner integerRangeSpinner(String label,
                                                    Supplier<Range<Integer>> boundGetter,
                                                    Consumer<Range<Integer>> boundSetter,
                                                    Supplier<Integer> minGetter, Supplier<Integer> maxGetter,
                                                    Supplier<Integer> stepSizeGetter) {

        IntegerRangeSpinner spinner = new IntegerRangeSpinner(uiHelper.getLabel(label), boundGetter.get().lowerBound(),
                minGetter.get(), maxGetter.get(), boundGetter.get().upperBound(),
                minGetter.get(), maxGetter.get(), stepSizeGetter.get());
        ChangeListener changeListener = (e -> boundSetter.accept(
                new Range<>(spinner.getMinValue(), spinner.getMaxValue(), Integer::compare))
        );

        spinner.addMinValueChangeListener(changeListener);
        spinner.addMinValueChangeListener(changeListener);

        return spinner;
    }

    private DoubleRangeSpinner doubleRangeSpinner(String label,
                                                  Supplier<Range<Double>> boundGetter,
                                                  Consumer<Range<Double>> boundSetter,
                                                  Supplier<Double> minGetter, Supplier<Double> maxGetter,
                                                  Supplier<Double> stepSizeGetter) {

        DoubleRangeSpinner spinner = new DoubleRangeSpinner(uiHelper.getLabel(label), boundGetter.get().lowerBound(),
                minGetter.get(), maxGetter.get(), boundGetter.get().upperBound(),
                minGetter.get(), maxGetter.get(), stepSizeGetter.get());

        ChangeListener changeListener = (e -> boundSetter.accept(
                new Range<>(spinner.getMinValue(), spinner.getMaxValue(), Double::compare))
        );

        spinner.addMinValueChangeListener(changeListener);
        spinner.addMinValueChangeListener(changeListener);

        return spinner;
    }

    public DoubleSpinner preciseCParamSpinner() {
        return preciseConstantCostParamSpinner;
    }

    public DoubleRangeSpinner intervalCParamSpinner() {
        return intervalConstantCostParamSpinner;
    }

    public DoubleSpinner preciseSigmaParamSpinner() {
        return preciseGaussianKernelParamSpinner;
    }

    public DoubleRangeSpinner intervalSigmaParamSpinner() {
        return intervalGaussianKernelParamSpinner;
    }

    public IntegerSpinner preciseNumberOfIterationsSpinner() {
        return samplesToGenerateCountSpinner;
    }

    public IntegerSpinner preciseSampleSizeSpinner() {
        return preciseSampleSizeSpinner;
    }

    public IntegerRangeSpinner intervalSampleSizeSpinner() {
        return intervalSampleSizeSpinner;
    }

    public IntegerSpinner precisePercentOfJudgedSampleItemsSpinner() {
        return precisePercentOfJudgedSampleItemsSpinner;
    }

    public IntegerRangeSpinner intervalPercentOfJudgedSampleItemsSpinner() {
        return intervalPercentOfJudgedSampleItemsSpinner;
    }

    public IntegerSpinner precisePreciseIntervalJudgedSampleItemsRatioSpinner() {
        return precisePreciseIntervalJudgementsRatioSpinner;
    }

    public IntegerRangeSpinner intervalPreciseIntervalJudgedSampleItemsRatioSpinner() {
        return intervalPreciseIntervalJudgementsRatioSpinner;
    }
}
