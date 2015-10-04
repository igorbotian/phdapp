package ru.spbftu.igorbotian.phdapp.svm.checker;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;

import java.io.IOException;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на среднее значение точности серии попарных классификаций
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AverageAccuracyValidator
 * @see BaseChecker
 */
public class AverageAccuracyChecker extends BaseChecker {

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "average_accuracy_precise",
                preciseValidators.averageAccuracyValidator()
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "average_accuracy_interval",
                intervalValidators.averageAccuracyValidator()
        );
    }
}
