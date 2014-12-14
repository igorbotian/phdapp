package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Реализация интерфейса <code>MultiClassificationReport</code>
 *
 * @see MultiClassificationReport
 */
class MultiClassificationReportImpl implements MultiClassificationReport {

    private final List<SingleClassificationReport> classifications;
    private float avgAccuracy;
    private float minAccuracy;
    private float maxAccuracy;
    private float avgPrecision;
    private float minPrecision;
    private float maxPrecision;
    private float avgRecall;
    private float minRecall;
    private float maxRecall;
    private int indexOfMaxPrecisionClassification;
    private int indexOfMinPrecisionClassification;

    MultiClassificationReportImpl(List<SingleClassificationReport> classifications) {
        if (classifications.isEmpty()) {
            throw new IllegalArgumentException("At least one classification should have been done");
        }

        this.classifications = Collections.unmodifiableList(Objects.requireNonNull(classifications));
        float accuracySum = 0.0f;
        float minAccuracy = 1.0f;
        float maxAccuracy = 0.0f;
        float precisionSum = 0.0f;
        float minPrecision = 1.0f;
        float maxPrecision = 0.0f;
        float recallSum = 0.0f;
        float minRecall = 1.0f;
        float maxRecall = 0.0f;

        int indexOfMaxPrecisionClassification = 0;
        int indexOfMinPrecisionClassification = 0;
        Iterator<SingleClassificationReport> it = this.classifications.iterator();

        for (int i = 0; it.hasNext(); i++) {
            SingleClassificationReport report = it.next();

            accuracySum += report.accuracy();

            if (report.accuracy() > maxAccuracy) {
                maxAccuracy = report.accuracy();
            }

            if (report.accuracy() < minAccuracy) {
                minAccuracy = report.accuracy();
            }

            precisionSum += report.precision();

            if (report.precision() > maxPrecision) {
                maxPrecision = report.precision();
                indexOfMaxPrecisionClassification = i;
            }

            if (report.precision() < minPrecision) {
                minPrecision = report.precision();
                indexOfMinPrecisionClassification = i;
            }

            recallSum += report.recall();

            if (report.recall() > maxRecall) {
                maxRecall = report.recall();
            }

            if (report.recall() < minRecall) {
                minRecall = report.recall();
            }
        }

        this.avgAccuracy = accuracySum / this.classifications.size();
        this.minAccuracy = minAccuracy;
        this.maxAccuracy = maxAccuracy;
        this.avgPrecision = precisionSum / this.classifications.size();
        this.minPrecision = minPrecision;
        this.maxPrecision = maxPrecision;
        this.avgRecall = recallSum / this.classifications.size();
        this.minRecall = minRecall;
        this.maxRecall = maxRecall;
        this.indexOfMaxPrecisionClassification = indexOfMaxPrecisionClassification;
        this.indexOfMinPrecisionClassification = indexOfMinPrecisionClassification;
    }

    @Override
    public int numberOfClassifications() {
        return classifications.size();
    }

    @Override
    public float averageAccuracy() {
        return avgAccuracy;
    }

    @Override
    public float minAccuracy() {
        return minAccuracy;
    }

    @Override
    public float maxAccuracy() {
        return maxAccuracy;
    }

    @Override
    public float averagePrecision() {
        return avgPrecision;
    }

    @Override
    public float minPrecision() {
        return minPrecision;
    }

    @Override
    public float maxPrecision() {
        return maxPrecision;
    }

    @Override
    public float averageRecall() {
        return avgRecall;
    }

    @Override
    public float minRecall() {
        return minRecall;
    }

    @Override
    public float maxRecall() {
        return maxRecall;
    }

    @Override
    public SingleClassificationReport max() {
        return classifications.get(indexOfMaxPrecisionClassification);
    }

    @Override
    public SingleClassificationReport min() {
        return classifications.get(indexOfMinPrecisionClassification);
    }

    @Override
    public List<SingleClassificationReport> classifications() {
        return classifications;
    }
}
