package ru.spbftu.igorbotian.phdapp.svm.checker;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.io.IOException;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ зависимости точности классификации от размера обучающей выборки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnSampleSizeAnalyzer
 * @see BaseChecker
 */
public class SampleSizeChecker extends BaseChecker {

    private static final int FROM = 4;
    private static final int TO = 500;
    private static final int STEP = 4;

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "sample_size_precise",
                preciseValidators.accuracyDependenceOnSampleSizeAnalyzer(),
                withSampleSize(FROM, TO, STEP)
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "sample_size_interval",
                intervalValidators.accuracyDependenceOnSampleSizeAnalyzer(),
                withSampleSize(FROM, TO, STEP)
        );
    }

    private CrossValidatorParameter<?> withSampleSize(int from, int to, int step) {
        return parameters.sampleSize(from, from, to, step, step, step);
    }
}
