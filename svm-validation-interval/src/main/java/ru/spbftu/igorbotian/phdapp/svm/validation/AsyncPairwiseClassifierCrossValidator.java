package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.util.Set;

/**
 * Средство проведения кросс-валидации попарного классификатора с заданными параметрами в асинхронном режиме
 *
 * @see PairwiseClassifierCrossValidator
 */
public interface AsyncPairwiseClassifierCrossValidator<R extends Report> extends PairwiseClassifierCrossValidator<R> {

    /**
     * Регистрация получателя уведомлений о ходе кросс-валидации
     *
     * @param listener получатель уведомлений
     * @throws NullPointerException если получатель не задан
     */
    void addProgressListener(CrossValidationProgressListener listener);

    /**
     * Асинхронная кросс-валидация заданного попарного классификатора с заданными параметрами кросс-валидации.
     * Только в этом случае будут рассылаться уведомления о ходе кросс-валидации.
     *
     * @param classifier      попарный классификатор
     * @param validatorParams множество параметров кросс-валидации
     * @throws CrossValidationException в случае ошибки кросс-валидации
     * @throws NullPointerException     если хотя бы один из параметров не задан
     */
    void validateAsync(PairwiseClassifier classifier, Set<? extends CrossValidatorParameter<?>> validatorParams);

    /**
     * Прерывание процесса кросс-валидации, выполняющегося в асинхронном режиме
     */
    void interrupt() throws CrossValidationException;
}
