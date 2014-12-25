package ru.spbftu.igorbotian.phdapp.ui.common;

import java.awt.*;

/**
 * Средство оповещения пользователя о произошедшей ошибке посредством показа окна с текстом данной ошибки
 */
public interface ErrorDialogs {

    /**
     * Показ окна с заданного сообщения об ошибке
     *
     * @param owner   родительское окно
     * @param message сообщение об ошибке
     * @throws NullPointerException если сообщение об ошибке не задано
     */
    void show(Window owner, String message);

    /**
     * Показ окна с заданного сообщения об ошибке
     *
     * @param owner   родительское окно
     * @param e ошибка
     * @throws NullPointerException если сообщение об ошибке не задано
     */
    void show(Window owner, Exception e);

    /**
     * Показ окна с заданного сообщения об ошибке
     *
     * @param message сообщение об ошибке
     * @throws NullPointerException если сообщение об ошибке не задано
     */
    void show(String message);

    /**
     * Показ окна с заданного сообщения об ошибке
     *
     * @param e ошибка
     * @throws NullPointerException если сообщение об ошибке не задано
     */
    void show(Exception e);
}
