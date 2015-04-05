package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация средства формирования выборки для кросс-валидации ранжирующего попарного классификатора,
 * поддерживающего только точные экспертные оценки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
@Singleton
class PreciseCrossValidationSampleManagerImpl implements PreciseCrossValidationSampleManager {

    private final DataFactory dataFactory;
    private final CrossValidationSampleGenerator sampleGenerator;

    @Inject
    public PreciseCrossValidationSampleManagerImpl(DataFactory dataFactory,
                                                   CrossValidationSampleGenerator sampleGenerator) {

        this.dataFactory = Objects.requireNonNull(dataFactory);
        this.sampleGenerator = Objects.requireNonNull(sampleGenerator);
    }

    @Override
    public CrossValidationSampleGenerator sampleGenerator() {
        return sampleGenerator;
    }

    @Override
    public ClassifiedData generateSample(int sampleSize) throws CrossValidationSampleException {
        checkSampleSize(sampleSize);

        sampleGenerator.regeneratePoints(sampleSize);
        Set<Point> first = sampleGenerator.firstSetOfPoints();
        Set<Point> second = sampleGenerator.secondSetOfPoints();

        Set<DataClass> classes = Stream.of(
                first.iterator().next().dataClass(),
                second.iterator().next().dataClass()
        ).collect(Collectors.toSet());

        Set<ClassifiedObject> objects = new HashSet<>();
        objects.addAll(first);
        objects.addAll(second);

        try {
            return dataFactory.newClassifiedData(classes, objects);
        } catch (DataException e) {
            throw new CrossValidationSampleException("Failed to generate sample", e);
        }
    }

    @Override
    public Pair<ClassifiedData, ClassifiedData> divideSampleIntoTwoGroups(ClassifiedData sample, int ratio)
            throws CrossValidationSampleException {

        Objects.requireNonNull(sample);
        checkSampleSize(sample.objects().size());
        checkRatio(ratio);

        int sampleSize = sample.objects().size();
        int trainingSetSize = (sampleSize * ratio / 100);

        if(trainingSetSize == 0) {
            trainingSetSize = 2;
        } else if (trainingSetSize % 2 != 0) {
            trainingSetSize++;
            assert (sampleSize - trainingSetSize) % 2 == 0;
        } else if(sampleSize == trainingSetSize) {
            trainingSetSize -= 2;
        }

        Set<ClassifiedObject> trainingSet = new HashSet<>();
        Set<ClassifiedObject> testingSet = new HashSet<>();

        int i = 0;
        Iterator<? extends ClassifiedObject> it = sample.objects().iterator();

        while (i < trainingSetSize) {
            assert it.hasNext();
            trainingSet.add(it.next());
            i++;
        }

        while (it.hasNext()) {
            testingSet.add(it.next());
        }

        try {
            return new Pair<>(
                    dataFactory.newClassifiedData(sample.classes(), trainingSet),
                    dataFactory.newClassifiedData(sample.classes(), testingSet)
            );
        } catch (DataException e) {
            throw new CrossValidationSampleException("Failed to divide a given sample into two groups", e);
        }
    }

    @Override
    public PairwiseTrainingSet generateTrainingSet(ClassifiedData source, int ratio, int maxJudgementGroupSize,
                                                   BiFunction<Set<? extends ClassifiedObject>,
                                                           Set<? extends ClassifiedObject>, Integer> expertFunction)
            throws CrossValidationSampleException {

        Objects.requireNonNull(source);
        Objects.requireNonNull(expertFunction);
        checkSampleSize(source.objects().size());
        checkRatio(ratio);

        if (maxJudgementGroupSize != 1) {
            throw new IllegalArgumentException("Maximum judgement group size should be 1 for a classifier " +
                    "which supports precise judgements only");
        }

        Set<Judgement> judgements = new HashSet<>();
        Iterator<? extends ClassifiedObject> it = source.objects().iterator();

        while(it.hasNext()) {
            Set<ClassifiedObject> first = Collections.singleton(it.next());
            assert it.hasNext();
            Set<ClassifiedObject> second = Collections.singleton(it.next());

            int isFirstPreferable = expertFunction.apply(first, second);

            judgements.add(dataFactory.newPairwiseTrainingObject(
                    isFirstPreferable > 0 ? first : second,
                    isFirstPreferable > 0 ? second : first
            ));
        }

        return dataFactory.newPairwiseTrainingSet(judgements);
    }

    private void checkSampleSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Sample size should have a positive value: " + size);
        }

        if (size % 2 != 0) {
            throw new IllegalArgumentException("Sample size should have an even value: " + size);
        }
    }

    private void checkRatio(int ratio) {
        if (ratio < 1 || ratio > 100) {
            throw new IllegalArgumentException("Ratio is out of range (0..100): " + ratio);
        }
    }
}
