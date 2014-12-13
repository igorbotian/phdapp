package ru.spbftu.igorbotian.phdapp.svm.analytics;

/**
 * Реализация набора параметров средства кросс-валидации классификатора
 */
class CrossValidatorParameterFactoryImpl implements CrossValidatorParameterFactory {

    @Override
    public MutableCrossValidatorParameter<Integer> newSamplesToGenerateCountParameter() {
        return new DefaultCrossValidatorParameterImpl<>("samplesToGenerateCount", Integer.class, 100,
                1, (int) Short.MAX_VALUE, 1, (int) Short.MAX_VALUE, 1, Integer::compare);
    }

    @Override
    public MutableCrossValidatorParameter<Integer> newSampleSizeParameter() {
        return new DefaultCrossValidatorParameterImpl<>("sampleSize", Integer.class, 1000,
                1, (int) Short.MAX_VALUE, 1, (int) Short.MAX_VALUE, 1, Integer::compare);
    }

    @Override
    public MutableCrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatioParameter() {
        return new DefaultCrossValidatorParameterImpl<>("trainingTestingSetsSizeRatio", Integer.class, 30,
                5, 100, 1, 100, 1, Integer::compare);
    }

    @Override
    public MutableCrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatioParameter() {
        return new DefaultCrossValidatorParameterImpl<>("preciseIntervalJudgmentsCountRatio", Integer.class, 40,
                0, 100, 0, 100, 1, Integer::compare);
    }
}
