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
    private double avgAccuracy;
    private double minAccuracy;
    private double maxAccuracy;
    private double avgPrecision;
    private double minPrecision;
    private double maxPrecision;
    private double avgRecall;
    private double minRecall;
    private double maxRecall;
    private double avgFMeasure;
    private double minFMeasure;
    private double maxFMeasure;
    private int indexOfMaxPrecisionClassification;
    private int indexOfMinPrecisionClassification;

    MultiClassificationReportImpl(List<SingleClassificationReport> classifications) {
        if (classifications.isEmpty()) {
            throw new IllegalArgumentException("At least one classification should have been done");
        }

        this.classifications = Collections.unmodifiableList(Objects.requireNonNull(classifications));
        double accuracySum = 0.0;
        double minAccuracy = 1.0;
        double maxAccuracy = 0.0;
        double precisionSum = 0.0;
        double minPrecision = 1.0;
        double maxPrecision = 0.0;
        double recallSum = 0.0;
        double minRecall = 1.0;
        double maxRecall = 0.0;
        double fMeasureSum = 0.0;
        double minFMeasure = 1.0;
        double maxFMeasure = 0.0;

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

            fMeasureSum += report.fMeasure();

            if (report.fMeasure() > maxFMeasure) {
                maxFMeasure = report.fMeasure();
            }

            if (report.fMeasure() < minFMeasure) {
                minFMeasure = report.fMeasure();
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
        this.avgFMeasure = fMeasureSum / this.classifications.size();
        this.minFMeasure = minFMeasure;
        this.maxFMeasure = maxFMeasure;
        this.indexOfMaxPrecisionClassification = indexOfMaxPrecisionClassification;
        this.indexOfMinPrecisionClassification = indexOfMinPrecisionClassification;
    }

    @Override
    public int numberOfClassifications() {
        return classifications.size();
    }

    @Override
    public double averageAccuracy() {
        return avgAccuracy;
    }

    @Override
    public double minAccuracy() {
        return minAccuracy;
    }

    @Override
    public double maxAccuracy() {
        return maxAccuracy;
    }

    @Override
    public double averagePrecision() {
        return avgPrecision;
    }

    @Override
    public double minPrecision() {
        return minPrecision;
    }

    @Override
    public double maxPrecision() {
        return maxPrecision;
    }

    @Override
    public double averageRecall() {
        return avgRecall;
    }

    @Override
    public double minRecall() {
        return minRecall;
    }

    @Override
    public double maxRecall() {
        return maxRecall;
    }

    @Override
    public double averageFMeasure() {
        return avgFMeasure;
    }

    @Override
    public double minFMeasure() {
        return minFMeasure;
    }

    @Override
    public double maxFMeasure() {
        return maxFMeasure;
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
