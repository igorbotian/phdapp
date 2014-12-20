package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Модульный тест для класса <code>CrossValidationSampleManager</code>
 */
public class CrossValidationSampleManagerTest {

    /**
     * Тестируемый класс
     */
    private CrossValidationSampleManager sampleManager;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(
                new DataModule(),
                new ApplicationConfigurationModule(Paths.get(".")),
                new SvmValidationSampleManagementModule()
        );
        sampleManager = injector.getInstance(CrossValidationSampleManager.class);
    }

    @Test
    public void testGenerateSample() throws CrossValidationSampleException {
        int sampleSize = 50;
        Assert.assertEquals(sampleSize, sampleManager.generateSample(sampleSize).objects().size());
    }

    /*
    // this test requires too long time
    @Test
    public void testGenerateSampleWithMaximumSize() throws CrossValidationSampleException {
        int sampleSize = CrossValidatorParameterFactory.SAMPLE_SIZE_MAX;
        Assert.assertEquals(sampleSize, sampleManager.generateSample(sampleSize).objects().size());
    }*/

    @Test
    public void testGenerateSampleWithMinimumSize() throws CrossValidationSampleException {
        int sampleSize = CrossValidatorParameterFactory.SAMPLE_SIZE_MIN;
        Assert.assertEquals(sampleSize, sampleManager.generateSample(sampleSize).objects().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateSampleWithLessThanMinimumSize() throws CrossValidationSampleException {
        int sampleSize = CrossValidatorParameterFactory.SAMPLE_SIZE_MIN - 1;
        Assert.assertEquals(sampleSize, sampleManager.generateSample(sampleSize).objects().size());
    }

    @Test
    public void testDivideSampleIntoTwoSets() throws CrossValidationSampleException {
        testDivideSampleIntoTwoSets(10, 50, 5, 5);
        testDivideSampleIntoTwoSets(10, 1, 2, 8);
        testDivideSampleIntoTwoSets(10, 100, 8, 2);
        testDivideSampleIntoTwoSets(11, 50, 6, 6);
    }

    private void testDivideSampleIntoTwoSets(int sampleSize, int ratio,
                                             int expectedFirstPartSize, int expectedSecondPartSize)

            throws CrossValidationSampleException {

        ClassifiedData sample = sampleManager.generateSample(sampleSize);
        Pair<ClassifiedData, ClassifiedData> parts = sampleManager.divideSampleIntoTwoGroups(sample, ratio);
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
        ClassifiedData sample = sampleManager.generateSample(anySize);
        sampleManager.divideSampleIntoTwoGroups(sample,
                CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MIN - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideSampleIntoTwoSetsWithGreaterThanMaximumRatio() throws CrossValidationSampleException {
        int anySize = 10;
        ClassifiedData sample = sampleManager.generateSample(anySize);
        sampleManager.divideSampleIntoTwoGroups(sample,
                CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MAX + 1);
    }

    @Test
    public void testGenerateTrainingSet() throws CrossValidationSampleException {
        int maxJudgementGroupSize = 5; /* any */

        testGenerateTrainingSet(10, 0, maxJudgementGroupSize, new int[] {0}, 5);
        // 50% от 10 это 5 объектов точных оценок - нечётное => +1 -=> 3 точных оценки
        testGenerateTrainingSet(10, 50, maxJudgementGroupSize, new int[] {1}, 3);
        testGenerateTrainingSet(10, 60, maxJudgementGroupSize, new int[] {1}, 2);
        testGenerateTrainingSet(10, 100, maxJudgementGroupSize, new int[] {1, 2, 3}, 0);
    }

    private void testGenerateTrainingSet(int sampleSize, int ratio, int maxJudgementGroupSize,
                                         int[] possibleIntervalJudgementsCounts, int expectedPreciseJudgementsCount)
            throws CrossValidationSampleException {

        ClassifiedData sample = sampleManager.generateSample(sampleSize);
        PairwiseTrainingSet trainingSet = sampleManager.generateTrainingSet(sample, ratio, maxJudgementGroupSize);
        int intervalJudgementsCount = numberOfIntervalJudgements(trainingSet);
        int preciseJudgementsCount = numberOfPreciseJudgements(trainingSet);

        Assert.assertEquals(expectedPreciseJudgementsCount, preciseJudgementsCount);

        boolean intervalJudgementsCountIsRight = false;

        for(int possibleIntervalJudgementsCount : possibleIntervalJudgementsCounts) {
            if(possibleIntervalJudgementsCount == intervalJudgementsCount) {
                intervalJudgementsCountIsRight = true;
            }
        }

        Assert.assertTrue(intervalJudgementsCountIsRight);
    }

    private int numberOfPreciseJudgements(PairwiseTrainingSet set) {
        int count = 0;

        for(PairwiseTrainingObject obj : set.objects()) {
            if(obj.preferable().size() == 1 && obj.inferior().size() == 1) {
                count++;
            }
        }

        return count;
    }

    private int numberOfIntervalJudgements(PairwiseTrainingSet set) {
        int count = 0;

        for(PairwiseTrainingObject obj : set.objects()) {
            if(obj.preferable().size() > 1 || obj.inferior().size() > 1) {
                count++;
            }
        }

        return count;
    }
}
