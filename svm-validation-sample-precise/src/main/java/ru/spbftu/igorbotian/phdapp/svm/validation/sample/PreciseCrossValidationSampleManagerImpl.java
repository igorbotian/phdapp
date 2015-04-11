package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Реализация средства формирования выборки для кросс-валидации ранжирующего попарного классификатора,
 * поддерживающего только точные экспертные оценки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
@Singleton
class PreciseCrossValidationSampleManagerImpl implements PreciseCrossValidationSampleManager {

    private final DataFactory dataFactory;
    private final MathDataFactory mathDataFactory;
    private final IntervalCrossValidationSampleManager sampleManager;

    @Inject
    public PreciseCrossValidationSampleManagerImpl(DataFactory dataFactory,
                                                   MathDataFactory mathDataFactory,
                                                   IntervalCrossValidationSampleManager sampleManager) {

        this.dataFactory = Objects.requireNonNull(dataFactory);
        this.mathDataFactory = Objects.requireNonNull(mathDataFactory);
        this.sampleManager = Objects.requireNonNull(sampleManager);
    }

    @Override
    public CrossValidationSampleGenerator sampleGenerator() {
        return sampleManager.sampleGenerator();
    }

    @Override
    public ClassifiedData generateSample(int sampleSize) throws CrossValidationSampleException {
        return sampleManager.generateSample(sampleSize);
    }

    @Override
    public Pair<ClassifiedData, ClassifiedData> divideSampleIntoTwoGroups(ClassifiedData sample, int ratio)
            throws CrossValidationSampleException {
        return sampleManager.divideSampleIntoTwoGroups(sample, ratio);
    }

    @Override
    public PairwiseTrainingSet generateTrainingSet(ClassifiedData source, int ratio, int maxJudgementGroupSize,
                                                   BiFunction<Set<? extends ClassifiedObject>,
                                                           Set<? extends ClassifiedObject>, Integer> expertFunction)
            throws CrossValidationSampleException {

        PairwiseTrainingSet trainingSet
                = sampleManager.generateTrainingSet(source, ratio, maxJudgementGroupSize, expertFunction);
        Set<Judgement> preciseJudgements = new HashSet<>();

        for (Judgement judgement : trainingSet.judgements()) {
            if (isPrecise(judgement)) {
                preciseJudgements.add(judgement);
            } else {
                preciseJudgements.add(makePrecise(judgement));
            }
        }

        return dataFactory.newPairwiseTrainingSet(preciseJudgements);
    }

    private boolean isPrecise(Judgement judgement) {
        assert judgement != null;
        return (judgement.inferior().size() == 1 && judgement.preferable().size() == 1);
    }

    private Judgement makePrecise(Judgement intervalJudgement) throws CrossValidationSampleException {
        assert intervalJudgement != null;
        Set<Point> preferable = asPoints(intervalJudgement.preferable());
        Set<Point> inferior = asPoints(intervalJudgement.inferior());
        return dataFactory.newPairwiseTrainingObject(makePrecise(preferable), makePrecise(inferior));
    }

    private Set<Point> asPoints(Set<? extends UnclassifiedObject> objects) throws CrossValidationSampleException {
        Set<Point> points = new HashSet<>();

        for (UnclassifiedObject object : objects) {
            if (object instanceof Point) {
                points.add((Point) object);
            } else {
                throw new CrossValidationSampleException("A given type is unsupported by the sample manager: "
                        + object.getClass().getName());
            }
        }

        return points;
    }

    private Set<Point> makePrecise(Set<Point> points) {
        if (points.size() <= 1) {
            return points;
        }

        double avgX = average(points, Point::x);
        double avgY = average(points, Point::y);

        return Collections.singleton(mathDataFactory.newPoint(avgX, avgY));
    }

    private double average(Set<Point> points, Function<Point, Double> valueSupplier) {
        double sum = 0.0;

        for (Point point : points) {
            sum += valueSupplier.apply(point);
        }

        return sum / points.size();
    }
}
