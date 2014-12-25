package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.ReportFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleException;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Средство анализа завимимости точности классификации от параметров классификации
 */
class PrecisionDependenceOnClassifierParametersAnalyzer
        extends AbstractPairwiseClassifierCrossValidator<MultiClassificationReport> {

    /**
     * Средство кросс-валидации, направленное на точность работы попарного классификатора
     */
    private final PrecisionValidator precisionValidator;

    /**
     * Фабрика параметров классификатора
     */
    private final IntervalClassifierParameterFactory classifierParameterFactory;

    public PrecisionDependenceOnClassifierParametersAnalyzer(CrossValidationSampleManager sampleManager,
                                                             IntervalClassifierParameterFactory classifierParameterFactory,
                                                             CrossValidatorParameterFactory crossValidatorParameterFactory,
                                                             ReportFactory reportFactory,
                                                             PrecisionValidator precisionValidator) {
        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory);
        this.precisionValidator = Objects.requireNonNull(precisionValidator);
        this.classifierParameterFactory = Objects.requireNonNull(classifierParameterFactory);
    }

    @Override
    protected MultiClassificationReport validate(PairwiseClassifier classifier,
                                                 Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                 CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        CrossValidatorParameter<Double> penaltyParam = specificValidatorParams.penaltyParameter();
        double ppLowerBound = penaltyParam.lowerBound().value();
        double ppUpperBound = penaltyParam.upperBound().value();
        double ppStepSize = penaltyParam.stepSize().value();

        CrossValidatorParameter<Double> gaussianKernelParam = specificValidatorParams.gaussianKernelParameter();
        double gkpLowerBound = gaussianKernelParam.lowerBound().value();
        double gkpUpperBound = gaussianKernelParam.upperBound().value();
        double gkpStepSize = gaussianKernelParam.stepSize().value();

        List<SingleClassificationReport> iterations = new LinkedList<>();

        for (double ccp = ppLowerBound; ccp <= ppUpperBound; ccp += ppStepSize) {
            for (double gkp = gkpLowerBound; gkp <= gkpUpperBound; gkp += gkpStepSize) {
                ClassifierParameter<Double> ccpParam = classifierParameterFactory.penaltyParameter(ccp);
                ClassifierParameter<Double> gkpParam = classifierParameterFactory.gaussianKernelParameter(gkp);

                iterations.add(precisionValidator.validate(
                                classifier,
                                override(specificClassifierParams,
                                        Stream.of(ccpParam, gkpParam).collect(Collectors.toSet())),
                                specificValidatorParams)
                );

            }
        }

        return reportFactory.newMultiClassificationReport(iterations);
    }
}