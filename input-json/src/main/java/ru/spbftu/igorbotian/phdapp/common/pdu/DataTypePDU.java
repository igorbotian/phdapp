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
import ru.spbftu.igorbotian.phdapp.common.DataType;
import ru.spbftu.igorbotian.phdapp.input.DataTypeAdapterRegistry;

/**
 * POJO-версия класса, предназначенная для использования в механизме сериализации
 *
 * @see ru.spbftu.igorbotian.phdapp.common.DataType
 */
public final class DataTypePDU<T> {

    public String name;

    public static <T> DataTypePDU<T> toPDU(DataType<T> type) {
        DataTypePDU<T> pdu = new DataTypePDU<>();
        pdu.name = type.name();
        return pdu;
    }

    @SuppressWarnings("unchecked")
    public DataType<T> toObject() throws DataException {
        // TODO в данном месте приведение типов небезопасно
        return (DataType<T>) DataTypeAdapterRegistry.INSTANCE.getTypeAdapterNamedAs(name).targetType();
    }
}
