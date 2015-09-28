package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Средство вычисления расстояния между центрами двух кластеров данных с вещественнозначными параметрами
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class ClusterCentroid {

    private ClusterCentroid() {
        //
    }

    /**
     * Вычисление расстояния междву центрами двух кластеров, то есть множествами объектов, подлежащих классификации
     * и имеющих вещественнозначные параметры
     * @param first первый кластер
     * @param second второй кластер
     * @return расстояние между центрами двух кластеров
     */
    public static double computeDistanceBetween(Set<? extends UnclassifiedObject> first,
                                                Set<? extends UnclassifiedObject> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        if (first.isEmpty()) {
            throw new IllegalArgumentException("First cluster cannot be empty");
        }

        if (second.isEmpty()) {
            throw new IllegalArgumentException("Second cluster cannot be empty");
        }

        return computeDistance(
                first.stream().map(UnclassifiedObjectUtils::decompose).collect(Collectors.toSet()),
                second.stream().map(UnclassifiedObjectUtils::decompose).collect(Collectors.toSet())
        );
    }

    /**
     * Вычисление расстояние между двумя кластерами
     */
    private static double computeDistance(Set<Map<String, Double>> first, Set<Map<String, Double>> second) {
        assert first != null;
        assert second != null;

        return computeDistanceBetween(
                computeCentroid(first),
                computeCentroid(second)
        );
    }

    /**
     * Вычисление центра заданного кластера
     */
    private static Map<String, Double> computeCentroid(Set<Map<String, Double>> cluster) {
        assert cluster != null;
        assert !cluster.isEmpty();
        Map<String, Double> centroid = new HashMap<>();

        for(String param : cluster.iterator().next().keySet()) {
            centroid.put(param, cluster.stream().mapToDouble(item -> item.get(param)).average().getAsDouble());
        }

        return centroid;
    }

    /**
     * Вычисление расстояния между двумя точками в N-мерном пространстве
     */
    private static double computeDistanceBetween(Map<String, Double> from, Map<String, Double> to) {
        assert from != null;
        assert to != null;
        double sum = 0.0;

        for(String param : from.keySet()) {
            double diff = Math.abs(from.get(param) - to.get(param));
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }
}
