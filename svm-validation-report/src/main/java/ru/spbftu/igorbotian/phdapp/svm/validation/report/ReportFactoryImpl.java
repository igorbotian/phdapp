package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.util.List;
import java.util.Set;

/**
 * Реализация фабрики различных отчётов по работе классификатора
 *
 * @see ReportFactory
 */
class ReportFactoryImpl implements ReportFactory {

    @Override
    public SingleClassificationReport newSingleClassificationReport(Set<? extends ClassifierParameter<?>> classifierParams,
                                                                    Set<? extends CrossValidatorParameter<?>> crossValidatorParams,
                                                                    float accuracy,
                                                                    float precision,
                                                                    float recall) {
        return new SingleClassificationReportImpl(classifierParams, crossValidatorParams, accuracy, precision, recall);
    }

    @Override
    public MultiClassificationReport newMultiClassificationReport(List<SingleClassificationReport> classifications) {
        return new MultiClassificationReportImpl(classifications);
    }
}
