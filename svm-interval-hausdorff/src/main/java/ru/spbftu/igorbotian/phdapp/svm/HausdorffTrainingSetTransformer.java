package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Средство преобразования обучающей выборки, состоящей из интервальных оценок предпочтений, в обучающую выборку,
 * состояющую только из точных оценок предпочтений.
 * В основе преобразования используется расстояние Хаусдорфа
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
final class HausdorffTrainingSetTransformer {

    private HausdorffTrainingSetTransformer() {
        //
    }

    //-------------------------------------------------------------------------

    /**
     * Преобразование заданной обучающей выборки, состоящей из интервальных оценок предпочтений, в обучающую выборку,
     * состояющую только из точных оценок предпочтений.
     */
    public static PairwiseTrainingSet transformToPrecise(PairwiseTrainingSet trainingSet, DataFactory dataFactory) {
        Objects.requireNonNull(trainingSet);
        Objects.requireNonNull(dataFactory);

        if (containsOnlyPreciseJudgements(trainingSet)) {
            return trainingSet;
        }

        return dataFactory.newPairwiseTrainingSet(transformToPrecise(trainingSet.judgements(), dataFactory));
    }

    //-------------------------------------------------------------------------

    /**
     * Проверка на то, содержит ли заданная обучающая выборка только точные экспертные оценки предпочтений или нет
     */
    private static boolean containsOnlyPreciseJudgements(PairwiseTrainingSet trainingSet) {
        assert trainingSet != null;

        for (Judgement judgement : trainingSet.judgements()) {
            if (!isPrecise(judgement)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Проверка на то, является ли заданная экспертная оценка предпочтений точной или нет (т.е. интервальной)
     */
    private static boolean isPrecise(Judgement judgement) {
        assert judgement != null;
        return judgement.preferable().size() == 1 && judgement.inferior().size() == 1;
    }

    /**
     * Преобразование множества интервальных оценок предпочтений в множество точных оценок предпочтений
     */
    private static Set<? extends Judgement> transformToPrecise(Set<? extends Judgement> judgements,
                                                               DataFactory dataFactory) {
        assert judgements != null;
        assert dataFactory != null;

        Set<Pair<Set<DecomposedUnclassifiedObject>, Set<DecomposedUnclassifiedObject>>> decomposed
                = judgements.stream().map(HausdorffTrainingSetTransformer::decompose)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Set<Pair<DecomposedUnclassifiedObject, DecomposedUnclassifiedObject>> precise = transformToPrecise(decomposed);

        return toJudgements(precise, dataFactory);
    }

    //-------------------------------------------------------------------------

    /**
     * Разложение экспертной оценки предпочтений на пару объектов, полученных разложением более и менее предпочтительных
     * частей оценки на вещественнозначные параметры
     */
    private static Pair<Set<DecomposedUnclassifiedObject>, Set<DecomposedUnclassifiedObject>> decompose(Judgement judgement) {
        assert judgement != null;

        return new Pair<>(
                decompose(judgement.preferable()),
                decompose(judgement.inferior())
        );
    }

    /**
     * Разложение части экспертной оценки предпочтений (более или менее предпочтительная составляющая)
     * по вещественнозначным параметрам
     */
    private static Set<DecomposedUnclassifiedObject> decompose(Set<? extends UnclassifiedObject> intervalObj) {
        assert intervalObj != null;
        return intervalObj.stream().map(DecomposedUnclassifiedObject::fromUnclassifiedObject).collect(Collectors.toSet());
    }

    //-------------------------------------------------------------------------

    /**
     * Преобразование множества интервальных экспертных оценок предпочтений в множество точных экспертных оценок предпочтений,
     * полученное с помощью расстояния Хаусдорфа
     */
    private static Set<Pair<DecomposedUnclassifiedObject, DecomposedUnclassifiedObject>> transformToPrecise(
            Set<Pair<Set<DecomposedUnclassifiedObject>, Set<DecomposedUnclassifiedObject>>> intervalJudgements) {

        assert intervalJudgements != null;

        Set<Pair<DecomposedUnclassifiedObject, DecomposedUnclassifiedObject>> preciseJudgements = new LinkedHashSet<>();
        Set<Set<DecomposedUnclassifiedObject>> judgementParts = toSetOfJudgmentParts(intervalJudgements);

        for (Pair<Set<DecomposedUnclassifiedObject>, Set<DecomposedUnclassifiedObject>> judgement : intervalJudgements) {
            // для эквивалетного преобразования части оценки другая часть не используется
            // (см. формулу ядра Мерсера - каждая часть из двух оценок не зависит от другой)
            Set<Set<DecomposedUnclassifiedObject>> otherJudgementParts = new HashSet<>(judgementParts);
            otherJudgementParts.remove(judgement.first);
            otherJudgementParts.remove(judgement.second);
            preciseJudgements.add(transformToPrecise(judgement, otherJudgementParts));
        }

        return preciseJudgements;
    }

    /**
     * Представление множества интервальных оценок предпочтений в множество частей этих предпочтений
     * (т.е. более или менее предпочтительных элементов этих оценок)
     */
    private static Set<Set<DecomposedUnclassifiedObject>> toSetOfJudgmentParts(
            Set<Pair<Set<DecomposedUnclassifiedObject>, Set<DecomposedUnclassifiedObject>>> judgements) {
        assert judgements != null;
        Set<Set<DecomposedUnclassifiedObject>> judgementsParts = new HashSet<>();

        for (Pair<Set<DecomposedUnclassifiedObject>, Set<DecomposedUnclassifiedObject>> judgement : judgements) {
            judgementsParts.add(judgement.first);
            judgementsParts.add(judgement.second);
        }

        return judgementsParts;
    }

    /**
     * Преобразование заданной интервальной экспертной оценки предпочтений в эквивалентную точную оценку
     * с использованием расстояния Хаусдорфа
     */
    private static Pair<DecomposedUnclassifiedObject, DecomposedUnclassifiedObject> transformToPrecise(
            Pair<Set<DecomposedUnclassifiedObject>, Set<DecomposedUnclassifiedObject>> judgement,
            Set<Set<DecomposedUnclassifiedObject>> otherJudgementParts) {

        assert judgement != null;
        assert otherJudgementParts != null;

        return new Pair<>(
                transformToPrecise(judgement.first, otherJudgementParts),
                transformToPrecise(judgement.second, otherJudgementParts)
        );
    }

    /**
     * Преобразование заданной части интервальной экспертной оценки предпочтений в эквивалентную точную оценку
     * с использованием расстояния Хаусдорфа
     * При преобразовании необходима информация обо всех других интервальных экспертных оценках предпочтений.
     */
    private static DecomposedUnclassifiedObject transformToPrecise(Set<DecomposedUnclassifiedObject> judgementPart,
                                                          Set<Set<DecomposedUnclassifiedObject>> otherJudgementParts) {
        assert judgementPart != null;
        assert otherJudgementParts != null;

        Map<Double, DecomposedUnclassifiedObject> mostDistantItems = new HashMap<>();

        for (Set<DecomposedUnclassifiedObject> other : otherJudgementParts) {
            Map.Entry<Double, DecomposedUnclassifiedObject> mostDistantItem = findMostDistantItem(judgementPart, other);
            mostDistantItems.put(mostDistantItem.getKey(), mostDistantItem.getValue());
        }

        return computePreciseJudgementPart(composeId(judgementPart), mostDistantItems);
    }

    /**
     * Нахождение элемента из части экспертной оценки, расстояние от которого до заданной другой оценки
     * является максимальным среди минимальным.
     */
    private static Map.Entry<Double, DecomposedUnclassifiedObject> findMostDistantItem(
            Set<DecomposedUnclassifiedObject> from, Set<DecomposedUnclassifiedObject> to) {

        assert from != null;
        assert to != null;

        Map<Double, DecomposedUnclassifiedObject> minDistances = new HashMap<>();

        for (DecomposedUnclassifiedObject fromItem : from) {
            double min = Double.MAX_VALUE;

            for (DecomposedUnclassifiedObject toItem : to) {
                double distance = fromItem.distanceTo(toItem);

                if (distance < min) {
                    min = distance;
                }
            }

            minDistances.put(min, fromItem);
        }

        double maxDistance = Collections.max(minDistances.keySet());
        return new AbstractMap.SimpleEntry<>(maxDistance, minDistances.get(maxDistance));
    }

    /**
     * Вычисление точной экспертной оценки предпочтений, имеющей усреднённые значения параметров по заданным расстояниям
     * до частей всех других экспертных оценок предпочтений
     */
    private static DecomposedUnclassifiedObject computePreciseJudgementPart(
            String id, Map<Double, DecomposedUnclassifiedObject> mostDistantItems) {
        assert id != null;
        assert mostDistantItems != null;

        Map<String, Double> average = new HashMap<>();
        double sumOfDistances = mostDistantItems.keySet().stream().reduce(Double::sum).get();

        for(String param : mostDistantItems.values().iterator().next().params.keySet()) {
            double sumOfValues = mostDistantItems.entrySet().stream().mapToDouble(
                    e -> e.getKey() * e.getValue().params.get(param)
            ).sum();
            average.put(param, sumOfValues / sumOfDistances);
        }

        return new DecomposedUnclassifiedObject(id, average);
    }

    /**
     * Формирование идентификатора части экспертной оценки предпочтений на основе её составляющих
     */
    private static String composeId(Set<DecomposedUnclassifiedObject> items) {
        assert items != null;

        Set<String> ids = items.stream().map(item -> item.id).collect(Collectors.toSet());
        return String.join(",", ids);
    }

    //-------------------------------------------------------------------------

    /**
     * Преобразование множества пар разложенных по вещественнозначным параметров объектов в множество экспертных оценок
     * предпочтений
     */
    private static Set<? extends Judgement> toJudgements(
            Set<Pair<DecomposedUnclassifiedObject, DecomposedUnclassifiedObject>> decomposed, DataFactory dataFactory) {

        assert decomposed != null;
        assert dataFactory != null;

        return decomposed.stream().map(entry -> toJudgement(entry, dataFactory)).collect(Collectors.toSet());
    }

    /**
     * Преобразование пары разложенных по вещественнозначным параметрам объектов в экспертную оценку предпочтений
     */
    private static Judgement toJudgement(
            Pair<DecomposedUnclassifiedObject, DecomposedUnclassifiedObject> decomposed, DataFactory dataFactory) {

        assert decomposed != null;
        assert dataFactory != null;

        return dataFactory.newJudgement(
                Collections.singleton(decomposed.first.toUnclassifiedObject(dataFactory)),
                Collections.singleton(decomposed.second.toUnclassifiedObject(dataFactory))
        );
    }
}
