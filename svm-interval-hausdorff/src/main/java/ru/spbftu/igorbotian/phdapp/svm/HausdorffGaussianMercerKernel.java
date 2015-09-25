package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.*;

/**
 * Средство вычисления Гауссова ядра для объектов, подлежащих классификации, с применением расстояния Хаусдорфа
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class HausdorffGaussianMercerKernel extends GaussianMercerKernel<UnclassifiedObject> {

    private DataFactory dataFactory;

    public HausdorffGaussianMercerKernel(double sigma, DataFactory dataFactory) {
        super(new GaussianKernelFunctionImpl(sigma));
        this.dataFactory = Objects.requireNonNull(dataFactory);
    }

    @Override
    public double compute(Pair<UnclassifiedObject, UnclassifiedObject> first,
                          Pair<UnclassifiedObject, UnclassifiedObject> second) {

        if (!(first.first instanceof UnclassifiedObjectSet) || !(first.second instanceof UnclassifiedObjectSet)) {
            throw new IllegalArgumentException("Objects of the first pair should be a set of unclassified objects");
        }

        if (!(second.first instanceof UnclassifiedObjectSet) || !(second.second instanceof UnclassifiedObjectSet)) {
            throw new IllegalArgumentException("Objects of the second pair should be a set of unclassified objects");
        }

        Pair<Pair<UnclassifiedObject, UnclassifiedObject>, Pair<UnclassifiedObject, UnclassifiedObject>> pair = modify(
                new Pair<>((UnclassifiedObjectSet) first.first, (UnclassifiedObjectSet) first.second),
                new Pair<>((UnclassifiedObjectSet) second.first, (UnclassifiedObjectSet) second.second)
        );

        return super.compute(
                new Pair<>(pair.first.first, pair.first.second),
                new Pair<>(pair.second.first, pair.second.second)
        );
    }

    @SuppressWarnings("unchecked")
    private Pair<Pair<UnclassifiedObject, UnclassifiedObject>, Pair<UnclassifiedObject, UnclassifiedObject>>
    modify(Pair<UnclassifiedObjectSet, UnclassifiedObjectSet> first,
           Pair<UnclassifiedObjectSet, UnclassifiedObjectSet> second) {

        Set<Map<String, Double>> firstFirst = extractItems(first.first);
        Set<Map<String, Double>> firstSecond = extractItems(first.second);
        Set<Map<String, Double>> secondFirst = extractItems(second.first);
        Set<Map<String, Double>> secondSecond = extractItems(second.second);

        // см. GaussianMerkelKernel - first.first не зависит от first.second (то же самое для других)
        Map<String, Double> avgFirstFirst = convertToAverageItem(firstFirst, secondFirst, secondSecond);
        Map<String, Double> avgFirstSecond = convertToAverageItem(firstSecond, secondFirst, secondSecond);
        Map<String, Double> avgSecondFirst = convertToAverageItem(secondFirst, firstFirst, firstSecond);
        Map<String, Double> avgSecondSecond = convertToAverageItem(secondSecond, firstFirst, firstSecond);

        return new Pair<>(
                new Pair<>(
                        toUnclassifiedObject(first.first.id(), avgFirstFirst),
                        toUnclassifiedObject(first.second.id(), avgFirstSecond)
                ),
                new Pair<>(
                        toUnclassifiedObject(second.first.id(), avgSecondFirst),
                        toUnclassifiedObject(second.second.id(), avgSecondSecond)
                )
        );
    }

    //-------------------------------------------------------------------------

    /**
     * Рассматриваем множество объектов, подлежащее классификации, т.е. нечёткие данные, как множество объектов
     * с вещественнозначными параметрами
     */
    private Set<Map<String, Double>> extractItems(UnclassifiedObjectSet set) {
        Set<Map<String, Double>> items = new HashSet<>();

        for (Parameter<?> param : set.parameters()) {
            items.add(extractDoubleParams((UnclassifiedObject) param.value()));
        }

        return items;
    }

    private Map<String, Double> extractDoubleParams(UnclassifiedObject obj) {
        Map<String, Double> params = new HashMap<>();

        for (Parameter<?> param : obj.parameters()) {
            params.put(param.name(), UnclassifiedObjectUtils.toDoubleValue(param));
        }

        return params;
    }

    //-------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private Map<String, Double> convertToAverageItem(Set<Map<String, Double>> item, Set<Map<String, Double>>... others) {
        Map<Double, Map<String, Double>> mostDistantItems = new HashMap<>();

        for(Set<Map<String, Double>> other : others) {
            Map.Entry<Double, Map<String, Double>> mostDistantItem = findMostDistantItem(item, other);
            mostDistantItems.put(mostDistantItem.getKey(), mostDistantItem.getValue());
        }

        return computeAverageItem(mostDistantItems);
    }

    //-------------------------------------------------------------------------

    /**
     * Поиск объекта из {@code from}, расстояние от которого до самого дальнего объекта из {@code to} наибольшее
     * (см. расстояние Хаусдорфа)
     */
    private Map.Entry<Double, Map<String, Double>> findMostDistantItem(Set<Map<String, Double>> from,
                                                                       Set<Map<String, Double>> to) {

        Map<Double, Map<String, Double>> minDistances = new HashMap<>();

        for(Map<String, Double> pointFrom : from) {
            double min = Double.MAX_VALUE;

            for(Map<String, Double> pointTo : to) {
                double distance = distanceBetween(pointFrom, pointTo);

                if(distance < min) {
                    min = distance;
                }
            }

            minDistances.put(min, pointFrom);
        }

        double maxDistance = Collections.max(minDistances.keySet());
        return new AbstractMap.SimpleEntry<>(maxDistance, minDistances.get(maxDistance));
    }

    private double distanceBetween(Map<String, Double> from, Map<String, Double> to) {
        double sum = 0.0;

        for(String param : from.keySet()) {
            double diff = Math.abs(from.get(param) - to.get(param));
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }

    //-------------------------------------------------------------------------

    private Map<String, Double> computeAverageItem(Map<Double, Map<String, Double>> mostDistantItems) {
        assert !mostDistantItems.isEmpty();

        Map<String, Double> average = new HashMap<>();
        double sumOfDistances = mostDistantItems.keySet().stream().reduce(Double::sum).get();

        for(String param : mostDistantItems.values().iterator().next().keySet()) {
            double sumOfValues = mostDistantItems.entrySet().stream().mapToDouble(
                    e -> e.getKey() * e.getValue().get(param)
            ).sum();
            average.put(param, sumOfValues / sumOfDistances);
        }

        return average;
    }

    //-------------------------------------------------------------------------

    private UnclassifiedObject toUnclassifiedObject(String id, Map<String, Double> params) {
        return dataFactory.newUnclassifiedObject(id, toParams(params));
    }

    private Set<Parameter<?>> toParams(Map<String, Double> values) {
        Set<Parameter<?>> params = new HashSet<>();

        for(Map.Entry<String, Double> value : values.entrySet()) {
            params.add(dataFactory.newParameter(value.getKey(), value.getValue(), BasicDataTypes.REAL));
        }

        return params;
    }
}
