/**
 * Copyright (c) 2014 Igor Botian
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.common;

import java.util.Comparator;
import java.util.Objects;

/**
 * Диапазон значений (включая границы)
 *
 * @param <T> тип значений
 */
public class Range<T> {

    /**
     * Нижняя граница диапазона
     */
    private final T lowerBound;

    /**
     * Верхняя граница диапазона
     */
    private final T upperBound;

    /**
     * Конструктор объекта
     *
     * @param lowerBound нижняя граница диапазона
     * @param upperBound верхняя граница диапазона
     * @param comparator необходим для проверки того, что нижняя граница по величине не больше верхней
     * @throws java.lang.NullPointerException     если хотя бы один из параметров не задан
     * @throws java.lang.IllegalArgumentException если значение нижней границы больше по величине значение верхней
     */
    public Range(T lowerBound, T upperBound, Comparator<T> comparator) {
        Objects.requireNonNull(lowerBound);
        Objects.requireNonNull(upperBound);
        Objects.requireNonNull(comparator);

        if (comparator.compare(lowerBound, upperBound) == 1) {
            throw new IllegalArgumentException("Range lower bound cannot be greater then the upper bound");
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Получение нижней границы диапазона
     *
     * @return нижняя граница диапазона
     */
    public T lowerBound() {
        return lowerBound;
    }

    /**
     * Получение верхней границы диапазона
     *
     * @return верхняя граница диапазона
     */
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

        Range other = (Range) obj;
        return lowerBound.equals(other.lowerBound)
                && upperBound.equals(other.upperBound);
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", lowerBound.toString(), upperBound.toString());
    }
}
