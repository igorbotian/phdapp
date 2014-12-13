package ru.spbftu.igorbotian.phdapp.svm.analytics;

/**
 * Набор параметров средства кросс-валидации классификатора
 */
public interface CrossValidatorParameterFactory {

    /**
     * Создание параметра, задающего количество генерируемых выборок
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> newSamplesToGenerateCountParameter();

    /**
     * Создание параметра, задающего количество объектов в генерируемой выборке, т.е. её размер
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> newSampleSizeParameter();

    /**
     * Создание параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatioParameter();

    /**
     * Создание параметра, задающего соотношение количества точных и интервальных экспертных оценок
     *
     * @return параметр средства кросс-валидации
     */
    MutableCrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatioParameter();
}
