package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.BasicDataTypes;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.*;

/**
 * Элемент обучающей выборки (или объект, подлежащий классификации), разложенный по вещественнозначным параметрам
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class DecomposedUnclassifiedObject {

    public final String id;
    public final Map<String, Double> params;

    public DecomposedUnclassifiedObject(String id, Map<String, Double> params) {
        this.id = Objects.requireNonNull(id);
        this.params = Collections.unmodifiableMap(Objects.requireNonNull(params));
    }

    public static DecomposedUnclassifiedObject fromUnclassifiedObject(UnclassifiedObject obj) {
        Objects.requireNonNull(obj);
        Map<String, Double> params = new HashMap<>();
        obj.parameters().forEach(p -> params.put(p.name(), UnclassifiedObjectUtils.toDoubleValue(p)));
        return new DecomposedUnclassifiedObject(obj.id(), params);
    }

    public UnclassifiedObject toUnclassifiedObject(DataFactory dataFactory) {
        Objects.requireNonNull(dataFactory);
        Set<Parameter<?>> parameters = new HashSet<>();

        for(Map.Entry<String, Double> param : params.entrySet()) {
            parameters.add(dataFactory.newParameter(param.getKey(), param.getValue(), BasicDataTypes.REAL));
        }

        return dataFactory.newUnclassifiedObject(id, parameters);
    }

    public double distanceTo(DecomposedUnclassifiedObject obj) {
        Objects.requireNonNull(obj);
        double sum = 0.0;

        for(String param : params.keySet()) {
            double diff = Math.abs(params.get(param) - obj.params.get(param));
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, params);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        } else if(obj == null || !(obj instanceof DecomposedUnclassifiedObject)) {
            return false;
        }

        DecomposedUnclassifiedObject other = (DecomposedUnclassifiedObject) obj;

        if(!id.equals(other.id) || params.size() != other.params.size()) {
            return false;
        }

        for(String key : params.keySet()) {
            if(!other.params.containsKey(key) || !params.get(key).equals(other.params.get(key))) {
                return false;
            }
        }

        return true;
    }
}
