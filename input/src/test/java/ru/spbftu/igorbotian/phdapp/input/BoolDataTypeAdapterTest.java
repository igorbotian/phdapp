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
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataException;

import java.io.IOException;

/**
 * Модульные тесты для класса <code>RealDataValueTypeAdapter</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.input.BoolDataTypeAdapter
 * @see ru.spbftu.igorbotian.phdapp.input.DataTypeAdapterTest
 */
public class BoolDataTypeAdapterTest extends DataTypeAdapterTest<Boolean> {

    private DataTypeAdapter<Boolean> adapter;

    @Before
    public void setUp() {
        adapter = new BoolDataTypeAdapter();
    }

    @Test
    public void testTrue() throws IOException, DataException {
        testSerialization(true, adapter);
    }

    @Test
    public void testFalse() throws IOException, DataException {
        testSerialization(false, adapter);
    }

    @Test
    public void testNonBoolean() throws IOException, DataException {
        Assert.assertEquals(false, adapter.fromString("non-boolean"));
    }
}
