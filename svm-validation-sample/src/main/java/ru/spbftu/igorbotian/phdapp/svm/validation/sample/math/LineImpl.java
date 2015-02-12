package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import java.util.Objects;

/**
 * Реализация интерфейса <code>Line</code>
 *
 * @see Line
 */
class LineImpl implements Line {

    /**
     * Коэффициент A
     */
    private final double a;

    /**
     * Коэффициент B
     */
    private final double b;

    /**
     * Коэффициент С
     */
    private final double c;

    public LineImpl(Point a, Point b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        this.a = a.y() - b.y();
        this.b = b.x() - a.x();
        this.c = a.x() * b.y() - b.x() * a.y();
    }

    public LineImpl(double a, double b, double c) {
        if (a == 0 && b == 0) {
            throw new IllegalArgumentException("A and coefficients cannot be equal to zero at the same time");
        }

        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double a() {
        return a;
    }

    @Override
    public double b() {
        return b;
    }

    @Override
    public double c() {
        return c;
    }

    @Override
    public double y(double x) {
        return (-c - a * x) / b;
    }

    @Override
    public double x(double y) {
        return (-c - b * y) / a;
    }

    @Override
    public double angle() {
        return Math.atan(/* k = */ -a / b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Line)) {
            return false;
        }

        LineImpl other = (LineImpl) obj;
        return a == other.a
                && b == other.b
                && c == other.c;
    }

    @Override
    public String toString() {
        return String.format("%.5f * x + %.5f * y + %.5f", a, b, c);
    }
}
