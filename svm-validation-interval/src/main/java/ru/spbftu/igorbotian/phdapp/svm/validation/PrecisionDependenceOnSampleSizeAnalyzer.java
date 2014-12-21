package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.ReportFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleException;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;

import java.util.*;

/**
 * Средство анализа зависимости точности классификации от размера обучающей выборки
 */
class PrecisionDependenceOnSampleSizeAnalyzer extends AbstractPairwiseClassifierCrossValidator<MultiClassificationReport> {

    /**
     * Средство кросс-валидации, направленное на точность работы попарного классификатора
     */
    private final PrecisionValidator precisionValidator;

    public PrecisionDependenceOnSampleSizeAnalyzer(CrossValidationSampleManager sampleManager,
                                                      IntervalClassifierParameterFactory classifierParameterFactory,
                                                      CrossValidatorParameterFactory crossValidatorParameterFactory,
                                                      ReportFactory reportFactory,
                                                      PrecisionValidator precisionValidator) {
        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory);
        this.precisionValidator = Objects.requireNonNull(precisionValidator);
    }

    @Override
    protected MultiClassificationReport validate(PairwiseClassifier classifier,
                                                 Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                 CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        CrossValidatorParameter<Integer> sampleSize = specificValidatorParams.sampleSize();
        int lowerBound = sampleSize.lowerBound().value();
        int upperBound = sampleSize.upperBound().value();
        int stepSize = sampleSize.stepSize().value();
        List<SingleClassificationReport> iterations = new ArrayList<>((upperBound - lowerBound) / stepSize);

        for (int i = lowerBound; i <= upperBound; i += stepSize) {
            CrossValidatorParameter<Integer> sampleSizeParam = specificValidatorParams.sampleSize(i);

            iterations.add(precisionValidator.validate(
                    classifier,
                    specificClassifierParams,
                    override(specificValidatorParams, Collections.singleton(sampleSizeParam))
            ));
        }

        return reportFactory.newMultiClassificationReport(iterations);
    }
}
