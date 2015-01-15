package ru.spbftu.igorbotian.phdapp.svm.validation;

import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.svm.*;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.ReportFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleException;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point;

import java.util.*;
import java.util.function.Function;

/**
 * Средство кросс-валидации, направленное на точность работы попарного классификатора
 */
class PrecisionValidator extends AbstractPairwiseClassifierCrossValidator<SingleClassificationReport> {

    private static final Logger LOGGER = Logger.getLogger(PrecisionValidator.class);

    /**
     * Фабрика математических примитивов
     */
    protected MathDataFactory mathDataFactory;

    /**
     * Фабрика объектов предметной области
     */
    protected DataFactory dataFactory;

    protected PrecisionValidator(CrossValidationSampleManager sampleManager,
                                 IntervalClassifierParameterFactory classifierParameterFactory,
                                 CrossValidatorParameterFactory crossValidatorParameterFactory,
                                 ReportFactory reportFactory, MathDataFactory mathDataFactory,
                                 DataFactory dataFactory,
                                 ApplicationConfiguration appConfig) {

        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory, appConfig);
        this.mathDataFactory = Objects.requireNonNull(mathDataFactory);
        this.dataFactory = Objects.requireNonNull(dataFactory);
    }

    @Override
    protected SingleClassificationReport validate(PairwiseClassifier classifier,
                                                  Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                  CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        int sampleSize = specificValidatorParams.sampleSize().value().value();
        int trainingTestingSetsSizeRatio = specificValidatorParams.trainingTestingSetsSizeRatio().value().value();
        int preciseIntervalJudgementsCountRatio = specificValidatorParams.preciseIntervalJudgmentsCountRatio().value().value();
        int maxJudgementGroupSize = 5;

        LOGGER.debug("Sample size = " + sampleSize);
        LOGGER.debug("Training/testing sets size ratio = " + trainingTestingSetsSizeRatio);
        LOGGER.debug("Precise/interval judgements count ratio = " + preciseIntervalJudgementsCountRatio);
        LOGGER.debug("Maximum judgement group size = " + maxJudgementGroupSize);

        ClassifiedData sample = sampleManager.generateSample(sampleSize);
        Pair<ClassifiedData, ClassifiedData> sampleSets = sampleManager.divideSampleIntoTwoGroups(sample,
                trainingTestingSetsSizeRatio);
        ClassifiedData trainingSetData = sampleSets.first;
        ClassifiedData testingSet = sampleSets.second;
        PairwiseTrainingSet trainingSet = sampleManager.generateTrainingSet(trainingSetData,
                preciseIntervalJudgementsCountRatio, maxJudgementGroupSize, this::judgePoints);

        try {
            classifier.train(trainingSet);
            ClassifiedData classifiedData = classifier.classify(testingSet, specificClassifierParams);
            return composeReport(testingSet, classifiedData, specificClassifierParams, specificValidatorParams);
        } catch (ClassifierTrainingException e) {
            throw new CrossValidationException("Failed to train objects during cross-validation", e);
        } catch (ClassificationException e) {
            throw new CrossValidationException("Failed to classify objects during cross-validation ", e);
        }
    }

    /**
     * Функция, определяющая экспертную оценку для заданных групп объектов
     */
    private Integer judgePoints(Set<? extends ClassifiedObject> firstGroup, Set<? extends ClassifiedObject> secondGroup) {
        if(bothGroupsBelongToSameClass(firstGroup, secondGroup)) {
            return 0;
        }

        Set<Point> firstPoints = toSetOfPoints(firstGroup);
        Set<Point> secondPoints = toSetOfPoints(secondGroup);
        Point averageFirstPoint = averagePointOf(firstPoints);
        Point averageSecondPoint = averagePointOf(secondPoints);

        return firstPointIsCloserToSecondSupportingPoint(averageFirstPoint, averageSecondPoint) ? 1 : -1;
    }

    /**
     * Проверка на то, что обе группы состоят из объектов одинакового класса
     */
    private boolean bothGroupsBelongToSameClass(Set<? extends ClassifiedObject> firstGroup,
                                                Set<? extends ClassifiedObject> secondGroup) {

        DataClass clazz = firstGroup.iterator().next().dataClass();

        for(ClassifiedObject obj : firstGroup) {
            if(!obj.dataClass().equals(clazz)) {
                return false;
            }
        }

        for(ClassifiedObject obj : secondGroup) {
            if(!obj.dataClass().equals(clazz)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Переход к поддерживаемому типу данных для кросс-валидации
     */
    private Set<Point> toSetOfPoints(Set<? extends ClassifiedObject> set) {
        Set<Point> points = new HashSet<>();

        for (ClassifiedObject obj : set) {
            if (obj instanceof Point) {
                points.add((Point) obj);
            } else {
                throw new IllegalStateException("Only data of Point class is supported " +
                        "by the cross-validation mechanism at this moment");
            }
        }

        return points;
    }

    /**
     * Вычисление точки, имеющей среднее значение координат из набор заданных точек
     */
    private Point averagePointOf(Set<Point> points) {
        long sumX = 0;
        long sumY = 0;

        for (Point point : points) {
            sumX += point.x();
            sumY += point.y();
        }

        return mathDataFactory.newPoint(sumX / points.size(), sumY / points.size());
    }

    private boolean firstPointIsCloserToSecondSupportingPoint(Point first, Point second) {
        Point supportingPoint = sampleManager.sampleGenerator().secondSupportingPoint();
        return (first.distanceTo(supportingPoint) < second.distanceTo(supportingPoint));

    }

    /**
     * Формирования отчёта по единичной классификации
     */
    private SingleClassificationReport composeReport(ClassifiedData realData, ClassifiedData classifiedData,
                                                     Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                     CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationException {

        Map<String, ClassifiedObject> realObjects = toClassifiedObjectsMap(realData);
        Map<String, ClassifiedObject> classifiedObjects = toClassifiedObjectsMap(classifiedData);

        if (!realObjects.keySet().equals(classifiedObjects.keySet())) {
            throw new CrossValidationException("Real objects and their classified equivalents should have the same IDs");
        }

        Metrics metrics = Metrics.forData(realObjects, classifiedObjects);

        return reportFactory.newSingleClassificationReport(
                specificClassifierParams, specificValidatorParams.defaultValues(),
                metrics.accuracy(), metrics.precision(), metrics.recall()
        );
    }

    /**
     * Создание ассоциативного массива "идентификатор объекта - сам объект" на основе заданного множества объектов
     */
    private Map<String, ClassifiedObject> toClassifiedObjectsMap(ClassifiedData data) throws CrossValidationException {
        Map<String, ClassifiedObject> map = new HashMap<>();

        for (ClassifiedObject obj : data.objects()) {
            if (map.containsKey(obj.id())) {
                throw new CrossValidationException("Classified data contains multiple objects with the same ID: "
                        + obj.id());
            }

            map.put(obj.id(), obj);
        }

        return map;
    }

    /**
     * Метрики оценки точности работы классификатора
     *
     * @see <a href="http://en.wikipedia.org/wiki/Precision_and_recall">http://en.wikipedia.org/wiki/Precision_and_recall</a>
     */
    private static class Metrics {

        private final float accuracy;
        private final float precision;
        private final float recall;

        private Metrics(float accuracy, float precision, float recall) {
            this.accuracy = accuracy;
            this.precision = precision;
            this.recall = recall;
        }

        public static Metrics forData(Map<String, ClassifiedObject> realObjects,
                                      Map<String, ClassifiedObject> classifiedObjects) {

            assert (realObjects.size() == classifiedObjects.size());

            Map<DataClass, MetricsPerClass> metrics = new HashMap<>();

            for (String objId : realObjects.keySet()) {
                DataClass realClass = realObjects.get(objId).dataClass();
                DataClass givenClass = classifiedObjects.get(objId).dataClass();

                if (!metrics.containsKey(realClass)) {
                    metrics.put(realClass, new MetricsPerClass());
                }

                if (!metrics.containsKey(givenClass)) {
                    metrics.put(givenClass, new MetricsPerClass());
                }

                MetricsPerClass metricsPerRealClass = metrics.get(realClass);
                MetricsPerClass metricsPerGivenClass = metrics.get(givenClass);

                if (realClass.equals(givenClass)) {
                    metricsPerRealClass.truePositives++;
                } else {
                    metricsPerRealClass.falseNegatives++;
                    metricsPerGivenClass.falsePositives++;
                }
            }

            return new Metrics(
                    averageValue(metrics, MetricsPerClass::accuracy),
                    averageValue(metrics, MetricsPerClass::precision),
                    averageValue(metrics, MetricsPerClass::recall)
            );
        }

        /**
         * Вычисление среднего значения по всем классам в выборке для данной метрики
         */
        private static float averageValue(Map<DataClass, MetricsPerClass> metrics, Function<MetricsPerClass,
                Float> metricGetter) {

            float metricSum = 0.0f;

            for (DataClass clazz : metrics.keySet()) {
                metricSum += metricGetter.apply(metrics.get(clazz));
            }

            return metricSum / metrics.size();
        }

        public float accuracy() {
            return accuracy;
        }

        public float precision() {
            return precision;
        }

        public float recall() {
            return recall;
        }
    }

    /**
     * Метрики оценки точности работы классификатора применительно к каждому классу классификации отдельно
     *
     * @see <a href="http://en.wikipedia.org/wiki/Precision_and_recall">http://en.wikipedia.org/wiki/Precision_and_recall</a>
     */
    private static class MetricsPerClass {

        /**
         * Количество объектов, которое классификатор верно отнёс к данному классу
         */
        public int truePositives;

        /**
         * Количество объектов другого класса, которое классификаторо неверно отнёс к данному классу
         */
        public int falsePositives;

        /**
         * Количество объектов данного класса, которое классификатор к нему не отнёс
         */
        public int falseNegatives;

        /**
         * Получение значения метрики Accuracy для заданного класса
         */
        public float accuracy() {
            return (float) truePositives / ((float) truePositives + (float) falsePositives);
        }

        /**
         * Получение значения метрики Precision для заданного класса
         */
        public float precision() {
            return (float) truePositives / ((float) truePositives + (float) falsePositives);
        }

        /**
         * Получение значения метрики Recall для заданного класса
         */
        public float recall() {
            return (float) truePositives / ((float) truePositives + (float) falseNegatives);
        }
    }
}