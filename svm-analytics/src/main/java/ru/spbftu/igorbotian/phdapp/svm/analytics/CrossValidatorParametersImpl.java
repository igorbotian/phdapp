package ru.spbftu.igorbotian.phdapp.svm.analytics;

/**
 * Реализация набора параметров средства кросс-валидации классификатора
 */
class CrossValidatorParametersImpl implements CrossValidatorParameters {

    @Override
    public CrossValidatorParameter<Integer> samplesToGenerateCount() {
        return new DefaultCrossValidatorParameterImpl<>("samplesToGenerateCount", Integer.class, 100,
                1, (int) Short.MAX_VALUE, 1, (int) Short.MAX_VALUE, 1, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> sampleSize() {
        return new DefaultCrossValidatorParameterImpl<>("sampleSize", Integer.class, 1000,
                1, (int) Short.MAX_VALUE, 1, (int) Short.MAX_VALUE, 1, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio() {
        return new DefaultCrossValidatorParameterImpl<>("trainingTestingSetsSizeRatio", Integer.class, 30,
                5, 100, 1, 100, 1, Integer::compare);
    }

    @Override
    public CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio() {
        return new DefaultCrossValidatorParameterImpl<>("preciseIntervalJudgmentsCountRatio", Integer.class, 40,
                0, 100, 0, 100, 1, Integer::compare);
    }
}
