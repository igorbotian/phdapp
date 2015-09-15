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
                Collections.max(minDistancesBetween(first, second)),
                Collections.max(minDistancesBetween(second, first))
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
            Set<Double> distances = new HashSet<>();

            for(List<Double> secondPoint : second) {
                distances.add(distanceBetween(firstPoint, secondPoint));
            }

            minDistances.add(Collections.min(distances));
        }

        return minDistances;
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
            sum += Math.pow(first.get(i) - second.get(i), 2.0);
        }

        return Math.sqrt(sum);
    }
}
