package ru.spbftu.igorbotian.phdapp.ui.swing;

import ru.spbftu.igorbotian.phdapp.svm.validation.PairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;
import ru.spbftu.igorbotian.phdapp.ui.common.MainFrameDirector;

/**
 * Модель реализации главного окна приложения при помощи библиотеки Swing
 */
interface SwingMainFrameDirector extends MainFrameDirector {

    /**
     * Указание главного окна приложения, с которым будет взаимодействовать данная модель
     */
    void setMainFrame(MainFrame mainFrame);

    /**
     * Получение средства для проведения выбранного пользователем варианта кросс-валидации
     */
    PairwiseClassifierCrossValidator<? extends Report> selectedCrossValidator();
}
