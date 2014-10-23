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

import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataException;

import java.io.IOException;

/**
 * Модульные тесты для класса <code>RealDataTypeAdapter</code>
 *
 * @see RealDataTypeAdapter
 * @see DataTypeAdapterTest
 */
public class RealDataTypeAdapterTest extends DataTypeAdapterTest<Double> {

    private DataTypeAdapter<Double> adapter;

    @Before
    public void setUp() {
        adapter = new RealDataTypeAdapter();
    }

    @Test
    public void testPositiveValueSerialization() throws IOException, DataException {
        testSerialization(5.0, adapter);
    }

    @Test
    public void testZeroSerialization() throws IOException, DataException {
        testSerialization(0.0, adapter);
    }

    @Test
    public void testNegativeValueSerialization() throws IOException, DataException {
        testSerialization(-5.0, adapter);
    }

    @Test(expected = DataException.class)
    public void testNonIntegerDeserialization() throws IOException, DataException {
        adapter.fromString("non-double");
    }
}
