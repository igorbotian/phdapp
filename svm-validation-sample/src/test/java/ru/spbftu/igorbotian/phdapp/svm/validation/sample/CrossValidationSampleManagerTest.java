package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Igor Botian <igor.botian@gmail.com>
 */
public abstract class CrossValidationSampleManagerTest {

    protected abstract CrossValidationSampleManager sampleManager();

    @Test
    public void testGenerateSample() throws CrossValidationSampleException {
        int sampleSize = 50;
        Assert.assertEquals(sampleSize, sampleManager().generateSample(sampleSize).objects().size());
    }

    @Test
    public void testGenerateSampleWithMinimumSize() throws CrossValidationSampleException {
        int sampleSize = CrossValidatorParameterFactory.SAMPLE_SIZE_MIN;
        Assert.assertEquals(sampleSize, sampleManager().generateSample(sampleSize).objects().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateSampleWithLessThanMinimumSize() throws CrossValidationSampleException {
        int sampleSize = CrossValidatorParameterFactory.SAMPLE_SIZE_MIN - 1;
        Assert.assertEquals(sampleSize, sampleManager().generateSample(sampleSize).objects().size());
    }

    protected void testDivideSampleIntoTwoSets(int sampleSize, int ratio,
                                             int expectedFirstPartSize, int expectedSecondPartSize)

            throws CrossValidationSampleException {

        ClassifiedData sample = sampleManager().generateSample(sampleSize);
        Pair<ClassifiedData, ClassifiedData> parts = sampleManager().divideSampleIntoTwoGroups(sample, ratio);
        Assert.assertEquals(expectedFirstPartSize, parts.first.objects().size());
        Assert.assertEquals(expectedSecondPartSize, parts.second.objects().size());

        Set<ClassifiedObject> firstPartObjects = new HashSet<>(parts.first.objects());
        firstPartObjects.removeAll(parts.second.objects());
        Assert.assertEquals(expectedFirstPartSize, firstPartObjects.size());

        Set<ClassifiedObject> secondPartObjects = new HashSet<>(parts.second.objects());
        firstPartObjects.removeAll(parts.first.objects());
        Assert.assertEquals(expectedSecondPartSize, secondPartObjects.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideSampleIntoTwoSetsWithLesserThanMinimumRatio() throws CrossValidationSampleException {
        int anySize = 10;
        ClassifiedData sample = sampleManager().generateSample(anySize);
        sampleManager().divideSampleIntoTwoGroups(sample,
                CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MIN - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideSampleIntoTwoSetsWithGreaterThanMaximumRatio() throws CrossValidationSampleException {
        int anySize = 10;
        ClassifiedData sample = sampleManager().generateSample(anySize);
        sampleManager().divideSampleIntoTwoGroups(sample,
                CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MAX + 1);
    }

    protected void testGenerateTrainingSet(int sampleSize, int ratio, int maxJudgementGroupSize,
                                         int[] possibleIntervalJudgementsCounts, int expectedPreciseJudgementsCount)
            throws CrossValidationSampleException {

        ClassifiedData sample = sampleManager().generateSample(sampleSize);
        PairwiseTrainingSet trainingSet = sampleManager().generateTrainingSet(sample, ratio, maxJudgementGroupSize,
                (f, s) -> 1 /* any */);
        int intervalJudgementsCount = numberOfIntervalJudgements(trainingSet);
        int preciseJudgementsCount = numberOfPreciseJudgements(trainingSet);

        Assert.assertEquals(expectedPreciseJudgementsCount, preciseJudgementsCount);

        boolean intervalJudgementsCountIsRight = false;

        for (int possibleIntervalJudgementsCount : possibleIntervalJudgementsCounts) {
            if (possibleIntervalJudgementsCount == intervalJudgementsCount) {
                intervalJudgementsCountIsRight = true;
            }
        }

        Assert.assertTrue(intervalJudgementsCountIsRight);
    }

    private int numberOfPreciseJudgements(PairwiseTrainingSet set) {
        int count = 0;

        for (Judgement obj : set.judgements()) {
            if (obj.preferable().size() == 1 && obj.inferior().size() == 1) {
                count++;
            }
        }

        return count;
    }

    private int numberOfIntervalJudgements(PairwiseTrainingSet set) {
        int count = 0;

        for (Judgement obj : set.judgements()) {
            if (obj.preferable().size() > 1 || obj.inferior().size() > 1) {
                count++;
            }
        }

        return count;
    }
}
