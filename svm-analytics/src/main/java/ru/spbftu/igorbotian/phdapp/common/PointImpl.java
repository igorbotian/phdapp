package ru.spbftu.igorbotian.phdapp.common;

import java.util.*;

/**
 * Реализация интерфейса <code>Point</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 */
class PointImpl implements Point {

    /**
     * Обозначение класса, когда точка не принадлежит никакому классу
     */
    private static final String UNCLASSIFIED_CLASS_NAME = "Unclassified";

    /**
     * Обозначение оси ординат
     */
    private static final String X_DIMENSION_LABEL = "X";

    /**
     * Обозначение оси ординат
     */
    private static final String Y_DIMENSION_LABEL = "Y";

    /**
     * Координата по оси абсцисс
     */
    private final double x;

    /**
     * Координата по оси ординат
     */
    private final double y;

    /**
     * Параметры точки как неклассифицированного объекта
     */
    private final Set<Parameter<?>> params;

    /**
     * Класс, которому принадлежит данная точка
     */
    private DataClass dataClass;

    /**
     * Фабрика создания объектов предметной области
     */
    private DataFactory dataFactory;

    public PointImpl(double x, double y, DataClass dataClass, DataFactory dataFactory) {
        this.x = x;
        this.y = y;
        this.dataClass = Objects.requireNonNull(dataClass);
        this.dataFactory = Objects.requireNonNull(dataFactory);

        params = Collections.unmodifiableSet(new HashSet<Parameter<?>>(Arrays.asList(
                dataFactory.newParameter(X_DIMENSION_LABEL, x, BasicDataTypes.REAL),
                dataFactory.newParameter(Y_DIMENSION_LABEL, y, BasicDataTypes.REAL)
        )));
    }

    public PointImpl(double x, double y, DataFactory dataFactory) {
        this(x, y, Objects.requireNonNull(dataFactory).newClass(UNCLASSIFIED_CLASS_NAME), dataFactory);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public Point shift(double dx, double dy) {
        return new PointImpl(x + dx, y + dy, dataClass, dataFactory);
    }

    @Override
    public PolarPoint toPolar() {
        double x = x();
        double y = y();
        double r = Math.sqrt(x * x + y * y);
        double phi;

        if (x > 0 && y >= 0) {
            phi = Math.atan(y / x);
        } else if (x > 0 && y < 0) {
            phi = Math.atan(y / x) + 2 * Math.PI;
        } else if (x < 0) {
            phi = Math.atan(y / x) + Math.PI;
        } else if (x == 0 && y > 0) {
            phi = Math.PI / 2;
        } else if (x == 0 && y < 0) {
            phi = 3 * Math.PI / 2;
        } else { // x == 0 && y == 0
            r = 0;
            phi = 0;
        }

        return new PolarPointImpl(r, phi, dataFactory);
    }

    @Override
    public double distanceTo(Point b) {
        Objects.requireNonNull(b);

        return Math.sqrt(Math.pow(Math.abs(b.x() - x()), 2.0) + Math.pow(Math.abs(b.y() - y()), 2.0));
    }

    @Override
    public DataClass dataClass() {
        return dataClass;
    }

    @Override
    public String id() {
        return String.format("(%.5f;%.5f;%s)", x, y, dataClass.toString());
    }

    @Override
    public Set<Parameter<?>> parameters() {
        return params;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, dataClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Point)) {
            return false;
        }

        PointImpl other = (PointImpl) obj;
        return (x == other.x && y == other.y && dataClass.equals(other.dataClass));
    }

    @Override
    public String toString() {
        return id();
    }
}
