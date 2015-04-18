package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.BasicDataTypes;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;

/**
 * Утилитарный класс для произведения манипуляций над объектами, подлежащими классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public final class UnclassifiedObjectUtils {

    private UnclassifiedObjectUtils() {
        //
    }

    /**
     * Представление заданного объекта, подлежащего классификации, в виде вещественного вектора
     *
     * @param obj объект, подлежащий классификации
     * @return массив из вещественных чисел
     * @throws NullPointerException     если параметр не задан
     * @throws IllegalArgumentException если хотя бы один параметр объекта, подлежащего классификации,
     *                                  имеет нечисловой тип
     */
    public static double[] toNumericalVector(UnclassifiedObject obj) {
        Objects.requireNonNull(obj);
        double[] result = new double[obj.parameters().size()];
        int i = 0;

        for (Parameter param : obj.parameters()) {
            if (param.valueType().equals(BasicDataTypes.INTEGER)) {
                result[i] = (Integer) param.value();
            } else if (param.valueType().equals(BasicDataTypes.REAL)) {
                result[i] = (Double) param.value();
            } else {
                throw new IllegalArgumentException("Unsupported parameter type: " + param.valueType());
            }

            i++;
        }

        return result;
    }
}
