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
import java.util.Objects;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedData
 */
public final class UnclassifiedDataPDU {

    public Set<DataClassPDU> classes;
    public Set<UnclassifiedObjectPDU> objects;

    public static UnclassifiedDataPDU toPDU(UnclassifiedData data) {
        UnclassifiedDataPDU pdu = new UnclassifiedDataPDU();

        pdu.classes = new LinkedHashSet<>();
        data.classes().forEach(clazz -> pdu.classes.add(DataClassPDU.toPDU(clazz)));

        pdu.objects = new LinkedHashSet<>();
        data.objects().forEach(obj -> pdu.objects.add(UnclassifiedObjectPDU.toPDU(obj)));

        return pdu;
    }

    public UnclassifiedData toObject(DataFactory dataFactory) throws DataException {
        Objects.requireNonNull(dataFactory);

        Set<DataClass> resultClasses = new LinkedHashSet<>();
        classes.forEach(clazz -> resultClasses.add(clazz.toObject(dataFactory)));

        Set<UnclassifiedObject> resultObjects = new LinkedHashSet<>();
        for (UnclassifiedObjectPDU pdu : objects) {
            resultObjects.add(pdu.toObject(dataFactory));
        }

        return dataFactory.newUnclassifiedData(resultClasses, resultObjects);
    }
}
