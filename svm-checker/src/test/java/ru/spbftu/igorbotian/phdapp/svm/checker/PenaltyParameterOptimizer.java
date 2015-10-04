package ru.spbftu.igorbotian.phdapp.svm.checker;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.IntervalPairwiseClassifierModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.RankingPairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;

import java.io.IOException;
import java.util.*;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ завимимости точности классификации от параметров классификации.
 * Целью кросс-валидации является получение значения параметра штрафа, при котором точность классификации имеет
 * наибольшее значение.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnClassifierParametersAnalyzer
 * @see BaseChecker
 */
public class PenaltyParameterOptimizer extends BaseChecker {

    private static final double GAUSSIAN_KERNEL_FROM = 1.0;
    private static final double GAUSSIAN_KERNEL_TO = 32.0;
    private static final double GAUSSIAN_KERNEL_STEP = 0.5;

    private static final double PENALTY_FROM = 1.0;
    private static final double PENALTY_TO = 32.0;
    private static final double PENALTY_STEP = 0.5;

    @Ignore
    @Test
    public void testPreciseClassifier() throws IOException, CrossValidationException {
        check(preciseValidators.accuracyDependenceOnClassifierParametersAnalyzer());
    }

    @Ignore
    @Test
    public void testIntervalClassifier() throws IOException, CrossValidationException {
        check(intervalValidators.accuracyDependenceOnClassifierParametersAnalyzer());
    }

    private void check(RankingPairwiseClassifierCrossValidator<MultiClassificationReport> crossValidator)
            throws IOException, CrossValidationException {

        MultiClassificationReport report = crossValidator.validate(classifier,
                applySpecificParameters(
                        withGaussianKernel(GAUSSIAN_KERNEL_FROM, GAUSSIAN_KERNEL_TO, GAUSSIAN_KERNEL_STEP),
                        withPenalty(PENALTY_FROM, PENALTY_TO, PENALTY_STEP)
                )
        );
        HashMap<Double, List<SingleClassificationReport>> reports = divideByPenalty(report);
        System.out.println("Penalty; Min. F-measure; Max. F-measure; Avg. F-measure");

        for (double penalty : reports.keySet()) {
            printStatistics(penalty, reports.get(penalty));
        }
    }

    private LinkedHashMap<Double, List<SingleClassificationReport>> divideByPenalty(MultiClassificationReport report) {
        LinkedHashMap<Double, List<SingleClassificationReport>> result = new LinkedHashMap<>();

        for (SingleClassificationReport r : report.classifications()) {
            double penalty = getParam(IntervalClassifierParameterFactory.PENALTY_PARAM_ID, r.classifierParameters());

            if (!result.containsKey(penalty)) {
                result.put(penalty, new LinkedList<>());
            }

            result.get(penalty).add(r);
            Collections.sort(result.get(penalty), (a, b) -> Double.compare(
                    getParam(IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_ID, a.classifierParameters()),
                    getParam(IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_ID, a.classifierParameters())
            ));
        }

        return result;
    }

    private double getParam(String paramId, Set<? extends ClassifierParameter<?>> params) {
        for (ClassifierParameter<?> param : params) {
            if (param.name().equals(paramId)) {
                if (param.value() instanceof Double) {
                    return (Double) param.value();
                }

                throw new RuntimeException();
            }
        }

        throw new RuntimeException();
    }

    private void printStatistics(double penalty, List<SingleClassificationReport> reports) {
        System.out.println(penalty + "; " + min(reports) + "; " + max(reports) + "; " + average(reports));
    }

    private double max(List<SingleClassificationReport> reports) {
        double max = Double.MIN_VALUE;
        for (SingleClassificationReport report : reports) {
            if (max < report.fMeasure()) {
                max = report.fMeasure();
            }
        }
        return max;
    }

    private double min(List<SingleClassificationReport> reports) {
        double min = Double.MAX_VALUE;
        for (SingleClassificationReport report : reports) {
            if (min > report.fMeasure()) {
                min = report.fMeasure();
            }
        }
        return min;
    }

    private double average(List<SingleClassificationReport> reports) {
        double sum = 0.0;
        for (SingleClassificationReport report : reports) {
            sum += report.fMeasure();
        }
        return sum / reports.size();
    }

    private CrossValidatorParameter<?> withGaussianKernel(double from, double to, double step) {
        return parameters.gaussianKernelParameter(from, from, to, step, step, step);
    }

    private CrossValidatorParameter<?> withPenalty(double from, double to, double step) {
        return parameters.penaltyParameter(from, from, to, step, step, step);
    }

    @Override
    protected Set<PhDAppModule> injectClassifierModules() {
        return Collections.singleton(new IntervalPairwiseClassifierModule());
    }
}
