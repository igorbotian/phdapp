package ru.spbftu.igorbotian.phdapp.common;

import java.util.Comparator;
import java.util.Objects;

/**
 * Реализация интерфейса <code>Range</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Range
 */
class RangeImpl<T> implements Range<T> {

    /**
     * Нижняя граница диапазона
     */
    private final T lowerBound;

    /**
     * Верхняя граница диапазона
     */
    private final T upperBound;

    public RangeImpl(T lowerBound, T upperBound, Comparator<T> comparator) {
        Objects.requireNonNull(lowerBound);
        Objects.requireNonNull(upperBound);
        Objects.requireNonNull(comparator);

        if (comparator.compare(lowerBound, upperBound) == 1) {
            throw new IllegalArgumentException("Range lower bound cannot be greater then the upper bound");
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public T lowerBound() {
        return lowerBound;
    }

    @Override
    public T upperBound() {
        return upperBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Range)) {
            return false;
        }

        RangeImpl other = (RangeImpl) obj;
        return lowerBound.equals(other.lowerBound)
                && upperBound.equals(other.upperBound);
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", lowerBound.toString(), upperBound.toString());
    }
}
