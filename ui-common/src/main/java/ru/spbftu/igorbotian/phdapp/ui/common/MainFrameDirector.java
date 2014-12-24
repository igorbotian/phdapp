package ru.spbftu.igorbotian.phdapp.ui.common;

/**
 * Модель главного окна приложения
 */
public interface MainFrameDirector {

    /**
     * Переход к соответствующему окну для выполнения заданного пользовательского действия
     * @param action пользовательское действие
     */
    public void performAction(UserAction action);
}
