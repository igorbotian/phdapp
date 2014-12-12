/*
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
import java.util.Objects;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet
 */
public class PairwiseInputDataPDU {

    public Set<DataClassPDU> classes;
    public Set<UnclassifiedObjectPDU> testingSet;
    public Set<PairwiseTrainingObjectPDU> trainingSet;

    public static PairwiseInputDataPDU toPDU(PairwiseInputData data) {
        PairwiseInputDataPDU pdu = new PairwiseInputDataPDU();

        pdu.classes = new LinkedHashSet<>();
        data.classes().forEach(clazz -> pdu.classes.add(DataClassPDU.toPDU(clazz)));

        pdu.testingSet = new LinkedHashSet<>();
        data.testingSet().forEach(obj -> pdu.testingSet.add(UnclassifiedObjectPDU.toPDU(obj)));

        pdu.trainingSet = new LinkedHashSet<>();
        data.trainingSet().forEach(obj -> pdu.trainingSet.add(PairwiseTrainingObjectPDU.toPDU(obj)));

        return pdu;
    }

    public PairwiseInputData toObject(DataFactory dataFactory, InputDataFactory inputDataFactory) throws DataException {
        Objects.requireNonNull(dataFactory);
        Objects.requireNonNull(inputDataFactory);

        Set<DataClass> classes = new LinkedHashSet<>();
        this.classes.forEach(clazz -> classes.add(clazz.toObject(dataFactory)));

        Set<UnclassifiedObject> testingSet = new LinkedHashSet<>();
        for (UnclassifiedObjectPDU pdu : this.testingSet) {
            testingSet.add(pdu.toObject(dataFactory));
        }

        Set<PairwiseTrainingObject> trainingSet = new LinkedHashSet<>();
        for (PairwiseTrainingObjectPDU pdu : this.trainingSet) {
            trainingSet.add(pdu.toObject(dataFactory));
        }

        return inputDataFactory.newPairwiseData(classes, trainingSet, testingSet);
    }
}
