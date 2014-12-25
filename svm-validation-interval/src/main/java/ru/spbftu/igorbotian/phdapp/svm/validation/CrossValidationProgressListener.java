package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

/**
 * Получатель уведомление о ходе процесса кросс-валидации
 */
public interface CrossValidationProgressListener {

    /**
     * Уведомление о том, что процесс кросс-валидации начался
     *
     * @param validator средство кросс-валидации
     */
    <R extends Report> void crossValidationStarted(PairwiseClassifierCrossValidator<R> validator);

    /**
     * Уведомление о том, что процесс кросс-валидации выполняется
     *
     * @param percentsCompleted процентов завершено (целое число от 0 до 100)
     */
    <R extends Report> void crossValidationContinued(PairwiseClassifierCrossValidator<R> validator, int percentsCompleted);

    /**
     * Уведомление о том, что процесс кросс-валидации прерван
     */
    <R extends Report> void crossValidationInterrupted(PairwiseClassifierCrossValidator<R> validator);

    /**
     * Уведомление о том, что процесс кросс-валидации закончен
     *
     * @param report результаты кросс-валидации
     */
    <R extends Report> void crossValidationCompleted(PairwiseClassifierCrossValidator<R> validator, Report report);

    /**
     * Уведомление о том, что процесс кросс-валидации закончился неудачно
     *
     * @param reason причина неудачи
     */
    <R extends Report> void crossValidationFailed(PairwiseClassifierCrossValidator<R> validator, Throwable reason);
}
