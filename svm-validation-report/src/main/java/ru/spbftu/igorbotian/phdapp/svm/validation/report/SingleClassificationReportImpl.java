package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.util.Collections;
import java.util.Set;

/**
 * Реализация интерфейса <code>SingleClassificationReport</code>
 *
 * @see SingleClassificationReport
 */
class SingleClassificationReportImpl implements SingleClassificationReport {

    private final double accuracy;
    private final double precision;
    private final double recall;
    private final Set<? extends ClassifierParameter<?>> classifierParams;
    private final Set<? extends CrossValidatorParameter<?>> crossValidatorParams;

    public SingleClassificationReportImpl(Set<? extends ClassifierParameter<?>> classifierParams,
                                          Set<? extends CrossValidatorParameter<?>> crossValidatorParams,
                                          final double accuracy,
                                          final double precision,
                                          final double recall) {
        this.accuracy = checkLimits(accuracy, 0.0, 1.0);
        this.precision = checkLimits(precision, 0.0, 1.0);
        this.recall = checkLimits(recall, 0.0, 1.0);
        this.classifierParams = Collections.unmodifiableSet(classifierParams);
        this.crossValidatorParams = Collections.unmodifiableSet(crossValidatorParams);
    }

    private double checkLimits(double n, double min, double max) {
        if (min > n || n > max) {
            throw new IllegalArgumentException(String.format("A given number (%.5f) should be in range of [%.5f;%.5f]",
                    n, min, max));
        }

        return n;
    }

    @Override
    public Set<? extends ClassifierParameter<?>> classifierParameters() {
        return classifierParams;
    }

    @Override
    public Set<? extends CrossValidatorParameter<?>> crossValidatorParameters() {
        return crossValidatorParams;
    }

    @Override
    public double accuracy() {
        return accuracy;
    }

    @Override
    public double precision() {
        return precision;
    }

    @Override
    public double recall() {
        return recall;
    }

    @Override
    public double fMeasure() {
        return 2 * (precision * recall) / (precision + recall);
    }
}
