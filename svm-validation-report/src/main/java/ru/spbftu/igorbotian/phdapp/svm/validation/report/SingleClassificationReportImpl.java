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

    private final float accuracy;
    private final float precision;
    private final float recall;
    private final Set<? extends ClassifierParameter<?>> classifierParams;
    private final Set<? extends CrossValidatorParameter<?>> crossValidatorParams;

    public SingleClassificationReportImpl(Set<? extends ClassifierParameter<?>> classifierParams,
                                          Set<? extends CrossValidatorParameter<?>> crossValidatorParams,
                                          final float accuracy,
                                          final float precision,
                                          final float recall) {
        this.accuracy = checkLimits(accuracy, 0.0f, 1.0f);
        this.precision = checkLimits(precision, 0.0f, 1.0f);
        this.recall = checkLimits(recall, 0.0f, 1.0f);
        this.classifierParams = Collections.unmodifiableSet(classifierParams);
        this.crossValidatorParams = Collections.unmodifiableSet(crossValidatorParams);
    }

    private float checkLimits(float n, float min, float max) {
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
    public float accuracy() {
        return accuracy;
    }

    @Override
    public float precision() {
        return precision;
    }

    @Override
    public float recall() {
        return recall;
    }
}
