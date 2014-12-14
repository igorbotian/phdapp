package ru.spbftu.igorbotian.phdapp.svm.validation;

/**
 * Ошибка, возникшая в процессе кросс-валидации классификатора
 */
public class CrossValidationException extends Exception {

    public CrossValidationException() {
        super();
    }

    public CrossValidationException(String message) {
        super(message);
    }

    public CrossValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrossValidationException(Throwable cause) {
        super(cause);
    }

    protected CrossValidationException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
