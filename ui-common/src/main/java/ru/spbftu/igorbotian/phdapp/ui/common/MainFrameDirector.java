package ru.spbftu.igorbotian.phdapp.ui.common;

import ru.spbftu.igorbotian.phdapp.svm.validation.AsyncPairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.PairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

/**
 * Модель главного окна приложения
 */
public interface MainFrameDirector {

    /**
     * Переход к соответствующему окну для выполнения заданного пользовательского действия
     * @param action пользовательское действие
     */
    public void performAction(UserAction action);

    /**
     * Получение средства для проведения выбранного пользователем варианта кросс-валидации
     */
    AsyncPairwiseClassifierCrossValidator<? extends Report> selectedCrossValidator();
}
