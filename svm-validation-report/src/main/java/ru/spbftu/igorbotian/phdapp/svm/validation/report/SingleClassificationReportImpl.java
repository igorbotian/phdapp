package ru.spbftu.igorbotian.phdapp.svm.validation.report;

/**
 * Реализация интерфейса <code>SingleClassificationReport</code>
 * @see SingleClassificationReport
 */
class SingleClassificationReportImpl implements SingleClassificationReport {

    private final float accuracy;
    private final float precision;
    private final float recall;
    private final float constantCostParameter;
    private final float gaussianKernelParameter;
    private final int sampleSize;
    private final float judgedSampleItemsRatio;
    private final float preciseIntervalSampleItemsRatio;

    public SingleClassificationReportImpl(final int sampleSize,
                                          final float constantCostParameter,
                                          final float gaussianKernelParameter,
                                          final float judgedSampleItemsRatio,
                                          final float preciseIntervalSampleItemsRatio,
                                          final float accuracy,
                                          final float precision,
                                          final float recall) {
        this.accuracy = checkLimits(accuracy, 0.0f, 1.0f);
        this.precision = checkLimits(precision, 0.0f, 1.0f);
        this.recall = checkLimits(recall, 0.0f, 1.0f);
        this.constantCostParameter = checkLimits(constantCostParameter, 0.0f, Float.MAX_VALUE);
        this.gaussianKernelParameter = gaussianKernelParameter;
        this.sampleSize = (int) checkLimits(sampleSize, 0.0f, Float.MAX_VALUE);
        this.judgedSampleItemsRatio = checkLimits(judgedSampleItemsRatio, 0.0f, 1.0f);
        this.preciseIntervalSampleItemsRatio = checkLimits(preciseIntervalSampleItemsRatio, 0.0f, 1.0f);
    }

    private float checkLimits(float n, float min, float max) {
        if(min > n || n > max) {
            throw new IllegalArgumentException(String.format("A given number (%.5f) should be in range of [%.5f;%.5f]",
                    n, min, max));
        }

        return n;
    }

    @Override
    public int sampleSize() {
        return sampleSize;
    }

    @Override
    public float constantCostParameter() {
        return constantCostParameter;
    }

    @Override
    public float gaussianKernelParameter() {
        return gaussianKernelParameter;
    }

    @Override
    public float judgedSampleItemsRatio() {
        return judgedSampleItemsRatio;
    }

    @Override
    public float preciseIntervalSampleItemsRatio() {
        return preciseIntervalSampleItemsRatio;
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
