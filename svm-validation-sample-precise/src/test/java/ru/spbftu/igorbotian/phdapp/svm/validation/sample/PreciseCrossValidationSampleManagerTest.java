package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataModule;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;

import java.nio.file.Paths;

/**
 * Модульный тест для класса <code>PreciseCrossValidationSampleManager</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class PreciseCrossValidationSampleManagerTest extends CrossValidationSampleManagerTest {

    /**
     * Тестируемый класс
     */
    private PreciseCrossValidationSampleManager sampleManager;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(
                new DataModule(),
                new ApplicationConfigurationModule(Paths.get("..")),
                new SvmValidationSampleManagementModule(),
                new SvmValidationPreciseSampleManagementModule()
        );
        sampleManager = injector.getInstance(PreciseCrossValidationSampleManager.class);
    }

    @Test
    public void testDivideSampleIntoTwoSets() throws CrossValidationSampleException {
        testDivideSampleIntoTwoSets(10, 50, 6, 4);
        testDivideSampleIntoTwoSets(12, 50, 6, 6);
        testDivideSampleIntoTwoSets(10, 1, 2, 8);
        testDivideSampleIntoTwoSets(10, 100, 8, 2);
    }

    @Test
    public void testGenerateTrainingSet() throws CrossValidationSampleException {
        int maxJudgementGroupSize = 1; /* any */

        testGenerateTrainingSet(10, 1, maxJudgementGroupSize, new int[]{0}, 5);
        testGenerateTrainingSet(10, 50, maxJudgementGroupSize, new int[]{0}, 5);
        testGenerateTrainingSet(10, 60, maxJudgementGroupSize, new int[]{0}, 5);
        testGenerateTrainingSet(10, 100, maxJudgementGroupSize, new int[]{0}, 5);
    }

    @Override
    protected CrossValidationSampleManager sampleManager() {
        return sampleManager;
    }
}
