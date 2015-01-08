package ru.spbftu.igorbotian.phdapp.ui.common;

import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationProgressListener;

/**
 * Модель окна, отображающего прогресс выполнения кросс-валидации классификатора с заданными параметрами
 */
public interface CrossValidationProgressWindowDirector {

    /**
     * Регистрация получателя уведомлений о ходе процесса кросс-валидации
     */
    void addProgressListener(CrossValidationProgressListener listener);

    /**
     * Удаление получателя уведомлений о ходе процесса кросс-валидации, зарегистрированного ранее
     */
    void removeProgressListener(CrossValidationProgressListener listener);

    /**
     * Начало процесса валидации с заданными параметрами.
     * Предполагается, что средство валидации было выбрано посредством <code>MainWindowDirector</code>,
     * а параметры валидации были заданы посредством <code>CrossValidationParamsWindowDirector</code>
     *
     * @throws IllegalStateException если средство валидации не было выбрано пользователем
     */
    void validate();

    /**
     * Остановка процесса валидации
     */
    void cancelValidation() throws CrossValidationException;
}
