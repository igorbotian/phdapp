package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.BasicDataTypes;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.*;

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
     * Попытка получения значения заданного параметра в виде вещественного числа
     * @param param параметр объекта, подлежащего классификации
     * @return вещественное число
     * @throws IllegalArgumentException если значение параметра не может быть приведено к вещественному значению
     */
    public static Double toDoubleValue(Parameter<?> param) {
        Objects.requireNonNull(param);

        if (param.valueType().equals(BasicDataTypes.INTEGER)) {
            return Double.valueOf((Integer) param.value());
        } else if (param.valueType().equals(BasicDataTypes.REAL)) {
            return (Double) param.value();
        } else {
            throw new IllegalArgumentException("Unsupported parameter type: " + param.valueType());
        }
    }

    /**
     * Формирование ассоциативного массива, в котором ключами являются названия параметров объекта,
     * подлежащего классификации, а значения - индексы в массиве параметров
     * @param obj объект, подлежащий классификации
     * @return ассоциативный массив, сформированный заданным способом
     */
    public static Map<String, Integer> composeMapOfParamIndexes(UnclassifiedObject obj) {
        Objects.requireNonNull(obj);

        Map<String, Integer> indexes = new HashMap<>();
        int i = 0;

        for(Parameter<?> param : obj.parameters()) {
            indexes.put(param.name(), i);
            i++;
        }

        return indexes;
    }

    /**
     * Формирование вектора из вещественных чисел для заданного объекта, подлежащего классификации
     * и ассоциативного массива, отображающего его параметра на индексы вектора
     * @param obj объект, подлежащий классификации
     * @param paramIndexes ассоциативный массив, в котором ключами являются названия параметров объекта,
     * подлежащего классификации, а значения - индексы в массиве параметров
     * @return вещественный вектор
     */
    public static List<Double> toNumericalVector(UnclassifiedObject obj, Map<String, Integer> paramIndexes) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(paramIndexes);

        Double[] vector = new Double[obj.parameters().size()];

        for(Parameter<?> param : obj.parameters()) {
            vector[paramIndexes.get(param.name())] = UnclassifiedObjectUtils.toDoubleValue(param);
        }

        return Arrays.asList(vector);
    }

    /**
     * Разложение объекта, подлежащего классификации, на ассоциативный массив, в котором ключами являются
     * названия параметров объекта, а значения - значения этих параметров
     * @param obj объект, подлежащий классификации
     * @return результат разложения
     */
    public static Map<String, Double> decompose(UnclassifiedObject obj) {
        Objects.requireNonNull(obj);
        Map<String, Double> decomposed = new LinkedHashMap<>();
        obj.parameters().stream().forEach(p -> decomposed.put(p.name(), toDoubleValue(p)));
        return decomposed;
    }
}
