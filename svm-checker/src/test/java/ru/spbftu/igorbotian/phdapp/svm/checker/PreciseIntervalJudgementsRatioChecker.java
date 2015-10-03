package ru.spbftu.igorbotian.phdapp.svm.checker;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.io.IOException;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора, ориентированной на анализ
 * завимимости точности классификации от процентного соотношения количества точных/интервальных экспертных оценок
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.PreciseIntervalJudgementsRatioAnalyzer
 * @see BaseChecker
 */
public class PreciseIntervalJudgementsRatioChecker extends BaseChecker {

    private static final int FROM = 1;
    private static final int TO = 100;
    private static final int STEP = 1;

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(
                "precise_interval_judgements_ratio_precise.csv",
                preciseValidators.accuracyDependenceOnPreciseIntervalJudgementsRatioAnalyzer(),
                withRatio(FROM, TO, STEP)
        );
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(
                "precise_interval_judgements_ratio_interval.csv",
                intervalValidators.accuracyDependenceOnPreciseIntervalJudgementsRatioAnalyzer(),
                withRatio(FROM, TO, STEP)
        );
    }

    private CrossValidatorParameter<?> withRatio(int from, int to, int step) {
        return parameters.preciseIntervalJudgmentsCountRatio(from, from, to, step, step, step);
    }
}
