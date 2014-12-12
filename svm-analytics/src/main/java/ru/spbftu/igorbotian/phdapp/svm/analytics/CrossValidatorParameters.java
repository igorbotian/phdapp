package ru.spbftu.igorbotian.phdapp.svm.analytics;

/**
 * Набор параметров средства кросс-валидации классификатора
 */
public interface CrossValidatorParameters {

    /**
     * Получение параметра, задающего количество генерируемых выборок
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> samplesToGenerateCount();

    /**
     * Получение параметра, задающего количество объектов в генерируемой выборке, т.е. её размер
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> sampleSize();

    /**
     * Получение параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio();

    /**
     * Получение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio();
}
