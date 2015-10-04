package ru.spbftu.igorbotian.phdapp.svm.checker;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.io.IOException;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ завимимости точности классификации от параметров классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnClassifierParametersAnalyzer
 * @see BaseChecker
 */
public class ClassifierParametersChecker extends BaseChecker {

    private static final double GAUSSIAN_KERNEL_FROM = 1.0;
    private static final double GAUSSIAN_KERNEL_TO = 32.0;
    private static final double GAUSSIAN_KERNEL_STEP = 1.0;

    private static final double PENALTY_FROM = 13.0;
    private static final double PENALTY_TO = 13.0;
    private static final double PENALTY_STEP = 0.5;

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "classifier_params_precise",
                preciseValidators.accuracyDependenceOnClassifierParametersAnalyzer(),
                withGaussianKernel(GAUSSIAN_KERNEL_FROM, GAUSSIAN_KERNEL_TO, GAUSSIAN_KERNEL_STEP),
                withPenalty(PENALTY_FROM, PENALTY_TO, PENALTY_STEP)
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "classifier_params_interval",
                intervalValidators.accuracyDependenceOnClassifierParametersAnalyzer(),
                withGaussianKernel(GAUSSIAN_KERNEL_FROM, GAUSSIAN_KERNEL_TO, GAUSSIAN_KERNEL_STEP),
                withPenalty(PENALTY_FROM, PENALTY_TO, PENALTY_STEP)
        );
    }

    private CrossValidatorParameter<?> withGaussianKernel(double from, double to, double step) {
        return parameters.gaussianKernelParameter(from, from, to, step, step, step);
    }

    private CrossValidatorParameter<?> withPenalty(double from, double to, double step) {
        return parameters.penaltyParameter(from, from, to, step, step, step);
    }
}
