package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.AbstractDataType;
import ru.spbftu.igorbotian.phdapp.common.DataType;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Контейнер для объектов, подлежащих классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class UnclassifiedObjectSet implements UnclassifiedObject {

    private static final UnclassifiedObjectSetType TYPE = new UnclassifiedObjectSetType();
    private Set<UnclassifiedObject> objects;

    public UnclassifiedObjectSet(Set<? extends UnclassifiedObject> objects) {
        this.objects = Collections.unmodifiableSet(new HashSet<>(objects));
    }

    @Override
    public String id() {
        return Integer.toString(objects.hashCode());
    }

    @Override
    public Set<Parameter<?>> parameters() {
        return Collections.singleton(new Parameter<Set<UnclassifiedObject>>() {

            @Override
            public String name() {
                return "UnclassifiedObjectSet";
            }

            @Override
            public Set<UnclassifiedObject> value() {
                return objects;
            }

            @Override
            public DataType<Set<UnclassifiedObject>> valueType() {
                return TYPE;
            }
        });
    }

    @Override
    public int hashCode() {
        return objects.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        } else if(obj == null || !(obj instanceof UnclassifiedObjectSet)) {
            return false;
        }

        UnclassifiedObjectSet other = (UnclassifiedObjectSet) obj;
        return objects.size() == other.objects.size()
                && objects.containsAll(other.objects);
    }

    /**
     * Тип данных для контейнера объектов, подлежащих классификации
     */
    private static class UnclassifiedObjectSetType extends AbstractDataType<Set<UnclassifiedObject>> {

        private static final Set<UnclassifiedObject> OBJECTS_TYPE = Collections.emptySet();

        @SuppressWarnings("unchecked")
        public UnclassifiedObjectSetType() {
            super("UnclassifiedObjectSetType", (Class<Set<UnclassifiedObject>>) OBJECTS_TYPE.getClass());
        }
    }
}
