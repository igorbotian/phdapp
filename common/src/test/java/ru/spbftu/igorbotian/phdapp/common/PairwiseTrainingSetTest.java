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
 * Модульные тесты для класса <code>PairwiseTrainingSet</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet
 */
public class PairwiseTrainingSetTest extends BaseDataTest<PairwiseTrainingSet> {

    private final UnclassifiedObject firstObj = dataFactory.newUnclassifiedObject("firstObj",
            Collections.singleton(dataFactory.newParameter("param", "firstObj", BasicDataTypes.STRING)));
    private final UnclassifiedObject secondObj = dataFactory.newUnclassifiedObject("secondObj",
            Collections.singleton(dataFactory.newParameter("param", "secondObj", BasicDataTypes.STRING)));
    private final UnclassifiedObject thirdObj = dataFactory.newUnclassifiedObject("thirdObj",
            Collections.singleton(dataFactory.newParameter("param", "thirdObj", BasicDataTypes.STRING)));

    private final Judgement firstPair = dataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstObj), Collections.singleton(secondObj)
    );
    private final Judgement secondPair = dataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstObj), Collections.singleton(thirdObj)
    );

    private final PairwiseTrainingSet obj = dataFactory.newPairwiseTrainingSet(Collections.singleton(firstPair));
    private final PairwiseTrainingSet similarObj = dataFactory.newPairwiseTrainingSet(Collections.singleton(firstPair));
    private final PairwiseTrainingSet differentObj = dataFactory.newPairwiseTrainingSet(Collections.singleton(secondPair));

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
    }

    @Test
    public void testEqual() {
        testEquals(obj, differentObj, similarObj);
    }
}
