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

import ru.spbftu.igorbotian.phdapp.common.Range;
import ru.spbftu.igorbotian.phdapp.conf.Configuration;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Реализация интерфейса <code>ClassifierParamsFrameDirector</code>
 *
 * @see ClassifierParamsFrameDirector
 */
public class ClassifierParamsFrameDirectorImpl implements ClassifierParamsFrameDirector {

    private final String CONSTANT_COST_PARAM_ID = "constantCostParam";
    private final String CONSTANT_COST_PARAM_MIN_ID = "constantCostParamMin";
    private final String CONSTANT_COST_PARAM_MAX_ID = "constantCostParamMax";
    private final String CONSTANT_COST_PARAM_STEP_SIZE_ID = "constantCostParamStepSize";

    private final String GAUSSIAN_KERNEL_PARAM_ID = "gaussianKernelParam";
    private final String GAUSSIAN_KERNEL_PARAM_MIN_ID = "gaussianKernelParamMin";
    private final String GAUSSIAN_KERNEL_PARAM_MAX_ID = "gaussianKernelParamMax";
    private final String GAUSSIAN_KERNEL_PARAM_STEP_SIZE_ID = "gaussianKernelParamStepSize";

    private final String SAMPLES_TO_GENERATE_COUNT_ID = "samplesToGenerate";
    private final String SAMPLES_TO_GENERATE_COUNT_STEP_SIZE_ID = "samplesToGenerateStepSize";

    private final String SAMPLE_SIZE_ID = "sampleSize";
    private final String SAMPLE_SIZE_MIN_ID = "sampleSizeMin";
    private final String SAMPLE_SIZE_MAX_ID = "sampleSizeMax";
    private final String SAMPLE_SIZE_STEP_SIZE_ID = "sampleSizeStepSize";

    private final String LEARNING_TRAINING_SETS_SIZE_RATIO_ID = "learningTrainingSetsSizeRatio";
    private final String LEARNING_TRAINING_SETS_SIZE_RATIO_MIN_ID = "learningTrainingSetsSizeRatioMin";
    private final String LEARNING_TRAINING_SETS_SIZE_RATIO_MAX_ID = "learningTrainingSetsSizeRatioMax";
    private final String LEARNING_TRAINING_SETS_SIZE_RATIO_STEP_SIZE_ID = "learningTrainingSetsSizeRatioStepSize";

    private final String PRECISE_INTERVAL_JUDGEMENTS_RATIO_ID = "preciseIntervalJudgementsRatio";
    private final String PRECISE_INTERVAL_JUDGEMENTS_RATIO_MIN_ID = "preciseIntervalJudgementsRatioMin";
    private final String PRECISE_INTERVAL_JUDGEMENTS_RATIO_MAX_ID = "preciseIntervalJudgementsRatioMax";
    private final String PRECISE_INTERVAL_JUDGEMENTS_RATIO_STEP_SIZE_ID = "preciseIntervalJudgementsRatioStepSize";

    /* Характеристика параметров */

    private final double DEFAULT_CONSTANT_COST_PARAM = 100.0;
    private final double DEFAULT_CONSTANT_COST_PARAM_MIN = 0.0;
    private final double DEFAULT_CONSTANT_COST_PARAM_MAX = 200.0;
    private final double DEFAULT_CONSTANT_COST_PARAM_STEP_SIZE = 1.0;
    private final double CONSTANT_COST_PARAM_MAX = 1000000.0;
    private final double CONSTANT_COST_PARAM_MIN = -CONSTANT_COST_PARAM_MAX;

    private final double DEFAULT_GAUSSIAN_KERNEL_PARAM = 0.1;
    private final double DEFAULT_GAUSSIAN_KERNEL_PARAM_MIN = 0.001;
    private final double DEFAULT_GAUSSIAN_KERNEL_PARAM_MAX = 0.1;
    private final double DEFAULT_GAUSSIAN_KERNEL_PARAM_STEP_SIZE = 0.001;
    private final double GAUSSIAN_KERNEL_PARAM_MAX = 1000000.0;
    private final double GAUSSIAN_KERNEL_PARAM_MIN = -GAUSSIAN_KERNEL_PARAM_MAX;

