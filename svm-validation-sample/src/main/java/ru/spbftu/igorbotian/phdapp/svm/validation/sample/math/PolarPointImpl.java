package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import ru.spbftu.igorbotian.phdapp.common.BasicDataTypes;
import ru.spbftu.igorbotian.phdapp.common.DataClass;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.Parameter;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация интерфейса <code>PolarPoint</code>
 *
 * @see PolarPoint
 */
class PolarPointImpl implements PolarPoint {

    /**
     * Обозначение класса, когда точка не принадлежит никакому классу
     */
    private static final String UNCLASSIFIED_CLASS_NAME = "Unclassified";

    /**
     * Обозначение радиуса
     */
    private static final String R_LABEL = "R";

    /**
     * Обозначение угла наклона
     */
    private static final String PHI_LABEL = "Phi";

    /**
     * Полярный радиус
     */
    private final double r;

    /**
     * Полярный угол (в радианах)
     */
    private final double phi;

    /**
     * Класс, которому принадлежит данная точка
     */
    private final DataClass dataClass;

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Параметры данной точки как элемента выборки
     */
    private Set<Parameter<?>> params;

    public PolarPointImpl(double r, double phi, DataClass dataClass, DataFactory dataFactory) {
        if (r < 0) {
            throw new IllegalArgumentException("Polar radius cannot have a negative value");
        }

        this.r = r;
        this.phi = phi;
        this.dataClass = Objects.requireNonNull(dataClass);
        this.dataFactory = Objects.requireNonNull(dataFactory);
        this.params = Stream.of(
                dataFactory.newParameter(R_LABEL, r, BasicDataTypes.REAL),
                dataFactory.newParameter(PHI_LABEL, phi, BasicDataTypes.REAL)
        ).collect(Collectors.toSet());
    }

    public PolarPointImpl(double r, double phi, DataFactory dataFactory) {
        this(r, phi, Objects.requireNonNull(dataFactory).newClass(UNCLASSIFIED_CLASS_NAME), dataFactory);
    }

    @Override
    public double r() {
        return r;
    }

    @Override
    public double phi() {
        return phi;
    }

    @Override
    public String id() {
        return toString();
    }

    @Override
    public DataClass dataClass() {
        return dataClass;
    }

    @Override
    public PolarPoint rotate(double radians) {
        return new PolarPointImpl(r, phi + radians, dataClass, dataFactory);
    }

    @Override
    public Point toCartesian() {
        return new PointImpl(r * Math.cos(phi), r * Math.sin(phi), dataClass(), dataFactory);
    }

    @Override
    public Set<Parameter<?>> parameters() {
        return params;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, phi, dataClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof PolarPoint)) {
            return false;
        }

        PolarPointImpl other = (PolarPointImpl) obj;
        return (r == other.r
                && phi == other.phi
                && dataClass.equals(other.dataClass));
    }

    @Override
    public String toString() {
        return String.format("(%.5f;%.5f;%s)", r, phi, dataClass);
    }
}
