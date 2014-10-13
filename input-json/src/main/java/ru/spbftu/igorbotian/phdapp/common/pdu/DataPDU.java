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

import ru.spbftu.igorbotian.phdapp.common.Data;
import ru.spbftu.igorbotian.phdapp.common.DataClass;
import ru.spbftu.igorbotian.phdapp.common.DataObject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Data
 */
public final class DataPDU {

    public Set<DataClassPDU> classes;
    public Set<DataObjectPDU> objects;

    public static DataPDU toPDU(Data data) {
        DataPDU pdu = new DataPDU();

        pdu.classes = new LinkedHashSet<>();
        data.classes().forEach(clazz -> pdu.classes.add(DataClassPDU.toPDU(clazz)));

        pdu.objects = new LinkedHashSet<>();
        data.objects().forEach(obj -> pdu.objects.add(DataObjectPDU.toPDU(obj)));

        return pdu;
    }

    public Data toObject() {
        Set<DataClass> classes = new LinkedHashSet<>();
        this.classes.forEach(clazz -> classes.add(clazz.toObject()));

        Set<DataObject> objects = new LinkedHashSet<>();
        this.objects.forEach(obj -> objects.add(obj.toObject()));

        return new Data(classes, objects);
    }
}
