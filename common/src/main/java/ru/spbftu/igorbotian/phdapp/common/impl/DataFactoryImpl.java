/**
 * Copyright (c) 2014 Igor Botian
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.common.impl;

import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Фабрика объектов предметной области
 */
class DataFactoryImpl implements DataFactory {

    public DataClass newClass(String name) {
        return new DataClassImpl(name);
    }

    public Set<DataClass> newClasses(String... names) {
        return Stream.of(names).map(this::newClass).collect(Collectors.toSet());
    }

    public Set<DataClass> newClasses(Set<String> names) {
        return names.stream().map(this::newClass).collect(Collectors.toSet());
    }

    public <V> Parameter<V> newParameter(String name, V value, DataType<V> valueType) {
        return new ParameterImpl<>(name, value, valueType);
    }

    public UnclassifiedObject newUnclassifiedObject(String id, Set<Parameter<?>> params) {
        return new UnclassifiedObjectImpl(id, params);
    }

    public ClassifiedObject newClassifiedObject(String id, Set<Parameter<?>> params, DataClass dataClass) {
        return new ClassifiedObjectImpl(id, params, dataClass);
    }

    public PointwiseTrainingObject newPointwiseTrainingObject(String id, Set<Parameter<?>> params, DataClass realClass) {
        return new PointwiseTrainingObjectImpl(id, params, realClass);
    }

    public PairwiseTrainingObject newPairwiseTrainingObject(Set<? extends UnclassifiedObject> preferable,
                                                                   Set<? extends UnclassifiedObject> inferior) {
        return new PairwiseTrainingObjectImpl(preferable, inferior);
    }

    public UnclassifiedData newUnclassifiedData(Set<? extends DataClass> classes,
                                                       Set<? extends UnclassifiedObject> objects) throws DataException {
        return new UnclassifiedDataImpl(classes, objects);
    }

    public ClassifiedData newClassifiedData(Set<? extends DataClass> classes,
                                                   Set<? extends ClassifiedObject> objects) throws DataException {
        return new ClassifiedDataImpl(classes, objects);
    }

    public PointwiseTrainingSet newPointwiseTrainingSet(Set<? extends DataClass> classes,
                                                               Set<? extends PointwiseTrainingObject> objects) throws DataException {
        return new PointwiseTrainingSetImpl(classes, objects);
    }

    public PairwiseTrainingSet newPairwiseTrainingSet(Set<? extends PairwiseTrainingObject> objects) {
        return new PairwiseTrainingSetImpl(objects);
    }
}
