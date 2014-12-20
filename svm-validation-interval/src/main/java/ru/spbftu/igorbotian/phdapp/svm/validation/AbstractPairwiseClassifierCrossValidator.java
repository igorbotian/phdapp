package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.ReportFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleException;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактная реализация средства кросс-валидации попарного классификатора
 */
abstract class AbstractPairwiseClassifierCrossValidator<R extends Report>
        implements PairwiseClassifierCrossValidator<R> {

    /**
     * Средство формирования выборки для кросс-валидации
     */
    protected final CrossValidationSampleManager sampleManager;

    /**
     * Фабрика параметров попарного классификатора
     */
    private final IntervalClassifierParameterFactory classifierParameterFactory;

    /**
     * Фабрика параметров средства кросс-валидации попарного классификтора
     */
    private final CrossValidatorParameterFactory crossValidatorParameterFactory;

    /**
     * Средство формирования отчётов по результатам кросс-валидации попарного классификатора
     */
    protected final ReportFactory reportFactory;

    protected AbstractPairwiseClassifierCrossValidator(CrossValidationSampleManager sampleManager,
                                                       IntervalClassifierParameterFactory classifierParameterFactory,
                                                       CrossValidatorParameterFactory crossValidatorParameterFactory,
                                                       ReportFactory reportFactory) {

        this.sampleManager = Objects.requireNonNull(sampleManager);
        this.classifierParameterFactory = Objects.requireNonNull(classifierParameterFactory);
        this.crossValidatorParameterFactory = Objects.requireNonNull(crossValidatorParameterFactory);
        this.reportFactory = Objects.requireNonNull(reportFactory);
    }

    @Override
    public R validate(PairwiseClassifier classifier, Set<? extends CrossValidatorParameter<?>> specificValidatorParams)
            throws CrossValidationException {
        Objects.requireNonNull(classifier);
        Objects.requireNonNull(specificValidatorParams);

        Set<? extends ClassifierParameter<?>> classifierParams = extractClassifierParams(specificValidatorParams);
        CrossValidatorParameterFactory parameterFactory
                = new SpecificCrossValidatorParameterFactory(crossValidatorParameterFactory, specificValidatorParams);

        try {
            return validate(classifier, classifierParams, parameterFactory);
        } catch (CrossValidationSampleException e) {
            throw new CrossValidationException("Failed to perform cross-validation on a given classifier", e);
        }
    }

    private Set<? extends ClassifierParameter<?>> extractClassifierParams(
            Set<? extends CrossValidatorParameter<?>> validatorParams) {

        Set<ClassifierParameter<?>> classifierParams = new HashSet<>();

        for (ClassifierParameter<?> classifierParam : classifierParameterFactory.defaultValues()) {
            for (CrossValidatorParameter<?> validatorParam : validatorParams) {
                if (classifierParam.name().equals(validatorParam.name())) {
                    classifierParams.add(classifierParam);
                    break;
                }
            }
        }

        return classifierParams;
    }

    /**
     * Создание экземпляра фабрики параметров средства кросс-валидации на базе существующего с учётом новых значений
     * для заданных параметров
     * @param paramsFactory экземпляр фабрики, используемый для получения исходных значений параметров
     * @param specificParams параметры, имеющие новые значения
     * @return экземпляр фабрики параметров средства кросс-валидации
     */
    protected CrossValidatorParameterFactory override(CrossValidatorParameterFactory paramsFactory,
                                                                      Set<CrossValidatorParameter<?>> specificParams) {
        return new SpecificCrossValidatorParameterFactory(paramsFactory, specificParams);
    }

    /**
     * Кросс-валидация заданного попарного классификатора с заданными параметрами кросс-валидации
     *
     * @param classifier               попарный классификатор, подлежащий кросс-валидации
     * @param specificClassifierParams параметры классификатора, имеющие значение, отличные от значений по умолчанию
     * @param specificValidatorParams  параметры кросс-валидации (с учётом значений, отличных от значений по умолчанию)
     * @return отчёт, содержащий результаты кросс-валидации указанного классификатора
     * @throws NullPointerException           если хотя бы один из параметров не задан
     * @throws CrossValidationSampleException в случае ошибки формирования выборки для кросс-валидации
     * @throws CrossValidationException       в случае ошибки в процессе кросс-валидации
     */
    protected abstract R validate(PairwiseClassifier classifier,
                                  Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                  CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException;
}
