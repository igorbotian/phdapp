package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

/**
 * Ошибка формирования выборки для кросс-валидации классификатора
 */
public class CrossValidationSampleException extends Exception {

    public CrossValidationSampleException() {
        super();
    }

    public CrossValidationSampleException(String message) {
        super(message);
    }

    public CrossValidationSampleException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrossValidationSampleException(Throwable cause) {
        super(cause);
    }

    protected CrossValidationSampleException(String message, Throwable cause,
                                             boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
