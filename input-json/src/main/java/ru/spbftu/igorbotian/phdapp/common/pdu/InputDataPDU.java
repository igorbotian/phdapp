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
import ru.spbftu.igorbotian.phdapp.common.impl.InputDataFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedData
 */
public class InputDataPDU {

    public Set<DataClassPDU> classes;
    public Set<UnclassifiedObjectPDU> testingSet;
    public Set<ClassifiedObjectPDU> trainingSet;

    public static InputDataPDU toPDU(InputData data) {
        InputDataPDU pdu = new InputDataPDU();

        pdu.classes = new LinkedHashSet<>();
        data.classes().forEach(clazz -> pdu.classes.add(DataClassPDU.toPDU(clazz)));

        pdu.testingSet = new LinkedHashSet<>();
        data.testingSet().forEach(obj -> pdu.testingSet.add(UnclassifiedObjectPDU.toPDU(obj)));

        pdu.trainingSet = new LinkedHashSet<>();
        data.trainingSet().forEach(obj -> pdu.trainingSet.add(ClassifiedObjectPDU.toPDU(obj)));

        return pdu;
    }

    public InputData toObject() throws DataException {
        Set<DataClass> classes = new LinkedHashSet<>();
        this.classes.forEach(clazz -> classes.add(clazz.toObject()));

        Set<UnclassifiedObject> testingSet = new LinkedHashSet<>();
        for (UnclassifiedObjectPDU pdu : this.testingSet) {
            testingSet.add(pdu.toObject());
        }

        Set<ClassifiedObject> trainingSet = new LinkedHashSet<>();
        for (ClassifiedObjectPDU pdu : this.trainingSet) {
            trainingSet.add(pdu.toObject());
        }

        return InputDataFactory.newData(classes, trainingSet, testingSet);
    }
}
