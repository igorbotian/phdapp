package ru.spbftu.igorbotian.phdapp.svm.validation;

import org.apache.log4j.Logger;
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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Абстрактная реализация средства кросс-валидации попарного классификатора
 */
abstract class AbstractPairwiseClassifierCrossValidator<R extends Report>
        implements AsyncPairwiseClassifierCrossValidator<R> {

    private static final Logger LOGGER = Logger.getLogger(AbstractPairwiseClassifierCrossValidator.class);

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

    /**
     * Получатели уведомлений о ходе кросс-валидации
     */
    private final Set<CrossValidationProgressListener> progressListeners = new CopyOnWriteArraySet<>();

    /**
     * Флаг прерванности процесса кросс-валидации
     */
    private final AtomicBoolean processInterrupted = new AtomicBoolean(false);

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
    public void addProgressListener(CrossValidationProgressListener listener) {
        progressListeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public void removeProgressListener(CrossValidationProgressListener listener) {
        progressListeners.remove(Objects.requireNonNull(listener));
    }

    @Override
    public void interrupt() throws CrossValidationException {
        processInterrupted.set(true);
    }

    /**
     * Получении информации о том, был ли прерван процесс валидации
     */
    protected boolean processInterrupted() {
        return processInterrupted.get();
    }

    /**
     * Уведомление о том, что процесс кросс-валидации начался
     */
    protected void fireCrossValidationStarted() {
        progressListeners.forEach(listener -> listener.crossValidationStarted(this));
    }

    /**
     * Уведомление о том, что кросс-валидация выполнена на заданное количество процентов
     */
    protected void fireCrossValidationContinued(int percents) {
        if (percents < 0 || percents > 100) {
            throw new IllegalArgumentException("Percents completed value is out of range: " + percents);
        }

        progressListeners.forEach(listener -> listener.crossValidationStarted(this));
    }

    /**
     * Уведомление о том, что процесс кросс-валидации был прерван
     */
    protected void fireCrossValidationInterrupted() {
        progressListeners.forEach(listener -> listener.crossValidationInterrupted(this));
    }

    /**
     * Уведомление о том, что произошла указанная ошибка в процессе кросс-валидации
     */
    protected void fireCrossValidationFailed(Throwable reason) {
        progressListeners.forEach(listener -> listener.crossValidationFailed(this, Objects.requireNonNull(reason)));
    }

    /**
     * Уведомление о том, что процесс кросс-валидации завершён с указанными результатами
     */
    protected void fireCrossValidationCompleted(R report) {
        progressListeners.forEach(listener -> listener.crossValidationCompleted(this, Objects.requireNonNull(report)));
    }

    @Override
    public void validateAsync(PairwiseClassifier classifier, Set<? extends CrossValidatorParameter<?>> validatorParams) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                processInterrupted.set(false);
                fireCrossValidationStarted();
                R report = validate(classifier, validatorParams);
                fireCrossValidationCompleted(report);
            } catch (Throwable e) {
                LOGGER.error("An error occurred during cross-validation", e);
                fireCrossValidationFailed(e);
            }
        });
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
     *
     * @param paramsFactory  экземпляр фабрики, используемый для получения исходных значений параметров
     * @param specificParams параметры, имеющие новые значения
     * @return экземпляр фабрики параметров средства кросс-валидации
     */
    protected CrossValidatorParameterFactory override(CrossValidatorParameterFactory paramsFactory,
                                                      Set<CrossValidatorParameter<?>> specificParams) {
        Objects.requireNonNull(paramsFactory);
        Objects.requireNonNull(specificParams);

        return new SpecificCrossValidatorParameterFactory(paramsFactory, specificParams);
    }

    /**
     * Создание набора параметров классификатора на базе существующего с учётом новых значений для заданных параметров
     *
     * @param params         набор параметров классификатора
     * @param specificParams параметры, имеющие новые значения
     * @return набор параметров классификатора
     */
    protected Set<? extends ClassifierParameter<?>> override(Set<? extends ClassifierParameter<?>> params,
                                                             Set<? extends ClassifierParameter<?>> specificParams) {
        Objects.requireNonNull(params);
        Objects.requireNonNull(specificParams);

        Set<ClassifierParameter<?>> result = new HashSet<>(params);
        specificParams.forEach(specificParam -> result.removeIf(param -> param.name().equals(specificParam.name())));
        specificParams.forEach(result::add);

        return result;
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
