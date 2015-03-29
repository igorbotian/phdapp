package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.util.Set;

/**
 * Средство проведения кросс-валидации ранжирующего попарного классификатора с заданными параметрами в асинхронном режиме
 *
 * @see RankingPairwiseClassifierCrossValidator
 */
public interface AsyncRankingPairwiseClassifierCrossValidator<R extends Report> extends RankingPairwiseClassifierCrossValidator<R> {

    /**
     * Регистрация получателя уведомлений о ходе кросс-валидации
     *
     * @param listener получатель уведомлений
     * @throws NullPointerException если получатель не задан
     */
    void addProgressListener(CrossValidationProgressListener listener);

    /**
     * Удаление получателя уведомлений о ходе кросс-валидации, зарегистрированного ранее
     *
     * @param listener получатель уведомлений
     * @throws NullPointerException если получатель не задан
     */
    void removeProgressListener(CrossValidationProgressListener listener);

    /**
     * Асинхронная кросс-валидация заданного ранжирующего попарного классификатора с заданными параметрами кросс-валидации.
     * Только в этом случае будут рассылаться уведомления о ходе кросс-валидации.
     *
     * @param classifier      попарный классификатор
     * @param validatorParams множество параметров кросс-валидации
     * @throws NullPointerException если хотя бы один из параметров не задан
     */
    void validateAsync(RankingPairwiseClassifier classifier, Set<? extends CrossValidatorParameter<?>> validatorParams);

    /**
     * Прерывание процесса кросс-валидации, выполняющегося в асинхронном режиме
     */
    void interrupt() throws CrossValidationException;
}
