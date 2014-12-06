package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Ошибка, связанная с отсутствием обязательного параметра классификации
 */
public class ClassifierParamNotFoundException extends RuntimeException {


    public ClassifierParamNotFoundException() {
        //
    }

    public ClassifierParamNotFoundException(String message) {
        super(message);
    }

    public ClassifierParamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassifierParamNotFoundException(Throwable cause) {
        super(cause);
    }

    public ClassifierParamNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
