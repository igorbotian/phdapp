package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Ошибка, произошедшая во время решения исходной задачи оптимизации в рамках классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class OptimizationException extends ClassificationException {

    public OptimizationException() {
        super();
    }

    public OptimizationException(String message) {
        super(message);
    }

    public OptimizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimizationException(Throwable cause) {
        super(cause);
    }

    protected OptimizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
