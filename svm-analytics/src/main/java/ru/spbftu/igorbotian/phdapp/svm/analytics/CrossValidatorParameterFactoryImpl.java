package ru.spbftu.igorbotian.phdapp.svm.analytics;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Реализация набора параметров средства кросс-валидации классификатора
 */
class CrossValidatorParameterFactoryImpl implements CrossValidatorParameterFactory {


    @Override
    public CrossValidatorParameter<Double> newConstantCostParameter() {
        return newConstantCostParameter(
                CONSTANT_COST_PARAM_DEFAULT_VALUE,
                CONSTANT_COST_PARAM_DEFAULT_LOWER_BOUND,
                CONSTANT_COST_PARAM_DEFAULT_UPPER_BOUND,
                CONSTANT_COST_PARAM_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Double> newConstantCostParameter(double value, double lowerBound,
                                                                     double upperBound, double stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(CONSTANT_COST_PARAM_ID, Double.class,
                value, lowerBound, upperBound, CONSTANT_COST_PARAM_MIN_VALUE, CONSTANT_COST_PARAM_MAX_VALUE,
                stepSize, Double::compare);
    }

    @Override
    public CrossValidatorParameter<Double> newGaussianKernelParameter() {
        return newGaussianKernelParameter(
                GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE,
                GAUSSIAN_KERNEL_PARAM_DEFAULT_LOWER_BOUND,
                GAUSSIAN_KERNEL_PARAM_DEFAULT_UPPER_BOUND,
                GAUSSIAN_KERNEL_PARAM_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Double> newGaussianKernelParameter(double value, double lowerBound,
                                                                      double upperBound, double stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(GAUSSIAN_KERNEL_PARAM_ID, Double.class,
                value, lowerBound, upperBound, GAUSSIAN_KERNEL_PARAM_MIN_VALUE, GAUSSIAN_KERNEL_PARAM_MAX_VALUE,
                stepSize, Double::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> newSamplesToGenerateCount() {
        return newSamplesToGenerateCount(
                SAMPLES_TO_GENERATE_COUNT_DEFAULT_VALUE,
                SAMPLES_TO_GENERATE_COUNT_DEFAULT_LOWER_BOUND,
                SAMPLES_TO_GENERATE_COUNT_DEFAULT_UPPER_BOUND,
                SAMPLES_TO_GENERATE_COUNT_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newSamplesToGenerateCount(int value, int lowerBound,
                                                                      int upperBound, int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(SAMPLES_TO_GENERATE_COUNT_ID, Integer.class,
                value, lowerBound, upperBound, SAMPLES_TO_GENERATE_COUNT_MIN, SAMPLES_TO_GENERATE_COUNT_MAX,
                stepSize, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> newSampleSize() {
        return newSampleSize(
                SAMPLE_SIZE_DEFAULT_VALUE,
                SAMPLE_SIZE_DEFAULT_LOWER_BOUND,
                SAMPLE_SIZE_DEFAULT_UPPER_BOUND,
                SAMPLE_SIZE_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newSampleSize(int value, int lowerBound, int upperBound,
                                                          int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(SAMPLE_SIZE_ID, Integer.class,
                value, lowerBound, upperBound, SAMPLE_SIZE_MIN, SAMPLE_SIZE_MAX,
                stepSize, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatio() {
        return newTrainingTestingSetsSizeRatio(
                TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_VALUE,
                TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_LOWER_BOUND,
                TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_UPPER_BOUND,
                TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatio(int value, int lowerBound,
                                                                            int upperBound, int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(TRAINING_TESTING_SETS_SIZE_RATIO_ID, Integer.class,
                value, lowerBound, upperBound, TRAINING_TESTING_SETS_SIZE_RATIO_MIN,
                TRAINING_TESTING_SETS_SIZE_RATIO_MAX, stepSize, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatio() {
        return newPreciseIntervalJudgmentsCountRatio(
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_VALUE,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_LOWER_BOUND,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_UPPER_BOUND,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatio(int value, int lowerBound,
                                                                                  int upperBound, int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_ID, Integer.class,
                value, lowerBound, upperBound, PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MIN,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MAX, stepSize, Integer::compare);
    }

    @Override
    public Set<CrossValidatorParameter<?>> defaultValues() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                newConstantCostParameter(),
                newGaussianKernelParameter(),
                newSamplesToGenerateCount(),
                newSampleSize(),
                newTrainingTestingSetsSizeRatio(),
                newPreciseIntervalJudgmentsCountRatio()
        )));
    }
}
