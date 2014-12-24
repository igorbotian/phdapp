package ru.spbftu.igorbotian.phdapp.ui.common;

/**
 * Действие, выполняемое пользователем
 */
public enum UserAction {

    /**
     * Вычисление точности классификации с заданными параметров
     */
    CALCULATE_PRECISION,

    /**
     * Вычисление средней точности серии классификаций с заданными параметрами
     */
    CALCULATE_AVERAGE_PRECISION,

    /**
     * Анализ зависимости точности классификации от размера всей выборки
     */
    ANALYZE_PRECISION_ON_SAMPLE_SIZE_DEPENDENCE,

    /**
     * Анализ зависимости точности классификации от размера обучающей
     */
    ANALYZE_PRECISION_ON_TRAINING_TESTING_SETS_SIZE_RATIO_DEPENDENCE,

    /**
     * Анализ зависимости точности классификации от значений параметров
     */
    ANALYZE_PRECISION_ON_CLASSIFIER_PARAMS_DEPENDENCE,

    /**
     * Анализ зависимости от соотношения количества точных и интервальных экспертных оценок
     */
    ANALYZE_PRECISION_ON_PRECISE_INTERVAL_SETS_SIZE_RATIO_DEPENDENCE
}
