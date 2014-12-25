package ru.spbftu.igorbotian.phdapp.ui.swing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.ui.common.ErrorDialogs;
import ru.spbftu.igorbotian.phdapp.ui.common.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Реализация окна вывода сообщения об ошибке
 */
@Singleton
class ErrorDialogsImpl implements ErrorDialogs {

    private static final String ERROR_LABEL = "error";

    /**
     * Общие элементы пользовательского интерфейса
     */
    private final UIHelper uiHelper;

    @Inject
    public ErrorDialogsImpl(UIHelper uiHelper) {
        this.uiHelper = Objects.requireNonNull(uiHelper);
    }

    @Override
    public void show(Window owner, String message) {
        JOptionPane.showMessageDialog(owner, Objects.requireNonNull(message),
                uiHelper.getLabel(ERROR_LABEL), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void show(Window owner, Exception e) {
        show(owner, Objects.requireNonNull(e.getMessage()));
    }

    @Override
    public void show(String message) {
        show(null, message);
    }

    @Override
    public void show(Exception e) {
        show(Objects.requireNonNull(e.getMessage()));
    }
}
