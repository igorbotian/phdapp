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

import ru.spbftu.igorbotian.phdapp.common.ClassifiedDataObject;
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.Parameter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedDataObject
 */
public final class ClassifiedDataObjectPDU {

    public String id;
    public Set<ParameterPDU> params;
    public DataClassPDU realClass;

    public static ClassifiedDataObjectPDU toPDU(ClassifiedDataObject obj) {
        ClassifiedDataObjectPDU pdu = new ClassifiedDataObjectPDU();

        pdu.id = obj.id();
        pdu.params = new LinkedHashSet<>();
        obj.parameters().forEach(param -> pdu.params.add(ParameterPDU.toPDU(param)));
        pdu.realClass = DataClassPDU.toPDU(obj.realClass());

        return pdu;
    }

    public ClassifiedDataObject toObject() throws DataException {
        Set<Parameter<?>> params = new LinkedHashSet<>();

        for(ParameterPDU param : this.params) {
            params.add(param.toObject());
        }
        return DataFactory.newClassifiedObject(id, params, realClass.toObject());
    }
}
