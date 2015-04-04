package ru.spbftu.igorbotian.phdapp.svm.validation;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация набора параметров средства кросс-валидации классификатора
 */
class CrossValidatorParameterFactoryImpl implements CrossValidatorParameterFactory {


    @Override
    public CrossValidatorParameter<Integer> maxJudgementGroupSize() {
        return maxJudgementGroupSize(DEFAULT_MAX_JUDGEMENT_GROUP_SIZE_VALUE);
    }

    @Override
    public CrossValidatorParameter<Integer> maxJudgementGroupSize(int value) {
        return new DefaultCrossValidatorParameterImpl<>(MAX_JUDGEMENT_GROUP_SIZE_PARAM_ID, Integer.class,
                value, 0, Integer.MAX_VALUE, value, value, 1, 1, 1, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Double> penaltyParameter() {
        return penaltyParameter(PENALTY_PARAM_DEFAULT_VALUE);
    }

    @Override
    public CrossValidatorParameter<Double> penaltyParameter(double value) {
        return penaltyParameter(
                value,
                PENALTY_PARAM_DEFAULT_LOWER_BOUND,
                PENALTY_PARAM_DEFAULT_UPPER_BOUND,
                PENALTY_PARAM_DEFAULT_STEP_SIZE,
                PENALTY_PARAM_STEP_SIZE_MIN,
                PENALTY_PARAM_STEP_SIZE_MAX
        );
    }

    @Override
    public CrossValidatorParameter<Double> penaltyParameter(double value, double lowerBound,
                                                            double upperBound, double stepSize,
                                                            double stepSizeMin, double stepSizeMax) {
        return new DefaultCrossValidatorParameterImpl<>(PENALTY_PARAM_ID, Double.class,
                value, lowerBound, upperBound, PENALTY_PARAM_MIN_VALUE, PENALTY_PARAM_MAX_VALUE,
                stepSize, stepSizeMin, stepSizeMax, Double::compare);
    }

    @Override
    public CrossValidatorParameter<Double> gaussianKernelParameter() {
        return gaussianKernelParameter(GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE);
    }

    @Override
    public CrossValidatorParameter<Double> gaussianKernelParameter(double value) {
        return gaussianKernelParameter(
                value,
                GAUSSIAN_KERNEL_PARAM_DEFAULT_LOWER_BOUND,
                GAUSSIAN_KERNEL_PARAM_DEFAULT_UPPER_BOUND,
                GAUSSIAN_KERNEL_PARAM_DEFAULT_STEP_SIZE,
                GAUSSIAN_KERNEL_PARAM_STEP_SIZE_MIN,
                GAUSSIAN_KERNEL_PARAM_STEP_SIZE_MAX
        );
    }

    @Override
    public CrossValidatorParameter<Double> gaussianKernelParameter(double value, double lowerBound,
                                                                   double upperBound, double stepSize,
                                                                   double stepSizeMin, double stepSizeMax) {
        return new DefaultCrossValidatorParameterImpl<>(GAUSSIAN_KERNEL_PARAM_ID, Double.class,
                value, lowerBound, upperBound, GAUSSIAN_KERNEL_PARAM_MIN_VALUE, GAUSSIAN_KERNEL_PARAM_MAX_VALUE,
                stepSize, stepSizeMin, stepSizeMax, Double::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> samplesToGenerateCount() {
        return samplesToGenerateCount(SAMPLES_TO_GENERATE_COUNT_DEFAULT_VALUE);
    }

    @Override
    public CrossValidatorParameter<Integer> samplesToGenerateCount(int value) {
        return samplesToGenerateCount(
                value,
                SAMPLES_TO_GENERATE_COUNT_DEFAULT_LOWER_BOUND,
                SAMPLES_TO_GENERATE_COUNT_DEFAULT_UPPER_BOUND,
                SAMPLES_TO_GENERATE_COUNT_DEFAULT_STEP_SIZE,
                SAMPLES_TO_GENERATE_COUNT_STEP_SIZE_MIN,
                SAMPLES_TO_GENERATE_COUNT_STEP_SIZE_MAX
        );
    }

    @Override
    public CrossValidatorParameter<Integer> samplesToGenerateCount(int value, int lowerBound,
                                                                   int upperBound, int stepSize,
                                                                   int stepSizeMin, int stepSizeMax) {
        return new DefaultCrossValidatorParameterImpl<>(SAMPLES_TO_GENERATE_COUNT_ID, Integer.class,
                value, lowerBound, upperBound, SAMPLES_TO_GENERATE_COUNT_MIN, SAMPLES_TO_GENERATE_COUNT_MAX,
                stepSize, stepSizeMin, stepSizeMax, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> sampleSize() {
        return sampleSize(SAMPLE_SIZE_DEFAULT_VALUE);
    }

    @Override
    public CrossValidatorParameter<Integer> sampleSize(int value) {
        return sampleSize(
                value,
                SAMPLE_SIZE_DEFAULT_LOWER_BOUND,
                SAMPLE_SIZE_DEFAULT_UPPER_BOUND,
                SAMPLE_SIZE_DEFAULT_STEP_SIZE,
                SAMPLE_SIZE_STEP_SIZE_MIN,
                SAMPLE_SIZE_STEP_SIZE_MAX
        );
    }

    @Override
    public CrossValidatorParameter<Integer> sampleSize(int value, int lowerBound, int upperBound,
                                                       int stepSize, int stepSizeMin, int stepSizeMax) {
        return new DefaultCrossValidatorParameterImpl<>(SAMPLE_SIZE_ID, Integer.class,
                value, lowerBound, upperBound, SAMPLE_SIZE_MIN, SAMPLE_SIZE_MAX,
                stepSize, stepSizeMin, stepSizeMax, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio() {
        return trainingTestingSetsSizeRatio(TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_VALUE);
    }

    @Override
    public CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio(int value) {
        return trainingTestingSetsSizeRatio(
                value,
                TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_LOWER_BOUND,
                TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_UPPER_BOUND,
                TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_STEP_SIZE,
                TRAINING_TESTING_SETS_SIZE_RATIO_STEP_SIZE_MIN,
                TRAINING_TESTING_SETS_SIZE_RATIO_STEP_SIZE_MAX
        );
    }

    @Override
    public CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio(int value, int lowerBound,
                                                                         int upperBound, int stepSize,
                                                                         int stepSizeMin, int stepSizeMax) {
        return new DefaultCrossValidatorParameterImpl<>(TRAINING_TESTING_SETS_SIZE_RATIO_ID, Integer.class,
                value, lowerBound, upperBound, TRAINING_TESTING_SETS_SIZE_RATIO_MIN,
                TRAINING_TESTING_SETS_SIZE_RATIO_MAX, stepSize, stepSizeMin, stepSizeMax, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio() {
        return preciseIntervalJudgmentsCountRatio(PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_VALUE);
    }

    @Override
    public CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio(int value) {
        return preciseIntervalJudgmentsCountRatio(
                value,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_LOWER_BOUND,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_UPPER_BOUND,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_STEP_SIZE,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_STEP_SIZE_MIN,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_STEP_SIZE_MAX
        );
    }

    @Override
    public CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio(int value, int lowerBound,
                                                                               int upperBound, int stepSize,
                                                                               int stepSizeMin, int stepSizeMax) {
        return new DefaultCrossValidatorParameterImpl<>(PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_ID, Integer.class,
                value, lowerBound, upperBound, PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MIN,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MAX, stepSize, stepSizeMin, stepSizeMax, Integer::compare);
    }

    @Override
    public Set<CrossValidatorParameter<?>> defaultValues() {
        return Stream.of(
                penaltyParameter(),
                gaussianKernelParameter(),
                samplesToGenerateCount(),
                sampleSize(),
                trainingTestingSetsSizeRatio(),
                preciseIntervalJudgmentsCountRatio()
        ).collect(Collectors.toSet());
    }
}
