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

import org.junit.Assert;
import ru.spbftu.igorbotian.phdapp.common.DataException;

import java.io.IOException;

/**
 * Базовый класс для модульных тестов для класса <code>DataTypeAdapter</code>
 *
 * @param <T> тип значения параметра объекта исходных данных
 * @see ru.spbftu.igorbotian.phdapp.input.DataTypeAdapter
 */
public abstract class DataTypeAdapterTest<T> {

    protected void testSerialization(T testValue, DataTypeAdapter<T> adapter) throws IOException, DataException {
        Assert.assertEquals(testValue, adapter.fromString(adapter.toString(testValue)));
    }
}
