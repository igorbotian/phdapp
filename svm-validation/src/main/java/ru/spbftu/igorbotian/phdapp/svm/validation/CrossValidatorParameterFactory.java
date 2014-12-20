package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;

import java.util.Set;

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
     * Минимально допустимое значение параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_MIN_VALUE
            = IntervalClassifierParameterFactory.CONSTANT_COST_PARAM_MIN_VALUE;

    /**
     * Максимально допустимое значение параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_MAX_VALUE
            = IntervalClassifierParameterFactory.CONSTANT_COST_PARAM_MAX_VALUE;

    /**
     * Значение по умолчанию величины изменения параметра постоянной стоимости в процессе кросс-валидации
     */
    public static final double CONSTANT_COST_PARAM_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение величины изменения параметра постоянной стоимости в процессе кросс-валидации
     */
    public static final double CONSTANT_COST_PARAM_STEP_SIZE_MIN = 1;

    /**
     * Максимально допустимое значение величины изменения параметра постоянной стоимости в процессе кросс-валидации
     */
    public static final double CONSTANT_COST_PARAM_STEP_SIZE_MAX = CONSTANT_COST_PARAM_MAX_VALUE;

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
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_LOWER_BOUND = 0.001;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_UPPER_BOUND = 10;

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

    /**
     * Значение по умолчанию величины изменения параметра Гауссова ядра в процессе кросс-валидации
     */
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_STEP_SIZE = 0.05;

    /**
     * Максимально допустимое значение величины изменения параметра Гауссова ядра в процессе кросс-валидации
     */
    public static final double GAUSSIAN_KERNEL_PARAM_STEP_SIZE_MIN = 0.0000001;

    /**
     * Максимально допустимое значение величины изменения параметра Гауссова ядра в процессе кросс-валидации
     */
    public static final double GAUSSIAN_KERNEL_PARAM_STEP_SIZE_MAX = GAUSSIAN_KERNEL_PARAM_MAX_VALUE;

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
     * Минимально допустимое значение параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего количество генерируемых выборок
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_MAX = Integer.MAX_VALUE;

    /**
     * Значение по умолчанию величины изменения параметра, задающего количество генерируемых выборок,
     * в процессе кросс-валидации
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение величины изменения параметра, задающего количество генерируемых выборок,
     * в процессе кросс-валидации
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_STEP_SIZE_MIN = 1;

    /**
     * Максимально допустимое значение величины изменения параметра, задающего количество генерируемых выборок,
     * в процессе кросс-валидации
     */
    public static final int SAMPLES_TO_GENERATE_COUNT_STEP_SIZE_MAX = SAMPLES_TO_GENERATE_COUNT_MAX;

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
    public static final int SAMPLE_SIZE_DEFAULT_LOWER_BOUND = 4;

    /**
     * Значение по умолчанию верхней границы интервального значения параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_DEFAULT_UPPER_BOUND = Short.MAX_VALUE;

    /**
     * Минимально допустимое значение параметра, задающего количество объектов в генерируемой выборке.
     * Оно должно удовлетворять следующим условиям:
     * 1) В каждой выборке должно присутствовать объектов, принадлежащих не менее 2-м классам
     * 2) При кросс-валидации генерируемая выборка состоит из объектов из 2-х классов, и при разбиении тестирующая
     * и обучающая выборки сами должны содержать объекты, принадлежащие не менее 2-м классам
     */
    public static final int SAMPLE_SIZE_MIN = 4;

    /**
     * Максимально допустимое значение параметра, задающего количество объектов в генерируемой выборке
     */
    public static final int SAMPLE_SIZE_MAX = Integer.MAX_VALUE;

    /**
     * Значение по умолчанию величины изменения параметра, задающего количество объектов в генерируемой выборке,
     * в процессе кросс-валидации
     */
    public static final int SAMPLE_SIZE_DEFAULT_STEP_SIZE = 2;

    /**
     * Минимально допустимое значение величины изменения параметра, задающего количество объектов в генерируемой выборке,
     * в процессе кросс-валидации
     */
    public static final int SAMPLE_SIZE_STEP_SIZE_MIN = 2;

    /**
     * Минимально допустимое значение величины изменения параметра, задающего количество объектов в генерируемой выборке,
     * в процессе кросс-валидации
     */
    public static final int SAMPLE_SIZE_STEP_SIZE_MAX = SAMPLE_SIZE_MAX;

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
     * Минимально допустимое значение параметра, задающего соотношение количества объектов, входящих в обучающей
     * и тестирующей выборкахе
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_MIN = 1;

    /**
     * Максимально допустимое значение параметра, задающего соотношение количества объектов, входящих в обучающей
     * и тестирующей выборках
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_MAX = 100;

    /**
     * Значение по умолчанию величины изменения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках, в процессе кросс-валидации
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимальное допустимое величины изменения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках, в процессе кросс-валидации
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_STEP_SIZE_MIN = 1;

    /**
     * Максимально допустимое величины изменения параметра, задающего соотношение количества объектов,
     * входящих в обучающей и тестирующей выборках, в процессе кросс-валидации
     */
    public static final int TRAINING_TESTING_SETS_SIZE_RATIO_STEP_SIZE_MAX = 50;

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
     * Минимально допустимое значение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MIN = 0;

    /**
     * Максимально допустимое значение параметра, задающего соотношение количества точных и интервальных экспертных оценок
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MAX = 100;

    /**
     * Значение по умолчанию величины изменения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок, в процессе кросс-валидации
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_DEFAULT_STEP_SIZE = 1;

    /**
     * Минимально допустимое значение величины изменения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок, в процессе кросс-валидации
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_STEP_SIZE_MIN = 1;

    /**
     * Максимально допустимое значение величины изменения параметра, задающего соотношение количества точных
     * и интервальных экспертных оценок, в процессе кросс-валидации
     */
    public static final int PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_STEP_SIZE_MAX = 50;

    //-------------------------------------------------------------------------

    /**
     * Создание параметра постоянной стоимости со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Double> constantCostParameter();

    /**
     * Создание параметра постоянной стоимости с заданным значением
     *
     * @param value значение параметра
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Double> constantCostParameter(double value);

    /**
     * Создание параметра постоянной стоимости с заданным значением
     *
     * @param value       значение параметра
     * @param lowerBound  значение нижней границы интервального значения параметра
     * @param upperBound  значение верхней границы интервального значения параметра
     * @param stepSize    величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMin минимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMax максимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Double> constantCostParameter(double value, double lowerBound,
                                                          double upperBound, double stepSize,
                                                          double stepSizeMin, double stepSizeMax);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра Гауссова ядра со значением по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Double> gaussianKernelParameter();

    /**
     * Создание параметра Гауссова ядра с заданным значением
     *
     * @param value значение параметра
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Double> gaussianKernelParameter(double value);

    /**
     * Создание параметра Гауссова ядра с заданным значением
     *
     * @param value       значение параметра
     * @param lowerBound  значение нижней границы интервального значения параметра
     * @param upperBound  значение верхней границы интервального значения параметра
     * @param stepSize    величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMin минимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMax максимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Double> gaussianKernelParameter(double value, double lowerBound,
                                                            double upperBound, double stepSize,
                                                            double stepSizeMin, double stepSizeMax);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего количество генерируемых выборок, со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> samplesToGenerateCount();

    /**
     * Создание параметра, задающего количество генерируемых выборок, с заданным значением
     *
     * @param value значение параметра
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> samplesToGenerateCount(int value);

    /**
     * Создание параметра, задающего количество генерируемых выборок, с заданным значением
     *
     * @param value       значение параметра
     * @param lowerBound  значение нижней границы интервального значения параметра
     * @param upperBound  значение верхней границы интервального значения параметра
     * @param stepSize    величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMin минимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMax максимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> samplesToGenerateCount(int value, int lowerBound,
                                                            int upperBound, int stepSize,
                                                            int stepSizeMin, int stepSizeMax);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего количество объектов в генерируемой выборке, со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> sampleSize();

    /**
     * Создание параметра, задающего количество объектов в генерируемой выборке, с заданным значением
     *
     * @param value значение параметра
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> sampleSize(int value);

    /**
     * Создание параметра, задающего количество объектов в генерируемой выборке, с заданным значением
     *
     * @param value       значение параметра
     * @param lowerBound  значение нижней границы интервального значения параметра
     * @param upperBound  значение верхней границы интервального значения параметра
     * @param stepSize    величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMin минимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMax максимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> sampleSize(int value, int lowerBound,
                                                int upperBound, int stepSize,
                                                int stepSizeMin, int stepSizeMax);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках,
     * со значениями по умолчанию;
     * 50 - обучающая и тестирующая выборка имеют одинаковое количество объектов;
     * 0 - обучающая выборка имеет минимально допустимое количество объектов,
     * равное <code>UnclassifiedData.MIN_NUMBER_OF_CLASSES</code>;
     * 100 - тестирующая выборка имеет минимально допустимое количество объектов,
     * равное <code>UnclassifiedData.MIN_NUMBER_OF_CLASSES</code>;
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio();

    /**
     * Создание параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках,
     * с заданным значением
     *
     * @param value значение параметра
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio(int value);

    /**
     * Создание параметра, задающего соотношение количества объектов, входящих в обучающей и тестирующей выборках,
     * с заданным значением;
     * 50 - обучающая и тестирующая выборка имеют одинаковое количество объектов;
     * 0 - обучающая выборка имеет минимально допустимое количество объектов,
     * равное <code>UnclassifiedData.MIN_NUMBER_OF_CLASSES</code>;
     * 100 - тестирующая выборка имеет минимально допустимое количество объектов,
     * равное <code>UnclassifiedData.MIN_NUMBER_OF_CLASSES</code>;
     *
     * @param value       значение параметра
     * @param lowerBound  значение нижней границы интервального значения параметра
     * @param upperBound  значение верхней границы интервального значения параметра
     * @param stepSize    величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMin минимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMax максимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> trainingTestingSetsSizeRatio(int value, int lowerBound,
                                                                  int upperBound, int stepSize,
                                                                  int stepSizeMin, int stepSizeMax);

    //-------------------------------------------------------------------------

    /**
     * Создание параметра, задающего соотношение количества точных и интервальных экспертных оценок,
     * со значениями по умолчанию
     *
     * @return параметр средства кросс-валидации
     */
    CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio();

    /**
     * Создание параметра, задающего соотношение количества точных и интервальных экспертных оценок, с заданным значением
     *
     * @param value значение параметра
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio(int value);

    /**
     * Создание параметра, задающего соотношение количества точных и интервальных экспертных оценок, с заданным значением
     *
     * @param value       значение параметра
     * @param lowerBound  значение нижней границы интервального значения параметра
     * @param upperBound  значение верхней границы интервального значения параметра
     * @param stepSize    величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMin минимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @param stepSizeMax максимально допустимая величина изменения значения параметра в процессе кросс-валидации
     * @return параметр средства кросс-валидации
     * @throws IllegalArgumentException если заданное значение параметра меньше минимально допустимого
     *                                  или больше максимально допустимого
     */
    CrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio(int value, int lowerBound,
                                                                        int upperBound, int stepSize,
                                                                        int stepSizeMin, int stepSizeMax);

    //-------------------------------------------------------------------------

    /**
     * Получение множества параметров средства кросс-валидации классификатора со значениями по умолчанию
     *
     * @return непустое множество параметров средства кросс-валидации классификатора
     */
    Set<CrossValidatorParameter<?>> defaultValues();
}
