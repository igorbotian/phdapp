package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.DataType;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Множество объектов, подлежащих классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class UnclassifiedObjectSet implements UnclassifiedObject {

    /**
     * Объекты, содержащиеся в данном множестве
     */
    private final Set<Parameter<?>> params;

    public UnclassifiedObjectSet(Set<? extends UnclassifiedObject> items) {
        Objects.requireNonNull(items);
        this.params = Collections.unmodifiableSet(asParams(items));
    }

    private Set<UnclassifiedObjectParameter> asParams(Set<? extends UnclassifiedObject> items) {
        Set<UnclassifiedObjectParameter> params = new HashSet<>();
        items.forEach(item -> params.add(new UnclassifiedObjectParameter(item)));
        return params;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !(obj instanceof UnclassifiedObjectSet)) {
            return false;
        }

        UnclassifiedObjectSet other = (UnclassifiedObjectSet) obj;
        return params.size() == other.params.size()
                && params.containsAll(other.params);
    }

    @Override
    public int hashCode() {
        return params.hashCode();
    }

    @Override
    public String toString() {
        return params.toString();
    }

    @Override
    public String id() {
        return Integer.toBinaryString(hashCode());
    }

    @Override
    public Set<Parameter<?>> parameters() {
        return params;
    }

    /**
     * Объект, подлежащий классификации, как параметр объекта, состоящего из множества объектов, подлежащих классификации
     */
    public static class UnclassifiedObjectParameter implements Parameter<UnclassifiedObject> {

        /**
         * Название параметра для множества объектов, подлежащих классификации
         */
        public static final String NAME = UnclassifiedObjectSet.class.getName();

        /**
         * Тип данных для объектов, подлежащих классификации
         */
        public static final DataType<UnclassifiedObject> DATA_TYPE = new DataType<UnclassifiedObject>() {

            @Override
            public String name() {
                return UnclassifiedObject.class.getName();
            }

            @Override
            public Class<UnclassifiedObject> javaClass() {
                return UnclassifiedObject.class;
            }
        };

        private final UnclassifiedObject object;

        public UnclassifiedObjectParameter(UnclassifiedObject object) {
            this.object = Objects.requireNonNull(object);
        }

        @Override
        public String name() {
            return NAME;
        }

        @Override
        public UnclassifiedObject value() {
            return object;
        }

        @Override
        public DataType<UnclassifiedObject> valueType() {
            return DATA_TYPE;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            } else if(obj == null || !(obj instanceof UnclassifiedObjectParameter)) {
                return false;
            }

            return object.equals(((UnclassifiedObjectParameter) obj).object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(object);
        }

        @Override
        public String toString() {
            return object.toString();
        }
    }
}
