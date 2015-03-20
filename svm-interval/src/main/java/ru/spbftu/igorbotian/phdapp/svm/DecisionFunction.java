package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Решающая функция, определяющая предпочтение одной группы объектов над другими.
 * Основана на решении задачи квадратичного программирования.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class DecisionFunction {

    /**
     * Ассоциативный массив, в котором каждой паре объектов из обучащей выборки соответствует множитель Лагранжа
     */
    private final Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> lagrangianMultipliers;

    /**
     * Функция ядра
     */
    private final KernelFunction kernelFunction;

    public DecisionFunction(Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> lagrangianMultipliers,
                            KernelFunction kernelFunction) {
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
    public boolean isPreferable(UnclassifiedObject object, UnclassifiedObject objectToCompareWith)
            throws DecisionException {

        Objects.requireNonNull(object);
        Objects.requireNonNull(objectToCompareWith);

        if (object.equals(objectToCompareWith)) {
            throw new DecisionException("Decision function cannot be applied to equal objects: " + object.toString());
        }

        Pair<UnclassifiedObject, UnclassifiedObject> objectsToCompare = new Pair<>(object, objectToCompareWith);
        double first = computeFirst(objectsToCompare);
        double second = computeSecond(objectsToCompare);

        if (first - second == 0.0) {
            throw new DecisionException("Cannot decide which object in the pair is preferable: " + object.toString()
                    + "; " + objectToCompareWith.toString());
        }

        return (first > second);
    }

    private double computeFirst(Pair<UnclassifiedObject, UnclassifiedObject> objectsToCompare) {
        return compute(objectsToCompare, (x, z) -> MercerKernel.compute(x, z, kernelFunction));
    }

    private double computeSecond(Pair<UnclassifiedObject, UnclassifiedObject> objectsToCompare) {
        return compute(objectsToCompare, (x, z) -> MercerKernel.compute(z, x, kernelFunction));
    }

    private double compute(Pair<UnclassifiedObject, UnclassifiedObject> objectsToCompare,
                           BiFunction<
                                   Pair<UnclassifiedObject, UnclassifiedObject>,
                                   Pair<UnclassifiedObject, UnclassifiedObject>,
                                   Double> kernel) {
        assert objectsToCompare != null;
        assert kernel != null;

        double result = 0.0;

        for (Pair<UnclassifiedObject, UnclassifiedObject> pair : lagrangianMultipliers.keySet()) {
            result += lagrangianMultipliers.get(pair) * kernel.apply(pair, objectsToCompare);
        }

        return result;
    }
}
