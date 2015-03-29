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
 * Средство кросс-валидации, направленное на точность работы ранжирующего попарного классификатора
 */
class PrecisionValidator extends AbstractRankingPairwiseClassifierCrossValidator<SingleClassificationReport> {

    private static final Logger LOGGER = Logger.getLogger(PrecisionValidator.class);

    /**
     * Фабрика математических примитивов
     */
    protected final MathDataFactory mathDataFactory;

    /**
     * Фабрика объектов предметной области
     */
    protected final DataFactory dataFactory;

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
    protected SingleClassificationReport validate(RankingPairwiseClassifier classifier,
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
            classifier.train(trainingSet, specificClassifierParams);
            return validate(classifier, specificClassifierParams, specificValidatorParams, testingSet.objects());
        } catch (ClassifierTrainingException e) {
            throw new CrossValidationException("Failed to train objects during cross-validation", e);
        } catch (ClassificationException e) {
            throw new CrossValidationException("Failed to classify objects during cross-validation ", e);
        }
    }

    private SingleClassificationReport validate(RankingPairwiseClassifier classifier,
                                                Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                CrossValidatorParameterFactory specificValidatorParams,
                                                Set<? extends UnclassifiedObject> testingSet)
            throws CrossValidationException, ClassificationException {

        assert classifier != null;
        assert specificClassifierParams != null;
        assert testingSet != null;

        Set<Point> testingSetOfPoints = asTestingSetOfPoints(testingSet);
        Set<Pair<Point, Point>> testingPairs = composeTestingPairs(testingSetOfPoints);
        Map<Pair<Point, Point>, Boolean> classificationResult
                = classify(classifier, specificClassifierParams, testingPairs);
        return composeReport(classificationResult, specificClassifierParams, specificValidatorParams);
    }

    private <T extends UnclassifiedObject> Map<Pair<T, T>, Boolean> classify(RankingPairwiseClassifier classifier,
                                                                             Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                                             Set<Pair<T, T>> data) throws ClassificationException {
        assert classifier != null;
        assert data != null;

        Map<Pair<T, T>, Boolean> results = new HashMap<>();

        for (Pair<T, T> pair : data) {
            results.put(pair, classifier.classify(pair.first, pair.second, specificClassifierParams));
        }

        return results;
    }

    private Set<Point> asTestingSetOfPoints(Set<? extends UnclassifiedObject> testingSet)
            throws CrossValidationException {

        assert testingSet != null;
        Set<Point> result = new LinkedHashSet<>();

        for (UnclassifiedObject obj : testingSet) {
            if (!(obj instanceof Point)) {
                throw new CrossValidationException("Unsupported type of testing set item: " + obj.getClass().getName());
            }

            result.add((Point) obj);
        }

        return result;
    }

    private <T> Set<Pair<T, T>> composeTestingPairs(Set<T> testingSet) {
        assert testingSet != null;
        Set<Pair<T, T>> pairs = new LinkedHashSet<>();
        Iterator<T> it = testingSet.iterator();

        while (it.hasNext()) {
            T first = it.next();

            if (it.hasNext()) {
                pairs.add(new Pair<>(first, it.next()));
            }
        }

        return pairs;
    }

    /**
     * Функция, определяющая экспертную оценку для заданных групп объектов
     */
    private Integer judgePoints(Set<? extends ClassifiedObject> firstGroup, Set<? extends ClassifiedObject> secondGroup) {
        if (bothGroupsBelongToSameClass(firstGroup, secondGroup)) {
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

        for (ClassifiedObject obj : firstGroup) {
            if (!obj.dataClass().equals(clazz)) {
                return false;
            }
        }

        for (ClassifiedObject obj : secondGroup) {
            if (!obj.dataClass().equals(clazz)) {
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
        return first.distanceTo(supportingPoint) < second.distanceTo(supportingPoint);

    }

    private Boolean realPointClassifier(Pair<Point, Point> points) {
        assert points != null;
        return firstPointIsCloserToSecondSupportingPoint(points.first, points.second);
    }

    /**
     * Формирования отчёта по единичной классификации
     */
    private SingleClassificationReport composeReport(Map<Pair<Point, Point>, Boolean> classificationResult,
                                                     Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                     CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationException {

        Metrics metrics = Metrics.forData(classificationResult, this::realPointClassifier);

        return reportFactory.newSingleClassificationReport(
                specificClassifierParams, specificValidatorParams.defaultValues(),
                metrics.accuracy(), metrics.precision(), metrics.recall()
        );
    }

    /**
     * Метрики оценки точности работы ранжирующего классификатора
     *
     * @see <a href="http://en.wikipedia.org/wiki/Precision_and_recall">http://en.wikipedia.org/wiki/Precision_and_recall</a>
     */
    private static class Metrics {

        /**
         * Количество объектов, которое классификатор верно отнёс к данному классу
         */
        private int truePositives;

        /**
         * Количество объектов другого класса, которое классификаторо неверно отнёс к данному классу
         */
        private int falsePositives;

        /**
         * Количество объектов данного класса, которое классификатор к нему не отнёс
         */
        private int falseNegatives;

        private Metrics(int truePositives, int falsePositives, int falseNegatives) {
            this.truePositives = truePositives;
            this.falsePositives = falsePositives;
            this.falseNegatives = falseNegatives;
        }

        public static <T extends UnclassifiedObject> Metrics forData(Map<Pair<T, T>, Boolean> classificationResult,
                                                                     Function<Pair<T, T>, Boolean> realClassifier) {

            assert classificationResult != null;
            assert realClassifier != null;

            int truePositives = 0;
            int falsePositives = 0;
            int falseNegatives = 0;

            for (Pair<T, T> pair : classificationResult.keySet()) {
                boolean givenResult = classificationResult.get(pair);
                boolean realResult = realClassifier.apply(pair);

                if (givenResult && realResult) {
                    truePositives++;
                } else if(realResult) {
                    falseNegatives++;
                } else if (givenResult) {
                    falsePositives++;
                }
            }

            return new Metrics(truePositives, falsePositives, falseNegatives);
        }

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