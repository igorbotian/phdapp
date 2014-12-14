package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.DataClass;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;

import java.util.Comparator;
import java.util.Objects;

/**
 * Реализация фабрики математических примитивов, используемых в кросс-валидации классификатора
 */
@Singleton
class MathDataFactoryImpl implements MathDataFactory {

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    @Inject
    public MathDataFactoryImpl(DataFactory dataFactory) {
        this.dataFactory = Objects.requireNonNull(dataFactory);
    }

    @Override
    public Line newLine(Point a, Point b) {
        return new LineImpl(a, b);
    }

    @Override
    public Line newLine(double a, double b, double c) {
        return new LineImpl(a, b, c);
    }

    @Override
    public Point newPoint(double x, double y, DataClass dataClass) {
        return new PointImpl(x, y, dataClass, dataFactory);
    }

    @Override
    public Point newPoint(double x, double y) {
        return new PointImpl(x, y, dataFactory);
    }

    @Override
    public PolarPoint newPolarPoint(double r, double phi, DataClass dataClass) {
        return new PolarPointImpl(r, phi, dataClass, dataFactory);
    }

    @Override
    public PolarPoint newPolarPoint(double r, double phi) {
        return new PolarPointImpl(r, phi, dataFactory);
    }

    @Override
    public <T> Range<T> newRange(T lowerBound, T upperBound, Comparator<T> comparator) {
        return new RangeImpl<>(lowerBound, upperBound, comparator);
    }

    @Override
    public Range<Integer> newRange(int lowerBound, int upperBound) {
        return new RangeImpl<>(lowerBound, upperBound, Integer::compare);
    }

    @Override
    public Range<Double> newRange(double lowerBound, double upperBound) {
        return new RangeImpl<>(lowerBound, upperBound, Double::compare);
    }
}
