package ru.spbftu.igorbotian.phdapp.svm.analytics;

import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;

/**
 * Набор параметров средства кросс-валидации классификатора
 */
public interface CrossValidatorParameterFactory {

    /**
     * Идентификатор параметра постоянной стоимости
     */
    public static final String CONSTANT_COST_PARAM_ID = IntervalClassifierParameterFactory.CONSTANT_COST_PARAM_ID;

    /**
     * Значение по умолчанию параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_DEFAULT_VALUE
            = IntervalClassifierParameterFactory.CONSTANT_COST_PARAM_DEFAULT_VALUE;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_DEFAULT_LOWER_BOUND = 1;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_DEFAULT_UPPER_BOUND = 1000;

    /**
     * Значение по умолчанию величины изменения параметра постоянной стоимости в процессе кросс-валидации
     */
    public static final double CONSTANT_COST_PARAM_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_MIN_VALUE
            = IntervalClassifierParameterFactory.CONSTANT_COST_PARAM_MIN_VALUE;

    /**
     * Максимально допустимое значение параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_MAX_VALUE
            = IntervalClassifierParameterFactory.CONSTANT_COST_PARAM_MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра Гауссова ядра
     */
    public static final String GAUSSIAN_KERNEL_PARAM_ID = IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_ID;

    /**
     * Значение по умолчанию параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE
            = IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_LOWER_BOUND = 1;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_UPPER_BOUND = 1000;

    /**
     * Значение по умолчанию величины изменения параметра Гауссова ядра в процессе кросс-валидации
     */
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_MIN_VALUE
            = IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_MIN_VALUE;

    /**
     * Максимально допустимое значение параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_MAX_VALUE
            = IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра, задающего количество генерируемых выборок
     */
    public static final String SAMPLES_TO_GENERATE_COUNT_ID = "samplesToGenerateCount";

    /**
     * Значение по умолчанию параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_DEFAULT_VALUE = 100;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_DEFAULT_LOWER_BOUND = 1;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_DEFAULT_UPPER_BOUND = Integer.MAX_VALUE;

    /**
     * Значение по умолчанию величины изменения параметра, задающего количество генерируемых выборок,
     * в процессе кросс-валидации
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_MAX = Integer.MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра, задающего количество объектов в генерируемой выборке
     */
    public static final String SAMPLE_SIZE_ID = "sampleSize";

    /**
     * Значение по умолчанию параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_DEFAULT_VALUE = 1000;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_DEFAULT_LOWER_BOUND = 1;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_DEFAULT_UPPER_BOUND = Short.MAX_VALUE;

    /**
     * Значение по умолчанию величины изменения параметра, задающего количество объектов в генерируемой выборке,
     * в процессе кросс-валидации
     */
    public static final int SAMPLE_SIZE_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_MAX = Integer.MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках
     */
    public static final String TRAINING_TESTING_SETS_SIZE_RATIO_ID = "trainingTestingSetsSizeRatio";

    /**
     * Значение по умолчанию параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_VALUE = 30;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_LOWER_BOUND = 5;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_UPPER_BOUND = 100;

    /**
     * Значение по умолчанию величины изменения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках, в процессе кросс-валидации
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего соотношение количества объектов, входящих в обучающей
     * и тестирующей выборкахе
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего соотношение количества объектов, входящих в обучающей
     * и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_MAX = 100;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final String PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_ID = "preciseIntervalJudgmentsCountRatio";

    /**
     * Значение по умолчанию параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_VALUE = 40;

    /**
     * Значение по умолчанию нижней границы интервального значения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_LOWER_BOUND = 0;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_UPPER_BOUND = 100;

    /**
     * Значение по умолчанию величины изменения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок, в процессе кросс-валидации
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MIN = 0;

    /**
     * Максимально допустимое значение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MAX = 100;

    //-------------------------------------------------------------------------

    /**
     * Создание параметра постоянной стоимости со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Double> newConstantCostParameter();

    /**
     * Создание параметра постоянной стоимости с заданным значением
     *
     * @param value      значение параметра
     * @param lowerBound значение нижней границы интервального значения параметра
     * @param upperBound значение верхней границы интервального значения параметра
     * @param stepSize   величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Double> newConstantCostParameter(double value, double lowerBound,
                                                             double upperBound, double stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра Гауссова ядра
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Double> newGaussianKernelParameter();

    /**
     * Создание параметра Гауссова ядра с заданным значением
     *
     * @param value      значение параметра
     * @param lowerBound значение нижней границы интервального значения параметра
     * @param upperBound значение верхней границы интервального значения параметра
     * @param stepSize   величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Double> newGaussianKernelParameter(double value, double lowerBound,
                                                               double upperBound, double stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего количество генерируемых выборок, со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newSamplesToGenerateCount();

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
    CrossValidatorParameter<Integer> newSamplesToGenerateCount(int value, int lowerBound,
                                                               int upperBound, int stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего количество объектов в генерируемой выборке, со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newSampleSize();

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
    CrossValidatorParameter<Integer> newSampleSize(int value, int lowerBound,
                                                   int upperBound, int stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках,
     * со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatio();

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
    CrossValidatorParameter<Integer> newTrainingTestingSetsSizeRatio(int value, int lowerBound,
                                                                     int upperBound, int stepSize);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего соотношение количества точных и интервальных экспертных оценок,
     * со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatio();

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
    CrossValidatorParameter<Integer> newPreciseIntervalJudgmentsCountRatio(int value, int lowerBound,
                                                                           int upperBound, int stepSize);
}
