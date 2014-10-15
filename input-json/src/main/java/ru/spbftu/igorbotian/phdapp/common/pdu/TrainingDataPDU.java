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

package ru.spbftu.igorbotian.phdapp.common.pdu;

import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see TrainingDataPDU
 */
public class TrainingDataPDU {

    public Set<DataClassPDU> classes;
    public Set<DataObjectPDU> testingSet;
    public Set<TrainingDataObjectPDU> trainingSet;

    public static TrainingDataPDU toPDU(TrainingData data) {
        TrainingDataPDU pdu = new TrainingDataPDU();

        pdu.classes = new LinkedHashSet<>();
        data.classes().forEach(clazz -> pdu.classes.add(DataClassPDU.toPDU(clazz)));

        pdu.testingSet = new LinkedHashSet<>();
        data.testingSet().forEach(obj -> pdu.testingSet.add(DataObjectPDU.toPDU(obj)));

        pdu.trainingSet = new LinkedHashSet<>();
        data.trainingSet().forEach(obj -> pdu.trainingSet.add(TrainingDataObjectPDU.toPDU(obj)));

        return pdu;
    }

    public TrainingData toObject() {
        Set<DataClass> classes = new LinkedHashSet<>();
        this.classes.forEach(clazz -> classes.add(clazz.toObject()));

        Set<DataObject> testingSet = new LinkedHashSet<>();
        this.testingSet.forEach(obj -> testingSet.add(obj.toObject()));

        Set<TrainingDataObject> trainingSet = new LinkedHashSet<>();
        this.trainingSet.forEach(obj -> trainingSet.add(obj.toObject()));

        return DataFactory.newTrainingData(classes, testingSet, trainingSet);
    }
}
