package ru.spbftu.igorbotian.phdapp.svm.checker;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.io.IOException;

/**
 * Абстрактный механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ завимимости точности классификации от параметра Гауссова ядра
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnClassifierParametersAnalyzer
 * @see BaseChecker
 */
public abstract class GaussianKernelParameterChecker extends BaseChecker {

    private static final double GAUSSIAN_KERNEL_FROM = 1.0;
    private static final double GAUSSIAN_KERNEL_TO = 32.0;
    private static final double GAUSSIAN_KERNEL_STEP = 0.5;

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "gaussian_kernel_param_precise",
                preciseValidators.accuracyDependenceOnClassifierParametersAnalyzer(),
                withGaussianKernel(GAUSSIAN_KERNEL_FROM, GAUSSIAN_KERNEL_TO, GAUSSIAN_KERNEL_STEP),
                withPenalty(PENALTY_PARAMETER)
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "gaussian_kernel_param_interval",
                intervalValidators.accuracyDependenceOnClassifierParametersAnalyzer(),
                withGaussianKernel(GAUSSIAN_KERNEL_FROM, GAUSSIAN_KERNEL_TO, GAUSSIAN_KERNEL_STEP),
                withPenalty(PENALTY_PARAMETER)
        );
    }

    private CrossValidatorParameter<?> withGaussianKernel(double from, double to, double step) {
        return parameters.gaussianKernelParameter(from, from, to, step, step, step);
    }

    private CrossValidatorParameter<?> withPenalty(double value) {
        return parameters.penaltyParameter(value, value, value, 1.0, 1.0, 1.0);
    }
}
