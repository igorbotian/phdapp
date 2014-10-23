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

import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.TrainingDataObject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingDataObject
 */
public final class TrainingDataObjectPDU {

    public String id;
    public Set<DataObjectParameterPDU> params;
    public DataClassPDU realClass;

    public static TrainingDataObjectPDU toPDU(TrainingDataObject obj) {
        TrainingDataObjectPDU pdu = new TrainingDataObjectPDU();

        pdu.id = obj.id();
        pdu.params = new LinkedHashSet<>();
        obj.parameters().forEach(param -> pdu.params.add(DataObjectParameterPDU.toPDU(param)));
        pdu.realClass = DataClassPDU.toPDU(obj.realClass());

        return pdu;
    }

    public TrainingDataObject toObject() throws DataException {
        Set<Parameter<?>> params = new LinkedHashSet<>();

        for(DataObjectParameterPDU param : this.params) {
            params.add(param.toObject());
        }
        return DataFactory.newTrainingObject(id, params, realClass.toObject());
    }
}
