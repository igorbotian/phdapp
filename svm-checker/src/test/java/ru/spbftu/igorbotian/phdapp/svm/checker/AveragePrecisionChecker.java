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
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AveragePrecisionValidator
 * @see BaseChecker
 */
public class AveragePrecisionChecker extends BaseChecker {

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "average_precision_precise.csv",
                preciseValidators.averagePrecisionValidator()
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "average_precision_interval.csv",
                intervalValidators.averagePrecisionValidator()
        );
    }

    @Ignore
    @Test
    public void testHausdorffIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "average_precision_interval_hausdorff.csv",
                intervalValidators.averagePrecisionValidator()
        );
    }
}
