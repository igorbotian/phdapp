package ru.spbftu.igorbotian.phdapp.svm.checker;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.io.IOException;

/**
 * Абстрактный механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ завимимости точности классификации от параметра штрафа
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnClassifierParametersAnalyzer
 * @see BaseChecker
 */
public abstract class PenaltyParameterChecker extends BaseChecker {

    private static final double PENALTY_FROM = 1.0;
    private static final double PENALTY_TO = 32.0;
    private static final double PENALTY_STEP = 0.5;

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "penalty_param_precise",
                preciseValidators.accuracyDependenceOnClassifierParametersAnalyzer(),
                withPenalty(PENALTY_FROM, PENALTY_TO, PENALTY_STEP),
                withGaussianKernel(GAUSSIAN_KERNEL_PARAMETER)
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "penalty_param_interval",
                intervalValidators.accuracyDependenceOnClassifierParametersAnalyzer(),
                withPenalty(PENALTY_FROM, PENALTY_TO, PENALTY_STEP),
                withGaussianKernel(GAUSSIAN_KERNEL_PARAMETER)
        );
    }

    private CrossValidatorParameter<?> withPenalty(double from, double to, double step) {
        return parameters.penaltyParameter(from, from, to, step, step, step);
    }

    private CrossValidatorParameter<?> withGaussianKernel(double value) {
        return parameters.gaussianKernelParameter(value, value, value, 1.0, 1.0, 1.0);
    }
}
