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

import java.util.Collections;

/**
 * Модульные тесты для класса <code>Judgement</code>
 *
 * @see Judgement
 */
public class JudgementTest extends BaseDataTest<Judgement> {

    private final UnclassifiedObject firstItem = dataFactory.newUnclassifiedObject("firstItem",
            Collections.singleton(dataFactory.newParameter("param", "firstItem", BasicDataTypes.STRING)));
    private final UnclassifiedObject secondItem = dataFactory.newUnclassifiedObject("secondItem",
            Collections.singleton(dataFactory.newParameter("param", "secondItem", BasicDataTypes.STRING)));
    private final UnclassifiedObject thirdItem = dataFactory.newUnclassifiedObject("thirdItem",
            Collections.singleton(dataFactory.newParameter("param", "thirdItem", BasicDataTypes.STRING)));

    private final Judgement obj = dataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstItem), Collections.singleton(secondItem)
    );
    private final Judgement similarObj = dataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstItem), Collections.singleton(secondItem)
    );

    private final Judgement differentObj = dataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstItem), Collections.singleton(thirdItem)
    );

    @Test(expected = IllegalArgumentException.class)
    public void testSameObjectInBothSets() {
        dataFactory.newPairwiseTrainingObject(Collections.singleton(firstItem), Collections.singleton(firstItem));
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
