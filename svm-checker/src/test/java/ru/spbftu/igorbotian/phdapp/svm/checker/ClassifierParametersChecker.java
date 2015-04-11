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
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.PrecisionDependenceOnClassifierParametersAnalyzer
 * @see BaseChecker
 */
public class ClassifierParametersChecker extends BaseChecker {

    private static final double GAUSSIAN_KERNEL_FROM = 0.2;
    private static final double GAUSSIAN_KERNEL_TO = 5.0;
    private static final double GAUSSIAN_KERNEL_STEP = 0.2;

    private static final double PENALTY_FROM = 1.0;
    private static final double PENALTY_TO = 32.0;
    private static final double PENALTY_STEP = 0.2;

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "classifier_params_precise.csv",
                preciseValidators.precisionDependenceOnClassifierParametersAnalyzer(),
                withGaussianKernel(GAUSSIAN_KERNEL_FROM, GAUSSIAN_KERNEL_TO, GAUSSIAN_KERNEL_STEP),
                withPenalty(PENALTY_FROM, PENALTY_TO, PENALTY_STEP)
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "classifier_params_interval.csv",
                intervalValidators.precisionDependenceOnClassifierParametersAnalyzer(),
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
