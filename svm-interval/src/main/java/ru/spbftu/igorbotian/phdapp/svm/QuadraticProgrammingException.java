package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Ошибка, произошедшая во время решения задачи квадратичного программирования, являющейся частью исходной задачи
 * классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class QuadraticProgrammingException extends ClassificationException {

    public QuadraticProgrammingException() {
        super();
    }

    public QuadraticProgrammingException(String message) {
        super(message);
    }

    public QuadraticProgrammingException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuadraticProgrammingException(Throwable cause) {
        super(cause);
    }

    protected QuadraticProgrammingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
