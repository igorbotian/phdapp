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

package ru.spbftu.igorbotian.phdapp.input;

import ru.spbftu.igorbotian.phdapp.common.BasicDataTypes;
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.DataType;

import java.util.Objects;

/**
 * Адаптер для серилазиции и десериализации вещественных чисел в и из строкового представления
 *
 * @see DataTypeAdapter
 */
public class RealDataTypeAdapter implements DataTypeAdapter<Double> {

    @Override
    public DataType<Double> targetType() {
        return BasicDataTypes.REAL;
    }

    @Override
    public String toString(Double value) {
        return Double.toString(Objects.requireNonNull(value));
    }

    @Override
    public Double fromString(String str) throws DataException {
        try {
            return Double.parseDouble(Objects.requireNonNull(str));
        } catch (NumberFormatException e) {
            throw new DataException("Failed to parse a double from a given string: " + str, e);
        }
    }
}
