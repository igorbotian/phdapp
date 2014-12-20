package ru.spbftu.igorbotian.phdapp.svm.validation;

import java.util.*;

/**
 * Фабрика параметров средства кросс-валидации с учётом заданных значений
 */
class SpecificCrossValidatorParameterFactory implements CrossValidatorParameterFactory {

    /**
     * Исходная фабрика параметров средства кросс-валидации
     */
    private final CrossValidatorParameterFactory parameterFactory;

    /**
     * Ассоциативный массив, содержащий все параметры средства кросс-валидации с учётом заданных изменений
     */
    private final Map<String, CrossValidatorParameter<?>> params = new HashMap<>();

    public SpecificCrossValidatorParameterFactory(CrossValidatorParameterFactory parameterFactory,
                                                Set<? extends CrossValidatorParameter<?>> specificValues) {
        this.parameterFactory = Objects.requireNonNull(parameterFactory);

        for(CrossValidatorParameter<?> param : Objects.requireNonNull(specificValues)) {
            params.put(param.name(), param);
        }

        for(CrossValidatorParameter<?> param : parameterFactory.defaultValues()) {
            if(!params.containsKey(param.name())) {
                params.put(param.name(), param);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> CrossValidatorParameter<T> getParamById(String id) {
        return (CrossValidatorParameter<T>) params.get(id);
    }

    @Override
    public CrossValidatorParameter<Double> constantCostParameter() {
        return getParamById(CrossValidatorParameterFactory.CONSTANT_COST_PARAM_ID);
    }

    @Override
    public CrossValidatorParameter<Double> constantCostParameter(double value) {
        CrossValidatorParameter<Double> param = constantCostParameter();
        return constantCostParameter(value, param.lowerBound().value(), param.upperBound().value(),
                param.stepSize().value(), param.stepSize().minValue(), param.stepSize().maxValue());
    }

    @Override
    public CrossValidatorParameter<Double> constantCostParameter(double value, double lowerBound,
                                                                 double upperBound, double stepSize,
                                                                 double stepSizeMin, double stepSizeMax) {
        return parameterFactory.constantCostParameter(value, lowerBound, upperBound,
                stepSize, stepSizeMin, stepSizeMax);
    }

    @Override
    public CrossValidatorParameter<Double> gaussianKernelParameter() {
        return getParamById(CrossValidatorParameterFactory.GAUSSIAN_KERNEL_PARAM_ID);
    }

    @Override
    public CrossValidatorParameter<Double> gaussianKernelParameter(double value) {
        CrossValidatorParameter<Double> param = gaussianKernelParameter();
        return gaussianKernelParameter(value, param.lowerBound().value(), param.upperBound().value(),
                param.stepSize().value(), param.stepSize().minValue(), param.stepSize().maxValue());
    }

    @Override
    public CrossValidatorParameter<Double> gaussianKernelParameter(double value, double lowerBound,
                                                                   double upperBound, double stepSize,
                                                                   double stepSizeMin, double stepSizeMax) {
        return parameterFactory.gaussianKernelParameter(value, lowerBound, upperBound,
                stepSize, stepSizeMin, stepSizeMax);
    }

    @Override
    public CrossValidatorParameter<Integer> samplesToGenerateCount() {
        return getParamById(CrossValidatorParameterFactory.SAMPLES_TO_GENERATE_COUNT_ID);
    }

    @Override
    public CrossValidatorParameter<Integer> samplesToGenerateCount(int value) {
        CrossValidatorParameter<Integer> param = samplesToGenerateCount();
        return samplesToGenerateCount(value, param.lowerBound().value(), param.upperBound().value(),
                param.stepSize().value(), param.stepSize().minValue(), param.stepSize().maxValue());
    }

    @Override
    public CrossValidatorParameter<Integer> samplesToGenerateCount(int value, int lowerBound,
                                                                   int upperBound, int stepSize,
                                                                   int stepSizeMin, int stepSizeMax) {
        return parameterFactory.samplesToGenerateCount(value, lowerBound, upperBound,
                stepSize, stepSizeMin, stepSizeMax);
    }

    @Override
    public CrossValidatorParameter<Integer> sampleSize() {
        return getParamById(CrossValidatorParameterFactory.SAMPLE_SIZE_ID);
    }

    @Override
    public CrossValidatorParameter<Integer> sampleSize(int value) {
        CrossValidatorParameter<Integer> param = sampleSize();
        return sampleSize(value, param.lowerBound().value(), param.upperBound().value(),
                param.stepSize().value(), param.stepSize().minValue(), param.stepSize().maxValue());
    }

    @Override
    public CrossValidatorParameter<Integer> sampleSize(int value, int lowerBound, int upperBound, int stepSize,
                                                       int stepSizeMin, int stepSizeMax) {
        return parameterFactory.sampleSize(value, lowerBound, upperBound,
                stepSize, stepSizeMin, stepSizeMax);
    }

    @Override
    public CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio() {
        return getParamById(CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_ID);
    }

    @Override
    public CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio(int value) {
        CrossValidatorParameter<Integer> param = trainingTestingSetsSizeRatio();
        return trainingTestingSetsSizeRatio(value, param.lowerBound().value(), param.upperBound().value(),
                param.stepSize().value(), param.stepSize().minValue(), param.stepSize().maxValue());
    }

    @Override
    public CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio(int value, int lowerBound,
                                                                         int upperBound, int stepSize,
                                                                         int stepSizeMin, int stepSizeMax) {
        return parameterFactory.trainingTestingSetsSizeRatio(value, lowerBound, upperBound,
                stepSize, stepSizeMin, stepSizeMax);
    }

    @Override
    public CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio() {
        return getParamById(CrossValidatorParameterFactory.PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_ID);
    }

    @Override
    public CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio(int value) {
        CrossValidatorParameter<Integer> param = preciseIntervalJudgmentsCountRatio();
        return preciseIntervalJudgmentsCountRatio(value, param.lowerBound().value(), param.upperBound().value(),
                param.stepSize().value(), param.stepSize().minValue(), param.stepSize().maxValue());
    }

    @Override
    public CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio(int value, int lowerBound,
                                                                               int upperBound, int stepSize,
                                                                               int stepSizeMin, int stepSizeMax) {
        return parameterFactory.preciseIntervalJudgmentsCountRatio(value, lowerBound, upperBound,
                stepSize, stepSizeMin, stepSizeMax);
    }

    @Override
    public Set<CrossValidatorParameter<?>> defaultValues() {
        return Collections.unmodifiableSet(new HashSet<>(params.values()));
    }
}
