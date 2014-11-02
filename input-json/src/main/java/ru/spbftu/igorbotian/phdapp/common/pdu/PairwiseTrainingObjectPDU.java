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

import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingObject;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingObject
 */
public class PairwiseTrainingObjectPDU {

    public Set<UnclassifiedObjectPDU> preferable;
    public Set<UnclassifiedObjectPDU> inferior;

    public static PairwiseTrainingObjectPDU toPDU(PairwiseTrainingObject obj) {
        PairwiseTrainingObjectPDU pdu = new PairwiseTrainingObjectPDU();

        pdu.inferior = new HashSet<>();
        obj.inferior().forEach(UnclassifiedObjectPDU::toPDU);

        pdu.preferable = new HashSet<>();
        obj.preferable().forEach(UnclassifiedObjectPDU::toPDU);

        return pdu;
    }

    public PairwiseTrainingObject toObject() throws DataException {
        Set<UnclassifiedObject> preferable = new HashSet<>();

        for(UnclassifiedObjectPDU pdu : this.preferable) {
            preferable.add(pdu.toObject());
        }

        Set<UnclassifiedObject> inferior = new HashSet<>();

        for(UnclassifiedObjectPDU pdu : this.inferior) {
            inferior.add(pdu.toObject());
        }

        return DataFactory.newPairwiseTrainingObject(preferable, inferior);
    }
}
