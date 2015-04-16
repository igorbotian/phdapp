package ru.spbftu.igorbotian.phdapp.common;

import java.util.*;

/**
 * Класс, служащий для вычисления расстояния Хаусдорфа между двумя заданными множествами точек
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="https://en.wikipedia.org/wiki/Hausdorff_distance">https://en.wikipedia.org/wiki/Hausdorff_distance</a>
 */
public final class HausdorffDistance {

    private HausdorffDistance() {
        //
    }

    /**
     * Вычисление расстояния Хаусдорфа между двумя заданными множествами точек в N-мерном пространстве
     *
     * @param first  первое множество точек
     * @param second второй множество точек
     * @return неотрицательное вещественное число
     * @throws NullPointerException     если хотя бы одно из множеств не задано
     * @throws IllegalArgumentException если хотя бы одно из множеств пустое
     * @throws IllegalArgumentException если хотя бы одна точка из множеств имеет количество параметров,
     *                                  отличное от других
     */
    public static double compute(Set<List<Double>> first, Set<List<Double>> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        checkSet(first);
        checkSet(second);

        if (first.iterator().next().size() != second.iterator().next().size()) {
            throw new IllegalArgumentException("All points in both sets should have the same number of parameters");
        }

        return Math.max(
                max(minDistancesBetween(first, second)),
                max(minDistancesBetween(second, first))
        );
    }

    private static void checkSet(Set<List<Double>> set) {
        assert set != null;

        if (set.isEmpty()) {
            throw new IllegalArgumentException("Set of points cannot be empty");
        }

        int expected = set.iterator().next().size();

        for (List<Double> point : set) {
            if (point.size() != expected) {
                throw new IllegalArgumentException("All points in a set should have the same number of parameters");
            }
        }
    }

    /**
     * Формирование набора, каждый элемент которого является минимальным расстоянием от каждой точки первого множества
     * до точек другого
     */
    private static Set<Double> minDistancesBetween(Set<List<Double>> first, Set<List<Double>> second) {
        assert first != null;
        assert !first.isEmpty();
        assert second != null;
        assert !second.isEmpty();

        Set<Double> minDistances = new HashSet<>();

        for (List<Double> firstPoint : first) {
            double min = Double.MAX_VALUE;

            for (List<Double> secondPoint : second) {
                min = Math.min(min, distanceBetween(firstPoint, secondPoint));
            }

            minDistances.add(min);
        }

        return minDistances;
    }

    /**
     * Выявление наибольшего вещественного числа из набора
     */
    private static double max(Collection<Double> numbers) {
        assert numbers != null;
        assert !numbers.isEmpty();

        double max = Double.MIN_VALUE;

        for (double number : numbers) {
            if (number > max) {
                max = number;
            }
        }

        return max;
    }

    /**
     * Вычисления между двумя точками в N-мерном пространстве
     */
    private static double distanceBetween(List<Double> first, List<Double> second) {
        assert first != null;
        assert !first.isEmpty();
        assert second != null;
        assert !second.isEmpty();
        assert first.size() == second.size();

        double sum = 0.0;

        for (int i = 0; i < first.size(); i++) {
            double diff = first.get(i) - second.get(i);
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }
}
