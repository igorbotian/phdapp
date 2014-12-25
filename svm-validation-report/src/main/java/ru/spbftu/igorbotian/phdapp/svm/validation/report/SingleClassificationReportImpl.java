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
                                          float accuracy,
                                          float precision,
                                          float recall) {

        this.accuracy = round(checkLimits(accuracy, 0.0f, 1.0f));
        this.precision = round(checkLimits(precision, 0.0f, 1.0f));
        this.recall = round(checkLimits(recall, 0.0f, 1.0f));
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

    private float round(float f) {
        return Float.valueOf(ROUNDED_DECIMAL_FORMAT.format(f));
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

    @Override
    public float fMeasure() {
        return (precision == 0 && recall == 0) ? 0.0f : 2 * (precision * recall) / (precision + recall);
    }
}