    private final int DEFAULT_SAMPLES_TO_GENERATE_COUNT = 100;
    private final int DEFAULT_SAMPLES_TO_GENERATE_COUNT_STEP_SIZE = 50;
    private final int SAMPLES_TO_GENERATE_COUNT_MIN = 1;
    private final int SAMPLES_TO_GENERATE_COUNT_MAX = Short.MAX_VALUE;

    private final int DEFAULT_SAMPLE_SIZE = 1000;
    private final int DEFAULT_SAMPLE_SIZE_MIN = 100;
    private final int DEFAULT_SAMPLE_SIZE_MAX = 10000;
    private final int DEFAULT_SAMPLE_SIZE_STEP_SIZE = 100;
    private final int SAMPLE_SIZE_MIN = 100;
    private final int SAMPLE_SIZE_MAX = Short.MAX_VALUE;

    private final int DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO = 30;
    private final int DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO_MIN = 5;
    private final int DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO_MAX = 100;
    private final int DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO_STEP_SIZE = 5;
    private final int LEARNING_TRAINING_SETS_SIZE_RATIO_MIN = 1;
    private final int LEARNING_TRAINING_SETS_SIZE_RATIO_MAX = 100;

    private final int DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO = 40;
    private final int DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO_MIN = 0;
    private final int DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO_MAX = 100;
    private final int DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO_STEP_SIZE = 5;
    private final int PRECISE_INTERVAL_JUDGEMENTS_RATIO_MIN = 0;
    private final int PRECISE_INTERVAL_JUDGEMENTS_RATIO_MAX = 100;

    private final Configuration config;

    public ClassifierParamsFrameDirectorImpl(Configuration config) {
        this.config = Objects.requireNonNull(config);
    }

    private double get(String param, double defaultValue) {
        return this.getValue(config::hasParam, config::getDouble, config::setDouble, param, defaultValue);
    }

    private int get(String param, int defaultValue) {
        return this.getValue(config::hasParam, config::getInt, config::setInt, param, defaultValue);
    }

    private <T> T getValue(Function<String, Boolean> checker, Function<String, T> getter,
                           BiConsumer<String, T> setter, String param, T defaultValue) {
        if(checker.apply(param)) {
            return getter.apply(param);
        }

        setter.accept(param, defaultValue);
        return defaultValue;
    }

    private void set(String param, double value) {
        config.setDouble(param, value);
    }

    private void set(String param, int value) {
        config.setDouble(param, value);
    }

    /* ПАРАМЕТР ПОСТОЯННОЙ СТОИМОСТИ */

    @Override
    public double constantCostParameter() {
        return get(CONSTANT_COST_PARAM_ID, DEFAULT_CONSTANT_COST_PARAM);
    }

    @Override
    public void setConstantCostParameter(double value) {
        set(CONSTANT_COST_PARAM_ID, value);
    }

    @Override
    public double constantCostParameterMin() {
        return CONSTANT_COST_PARAM_MIN;
    }

    @Override
    public double constantCostParameterMax() {
        return CONSTANT_COST_PARAM_MAX;
    }

    @Override
    public Range<Double> intervalConstantCostParameter() {
        return new Range<>(get(CONSTANT_COST_PARAM_MIN_ID, DEFAULT_CONSTANT_COST_PARAM_MIN),
                get(CONSTANT_COST_PARAM_MAX_ID, DEFAULT_CONSTANT_COST_PARAM_MAX),
                Double::compare);
    }

    @Override
    public void setIntervalConstantCostParameter(Range<Double> value) {
        set(CONSTANT_COST_PARAM_MIN_ID, value.lowerBound());
        set(CONSTANT_COST_PARAM_MAX_ID, value.upperBound());
    }

    @Override
    public double intervalConstantCostParameterStepSize() {
        return get(CONSTANT_COST_PARAM_STEP_SIZE_ID, DEFAULT_CONSTANT_COST_PARAM_STEP_SIZE);
    }

    @Override
    public void setIntervalConstantCostParameterStepSize(double stepSize) {
        set(CONSTANT_COST_PARAM_STEP_SIZE_ID, stepSize);
    }

    /* ПАРАМЕТР ГАУССОВА ЯДРА */

    @Override
    public double gaussianKernelParameter() {
        return get(GAUSSIAN_KERNEL_PARAM_ID, DEFAULT_GAUSSIAN_KERNEL_PARAM);
    }

    @Override
    public void setGaussianKernelParameter(double value) {
        set(GAUSSIAN_KERNEL_PARAM_ID, value);
    }

