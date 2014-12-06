package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Ошибка, возникшая в процессе обучения классификатора
 */
public class ClassifierTrainingException extends ClassificationException {

    public ClassifierTrainingException() {
    }

    public ClassifierTrainingException(String message) {
        super(message);
    }

    public ClassifierTrainingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassifierTrainingException(Throwable cause) {
        super(cause);
    }

    public ClassifierTrainingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
