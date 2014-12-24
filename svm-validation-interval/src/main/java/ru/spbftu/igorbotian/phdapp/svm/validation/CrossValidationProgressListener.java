package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

/**
 * Получатель уведомление о ходе процесса кросс-валидации
 */
public interface CrossValidationProgressListener {

    /**
     * Уведомление о том, что процесс кросс-валидации выполняется
     * @param percentsCompleted процентов завершено (целое число от 0 до 100)
     */
    void crossValidationContinued(int percentsCompleted);

    /**
     * Уведомление о том, что процесс кросс-валидации закончен
     * @param report результаты кросс-валидации
     */
    void crossValidationCompleted(Report report);

    /**
     * Уведомление о том, что процесс кросс-валидации закончился неудачно
     * @param reason причина неудачи
     */
    void crossValidationFailed(Exception reason);
}