    @Override
    public double gaussianKernelParameterMin() {
        return GAUSSIAN_KERNEL_PARAM_MIN;
    }

    @Override
    public double gaussianKernelParameterMax() {
        return GAUSSIAN_KERNEL_PARAM_MAX;
    }

    @Override
    public Range<Double> intervalGaussianKernelParameter() {
        return new Range<>(get(GAUSSIAN_KERNEL_PARAM_MIN_ID, DEFAULT_GAUSSIAN_KERNEL_PARAM_MIN),
                get(GAUSSIAN_KERNEL_PARAM_MAX_ID, DEFAULT_GAUSSIAN_KERNEL_PARAM_MAX),
                Double::compare);
    }

    @Override
    public void setIntervalGaussianKernelParameter(Range<Double> value) {
        set(GAUSSIAN_KERNEL_PARAM_MIN_ID, value.lowerBound());
        set(GAUSSIAN_KERNEL_PARAM_MAX_ID, value.upperBound());
    }

    @Override
    public double intervalGaussianKernelParameterStepSize() {
        return get(GAUSSIAN_KERNEL_PARAM_STEP_SIZE_ID, DEFAULT_GAUSSIAN_KERNEL_PARAM_STEP_SIZE);
    }

    @Override
    public void setIntervalGaussianKernelParameterStepSize(double stepSize) {
        set(GAUSSIAN_KERNEL_PARAM_STEP_SIZE_ID, stepSize);
    }

    /* КОЛИЧЕСТВО СГЕНЕРИРОВАННЫХ ВЫБОРОК */

    @Override
    public int samplesToGenerateCount() {
        return get(SAMPLES_TO_GENERATE_COUNT_ID, DEFAULT_SAMPLES_TO_GENERATE_COUNT);
    }

    @Override
    public void setSamplesToGenerateCount(int count) {
        set(SAMPLES_TO_GENERATE_COUNT_ID, count);
    }

    @Override
    public int samplesToGenerateCountMin() {
        return SAMPLES_TO_GENERATE_COUNT_MIN;
    }

    @Override
    public int samplesToGenerateCountMax() {
        return SAMPLES_TO_GENERATE_COUNT_MAX;
    }

    @Override
    public int intervalSamplesToGenerateCountStepSize() {
        return get(SAMPLES_TO_GENERATE_COUNT_STEP_SIZE_ID, DEFAULT_SAMPLES_TO_GENERATE_COUNT_STEP_SIZE);
    }

    @Override
    public void setIntervalSamplesToGenerateCountStepSize(int stepSize) {
        set(SAMPLES_TO_GENERATE_COUNT_STEP_SIZE_ID, stepSize);
    }

    /* РАЗМЕР ВЫБОРКИ */

    @Override
    public int sampleSize() {
        return get(SAMPLE_SIZE_ID, DEFAULT_SAMPLE_SIZE);
    }

    @Override
    public void setSampleSize(int size) {
        set(SAMPLE_SIZE_ID, size);
    }

    @Override
    public int sampleSizeMin() {
        return SAMPLE_SIZE_MIN;
    }

    @Override
    public int sampleSizeMax() {
        return SAMPLE_SIZE_MAX;
    }

    @Override
    public Range<Integer> intervalSampleSize() {
        return new Range<>(get(SAMPLE_SIZE_MIN_ID, DEFAULT_SAMPLE_SIZE_MIN),
                get(SAMPLE_SIZE_MAX_ID, DEFAULT_SAMPLE_SIZE_MAX),
                Integer::compare);
    }

    @Override
    public void setIntervalSampleSize(Range<Integer> sampleSize) {
        set(SAMPLE_SIZE_MIN_ID, sampleSize.lowerBound());
        set(SAMPLE_SIZE_MAX_ID, sampleSize.upperBound());
    }

    @Override
    public int intervalSampleSizeStepSize() {
        return get(SAMPLE_SIZE_STEP_SIZE_ID, DEFAULT_SAMPLE_SIZE_STEP_SIZE);
    }

    @Override
    public void setIntervalSampleSizeStepSize(int stepSize) {
        set(SAMPLE_SIZE_STEP_SIZE_ID, stepSize);
    }

