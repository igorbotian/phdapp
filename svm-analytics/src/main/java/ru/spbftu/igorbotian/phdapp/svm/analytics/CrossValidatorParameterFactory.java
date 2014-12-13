package ru.spbftu.igorbotian.phdapp.svm.analytics;

/**
 * Набор параметров средства кросс-валидации классификатора
 */
public interface CrossValidatorParameterFactory {

    /**
     * Идентификатор параметра, задающего количество генерируемых выборок
     */
    public static final String SAMPLES_TO_GENERATE_COUNT_PARAM_ID = "samplesToGenerateCount";

    /**
     * Значение по умолчанию параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_VALUE = 100;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_LOWER_BOUND = 1;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_UPPER_BOUND = Integer.MAX_VALUE;

    /**
     * Значение по умолчанию величины изменения параметра, задающего количество генерируемых выборок,
     * в процессе кросс-валидации
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_PARAM_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_PARAM_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_PARAM_MAX = Integer.MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра, задающего количество объектов в генерируемой выборке
     */
    public static final String SAMPLE_SIZE_PARAM_ID = "samplesToGenerateCount";

    /**
     * Значение по умолчанию параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_PARAM_DEFAULT_VALUE = 1000;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_PARAM_DEFAULT_LOWER_BOUND = 1;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_PARAM_DEFAULT_UPPER_BOUND = Integer.MAX_VALUE;

    /**
     * Значение по умолчанию величины изменения параметра, задающего количество объектов в генерируемой выборке,
     * в процессе кросс-валидации
     */
    public static final int SAMPLE_SIZE_PARAM_PARAM_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_PARAM_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_PARAM_MAX = Integer.MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках
     */
    public static final String TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_ID = "trainingTestingSetsSizeRatio";

    /**
     * Значение по умолчанию параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_VALUE = 30;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_LOWER_BOUND = 5;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_UPPER_BOUND = 100;

    /**
     * Значение по умолчанию величины изменения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках, в процессе кросс-валидации
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего соотношение количества объектов, входящих в обучающей
     * и тестирующей выборкахе
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего соотношение количества объектов, входящих в обучающей
     * и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_PARAM_MAX = 100;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final String PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_ID = "preciseIntervalJudgmentsCountRatio";

    /**
     * Значение по умолчанию параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_VALUE = 40;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_LOWER_BOUND = 0;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_UPPER_BOUND = 100;

    /**
     * Значение по умолчанию величины изменения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок, в процессе кросс-валидации
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_MIN = 0;

    /**
     * Максимально допустимое значение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_PARAM_MAX = 100;

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего количество генерируемых выборок, со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newSamplesToGenerateCountParameter();

    /**
     * Создание параметра, задающего количество генерируемых выборок, с заданным значением
     *
     * @param value      значение параметра
     * @param lowerBound значение нижней границы интервального значения параметра
     * @param upperBound значение верхней границы интервального значения параметра
     * @param stepSize   величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> newSamplesToGenerateCountParameter(int value, int lowerBound,
                                                                        int upperBound, int stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего количество объектов в генерируемой выборке, со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newSampleSizeParameter();

    /**
     * Создание параметра, задающего количество объектов в генерируемой выборке, с заданным значением
     *
     * @param value      значение параметра
     * @param lowerBound значение нижней границы интервального значения параметра
     * @param upperBound значение верхней границы интервального значения параметра
     * @param stepSize   величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> newSampleSizeParameter(int value, int lowerBound,
                                                            int upperBound, int stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках,
     * со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatioParameter();

    /**
     * Создание параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках,
     * с заданным значением
     *
     * @param value      значение параметра
     * @param lowerBound значение нижней границы интервального значения параметра
     * @param upperBound значение верхней границы интервального значения параметра
     * @param stepSize   величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatioParameter(int value, int lowerBound,
                                                                              int upperBound, int stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего соотношение количества точных и интервальных экспертных оценок,
     * со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatioParameter();

    /**
     * Создание параметра, задающего соотношение количества точных и интервальных экспертных оценок, с заданным значением
     *
     * @param value      значение параметра
     * @param lowerBound значение нижней границы интервального значения параметра
     * @param upperBound значение верхней границы интервального значения параметра
     * @param stepSize   величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatioParameter(int value, int lowerBound,
                                                                                    int upperBound, int stepSize);
}
