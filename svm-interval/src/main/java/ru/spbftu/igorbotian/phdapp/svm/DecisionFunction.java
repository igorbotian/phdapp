package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Решающая функция, определяющая предпочтение одной группы объектов над другими.
 * Основана на решении задачи квадратичного программирования.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class DecisionFunction<T> {

    /**
     * Ассоциативный массив, в котором каждой паре объектов из обучащей выборки соответствует множитель Лагранжа
     */
    private final Map<Pair<T, T>, Double> lagrangianMultipliers;

    /**
     * Функция ядра
     */
    private final KernelFunction<T> kernelFunction;

    public DecisionFunction(Map<Pair<T, T>, Double> lagrangianMultipliers, KernelFunction<T> kernelFunction) {
        Objects.requireNonNull(lagrangianMultipliers);
        Objects.requireNonNull(kernelFunction);

        if (lagrangianMultipliers.isEmpty()) {
            throw new IllegalArgumentException("A set of Lagrangian multipliers cannot be empty");
        }

        this.lagrangianMultipliers = Collections.unmodifiableMap(lagrangianMultipliers);
        this.kernelFunction = kernelFunction;
    }

    /**
     * Выяснение, является ли объект более предпочтительным, чем второй, или нет
     *
     * @param object              исходный объект
     * @param objectToCompareWith объект, с которым сравнивается исходный объект
     * @return <code>true</code>, если исходный объект предпочтительнее; <code>false</code>, если наоборот
     * @throws DecisionException если определить предпочтение невозможно
     */
    public boolean isPreferable(T object, T objectToCompareWith)
            throws DecisionException {

        Objects.requireNonNull(object);
        Objects.requireNonNull(objectToCompareWith);

        if (object.equals(objectToCompareWith)) {
            throw new DecisionException("Decision function cannot be applied to equal objects: " + object.toString());
        }

        double first = compute(new Pair<>(object, objectToCompareWith));
        double second = compute(new Pair<>(objectToCompareWith, object));

        if (first - second == 0.0) {
            throw new DecisionException("Cannot decide which object in the pair is preferable: " + object.toString()
                    + "; " + objectToCompareWith.toString());
        }

        return (first > second);
    }

    private double compute(Pair<T, T> objectsToCompare) {
        assert objectsToCompare != null;
        double sum = 0;

        for (Pair<T, T> pair : lagrangianMultipliers.keySet()) {
            sum += lagrangianMultipliers.get(pair) * MercerKernel.compute(pair, objectsToCompare, kernelFunction);
        }

        return sum;
    }
}
