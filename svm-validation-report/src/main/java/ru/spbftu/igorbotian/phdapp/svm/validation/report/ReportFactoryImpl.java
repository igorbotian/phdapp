package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import java.util.List;

/**
 * Реализация фабрики различных отчётов по работе классификатора
 *
 * @see ReportFactory
 */
class ReportFactoryImpl implements ReportFactory {

    @Override
    public SingleClassificationReport newSingleClassificationReport(int sampleSize,
                                                                    float constantCostParameter,
                                                                    float gaussianKernelParameter,
                                                                    float judgedSampleItemsRatio,
                                                                    float preciseIntervalSampleItemsRatio,
                                                                    float accuracy,
                                                                    float precision,
                                                                    float recall) {
        return new SingleClassificationReportImpl(sampleSize, constantCostParameter, gaussianKernelParameter,
                judgedSampleItemsRatio, preciseIntervalSampleItemsRatio, accuracy, precision, recall);
    }

    @Override
    public MultiClassificationReport newMultiClassificationReport(List<SingleClassificationReport> classifications) {
        return new MultiClassificationReportImpl(classifications);
    }
}
