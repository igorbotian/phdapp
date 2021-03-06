package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataModule;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;

import java.nio.file.Paths;

/**
 * Модульный тест для класса <code>IntervalCrossValidationSampleManager</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class IntervalCrossValidationSampleManagerTest extends CrossValidationSampleManagerTest {

    /**
     * Тестируемый класс
     */
    private IntervalCrossValidationSampleManager sampleManager;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(
                new DataModule(),
                new ApplicationConfigurationModule(Paths.get("..")),
                new SvmValidationSampleManagementModule(),
                new SvmValidationIntervalSampleManagementModule()
        );
        sampleManager = injector.getInstance(IntervalCrossValidationSampleManager.class);
    }

    @Test
    public void testDivideSampleIntoTwoSets() throws CrossValidationSampleException {
        testDivideSampleIntoTwoSets(10, 50, 5, 5);
        testDivideSampleIntoTwoSets(10, 1, 2, 8);
        testDivideSampleIntoTwoSets(10, 100, 8, 2);
        testDivideSampleIntoTwoSets(11, 50, 6, 6);
    }

    @Test
    public void testGenerateTrainingSet() throws CrossValidationSampleException {
        int maxJudgementGroupSize = 5; /* any */

        testGenerateTrainingSet(10, 1, maxJudgementGroupSize, new int[]{0}, 5);
        // 50% от 10 это 5 объектов точных оценок - нечётное => +1 -=> 3 точных оценки
        testGenerateTrainingSet(10, 50, maxJudgementGroupSize, new int[]{1}, 3);
        testGenerateTrainingSet(10, 60, maxJudgementGroupSize, new int[]{1}, 2);
        testGenerateTrainingSet(10, 100, maxJudgementGroupSize, new int[]{1, 2, 3}, 0);
    }

    @Override
    protected CrossValidationSampleManager sampleManager() {
        return sampleManager;
    }
}
