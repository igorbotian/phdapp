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

package ru.spbftu.igorbotian.phdapp.common;

import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.Collections;

/**
 * Модульные тесты для класса <code>PairwiseTrainingObject</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingObject
 */
public class PairwiseTrainingObjectTest extends BaseDataTest<PairwiseTrainingObject> {

    private final UnclassifiedObject firstItem = DataFactory.newUnclassifiedObject("firstItem",
            Collections.singleton(DataFactory.newParameter("param", "firstItem", BasicDataTypes.STRING)));
    private final UnclassifiedObject secondItem = DataFactory.newUnclassifiedObject("secondItem",
            Collections.singleton(DataFactory.newParameter("param", "secondItem", BasicDataTypes.STRING)));
    private final UnclassifiedObject thirdItem = DataFactory.newUnclassifiedObject("thirdItem",
            Collections.singleton(DataFactory.newParameter("param", "thirdItem", BasicDataTypes.STRING)));

    private final PairwiseTrainingObject obj = DataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstItem), Collections.singleton(secondItem)
    );
    private final PairwiseTrainingObject similarObj = DataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstItem), Collections.singleton(secondItem)
    );

    private final PairwiseTrainingObject differentObj = DataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstItem), Collections.singleton(thirdItem)
    );

    @Test(expected = IllegalArgumentException.class)
    public void testSameObjectInBothSets() {
        DataFactory.newPairwiseTrainingObject(Collections.singleton(firstItem), Collections.singleton(firstItem));
    }

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
    }

    @Test
    public void testEqual() {
        testEquals(obj, differentObj, similarObj);
    }
}
