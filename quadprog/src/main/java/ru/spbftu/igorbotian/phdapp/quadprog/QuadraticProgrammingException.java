package ru.spbftu.igorbotian.phdapp.quadprog;

/**
 * Ошибка, возникшая в ходе решения задачи квадратичного программирования
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class QuadraticProgrammingException extends Exception {

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
