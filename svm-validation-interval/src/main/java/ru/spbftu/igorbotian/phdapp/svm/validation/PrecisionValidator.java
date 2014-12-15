package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.common.ClassifiedData;
import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.ReportFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleException;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;

import java.util.Set;

/**
 * Средство кросс-валидации, направленное на точность работы попарного классификатора
 */
class PrecisionValidator extends AbstractPairwiseClassifierCrossValidator<SingleClassificationReport> {

    protected PrecisionValidator(CrossValidationSampleManager sampleManager,
                                 IntervalClassifierParameterFactory classifierParameterFactory,
                                 CrossValidatorParameterFactory crossValidatorParameterFactory,
                                 ReportFactory reportFactory) {

        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory);
    }

    @Override
    protected SingleClassificationReport validate(PairwiseClassifier classifier,
                                                  Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                  CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        int sampleSize = specificValidatorParams.sampleSize().value().value();
        int trainingTestingSetsSizeRatio = specificValidatorParams.trainingTestingSetsSizeRatio().value().value();
        ClassifiedData sample = sampleManager.generateSample(sampleSize);
        Pair<ClassifiedData, ClassifiedData> sampleSets = sampleManager.divideSampleIntoTwoGroups(sample,
                trainingTestingSetsSizeRatio);


        return null; // TODO
    }
}