    /* ПРОЦЕНТНОЕ СООТНОШЕНИЕ ЭЛЕМЕНТОВ ВХОДЯЩИХ В ОБУЧАЮЩУЮ И ТЕСТИРУЮЩУЮ ВЫБОРКИ */

    @Override
    public int learningTrainingSetsSizeRatio() {
        return get(LEARNING_TRAINING_SETS_SIZE_RATIO_ID, DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO);
    }

    @Override
    public void setLearningTrainingSetsSizeRatio(int ratio) {
        set(LEARNING_TRAINING_SETS_SIZE_RATIO_ID, ratio);
    }

    @Override
    public int learningTrainingSetsSizeRatioMin() {
        return LEARNING_TRAINING_SETS_SIZE_RATIO_MIN;
    }

    @Override
    public int learningTrainingSetsSizeRatioMax() {
        return LEARNING_TRAINING_SETS_SIZE_RATIO_MAX;
    }

    @Override
    public void setIntervalLearningTrainingSetsSizeRatio(Range<Integer> ratio) {
        set(LEARNING_TRAINING_SETS_SIZE_RATIO_MIN_ID, ratio.lowerBound());
        set(LEARNING_TRAINING_SETS_SIZE_RATIO_MAX_ID, ratio.upperBound());
    }

    @Override
    public Range<Integer> intervalLearningTrainingSetsSizeRatio() {
        return new Range<>(get(LEARNING_TRAINING_SETS_SIZE_RATIO_MIN_ID, DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO_MIN),
                get(LEARNING_TRAINING_SETS_SIZE_RATIO_MAX_ID, DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO_MAX),
                Integer::compare);
    }

    @Override
    public int intervalLearningTrainingSetsSizeRatioStepSize() {
        return get(LEARNING_TRAINING_SETS_SIZE_RATIO_STEP_SIZE_ID, DEFAULT_LEARNING_TRAINING_SETS_SIZE_RATIO_STEP_SIZE);
    }

    @Override
    public void setIntervalLearningTrainingSetsSizeRatioStepSize(int stepSize) {
        set(LEARNING_TRAINING_SETS_SIZE_RATIO_STEP_SIZE_ID, stepSize);
    }

    /* ПРОЦЕНТНОЕ СООТНОШЕНИЕ ТОЧНЫХ И ИНТЕРВАЛЬНЫХ ПРЕДПОЧТЕНИЙ В ОБУЧАЮЩЕЙ ВЫБОРКЕ */

    @Override
    public int preciseIntervalJudgementsRatio() {
        return get(PRECISE_INTERVAL_JUDGEMENTS_RATIO_ID, DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO);
    }

    @Override
    public void setPreciseIntervalJudgementsRatio(int ratio) {
        set(PRECISE_INTERVAL_JUDGEMENTS_RATIO_ID, ratio);
    }

    @Override
    public int preciseIntervalJudgementsRatioMin() {
        return PRECISE_INTERVAL_JUDGEMENTS_RATIO_MIN;
    }

    @Override
    public int preciseIntervalJudgementsRatioMax() {
        return PRECISE_INTERVAL_JUDGEMENTS_RATIO_MAX;
    }

    @Override
    public void setIntervalPreciseIntervalJudgementsRatio(Range<Integer> ratio) {
        set(PRECISE_INTERVAL_JUDGEMENTS_RATIO_MIN_ID, ratio.lowerBound());
        set(PRECISE_INTERVAL_JUDGEMENTS_RATIO_MAX_ID, ratio.upperBound());
    }

    @Override
    public Range<Integer> intervalPreciseIntervalJudgementsRatio() {
        return new Range<>(get(PRECISE_INTERVAL_JUDGEMENTS_RATIO_MIN_ID, DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO_MIN),
                get(PRECISE_INTERVAL_JUDGEMENTS_RATIO_MAX_ID, DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO_MAX),
                Integer::compare);
    }

    @Override
    public int intervalPreciseIntervalJudgementsRatioStepSize() {
        return get(PRECISE_INTERVAL_JUDGEMENTS_RATIO_STEP_SIZE_ID, DEFAULT_PRECISE_INTERVAL_JUDGEMENTS_RATIO_STEP_SIZE);
    }

    @Override
    public void setIntervalPreciseIntervalJudgementsRatioStepSize(int stepSize) {
        set(PRECISE_INTERVAL_JUDGEMENTS_RATIO_STEP_SIZE_ID, stepSize);
    }
}
