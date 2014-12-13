package ru.spbftu.igorbotian.phdapp.svm.analytics;

/**
 * Реализация набора параметров средства кросс-валидации классификатора
 */
class CrossValidatorParameterFactoryImpl implements CrossValidatorParameterFactory {


    @Override
    public CrossValidatorParameter<Integer> newSamplesToGenerateCountParameter() {
        return newSamplesToGenerateCountParameter(
                SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_VALUE,
                SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_LOWER_BOUND,
                SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_UPPER_BOUND,
                SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newSamplesToGenerateCountParameter(int value, int lowerBound,
                                                                               int upperBound, int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(SAMPLES_TO_GENERATE_COUNT_PARAM_ID, Integer.class,
                value, lowerBound, upperBound, SAMPLES_TO_GENERATE_COUNT_PARAM_MIN, SAMPLES_TO_GENERATE_COUNT_PARAM_MAX,
                stepSize, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> newSampleSizeParameter() {
        return newSamplesToGenerateCountParameter(
                SAMPLE_SIZE_PARAM_DEFAULT_VALUE,
                SAMPLE_SIZE_PARAM_DEFAULT_LOWER_BOUND,
                SAMPLE_SIZE_PARAM_DEFAULT_UPPER_BOUND,
                SAMPLE_SIZE_PARAM_PARAM_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newSampleSizeParameter(int value, int lowerBound, int upperBound,
                                                                   int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(SAMPLE_SIZE_PARAM_ID, Integer.class,
                value, lowerBound, upperBound, SAMPLE_SIZE_PARAM_MIN, SAMPLE_SIZE_PARAM_MAX,
                stepSize, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatioParameter() {
        return newSamplesToGenerateCountParameter(
                TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_VALUE,
                TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_LOWER_BOUND,
                TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_UPPER_BOUND,
                TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatioParameter(int value, int lowerBound,
                                                                                     int upperBound, int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_ID, Integer.class,
                value, lowerBound, upperBound, TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_MIN,
                TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_MAX, stepSize, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatioParameter() {
        return newSamplesToGenerateCountParameter(
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_VALUE,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_LOWER_BOUND,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_UPPER_BOUND,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_STEP_SIZE
        );
    }

    @Override
    public CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatioParameter(int value, int lowerBound,
                                                                                           int upperBound, int stepSize) {
        return new DefaultCrossValidatorParameterImpl<>(PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_ID, Integer.class,
                value, lowerBound, upperBound, PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_MIN,
                PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_MAX, stepSize, Integer::compare);
    }
}
