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
    private static final String REASON_LABEL = "reason";

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
    public void show(Window owner, Throwable e) {
        Objects.requireNonNull(e);
        String message = e.getMessage();

        if(e.getCause() != null) {
            message += String.format("\r\n%s: %s", uiHelper.getLabel(REASON_LABEL), e.getCause().getMessage());
        }

        show(owner, message);
    }

    @Override
    public void show(String message) {
        show(null, message);
    }

    @Override
    public void show(Throwable e) {
        show(null, e);
    }
}
