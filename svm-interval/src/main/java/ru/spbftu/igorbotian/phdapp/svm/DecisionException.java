package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Ошибка, возникающая в результате невозможности определения, какой объект из пары предпочтительнее другого
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class DecisionException extends ClassificationException {

    public DecisionException() {
        super();
    }

    public DecisionException(String message) {
        super(message);
    }

    public DecisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecisionException(Throwable cause) {
        super(cause);
    }

    protected DecisionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
