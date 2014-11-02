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
 * Модульные тесты для класса <code>PairwiseTrainingSet</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet
 */
public class PairwiseTrainingSetTest extends BaseDataTest<PairwiseTrainingSet> {

    private final UnclassifiedObject firstObj = DataFactory.newUnclassifiedObject("firstObj",
            Collections.singleton(DataFactory.newParameter("param", "firstObj", BasicDataTypes.STRING)));
    private final UnclassifiedObject secondObj = DataFactory.newUnclassifiedObject("secondObj",
            Collections.singleton(DataFactory.newParameter("param", "secondObj", BasicDataTypes.STRING)));
    private final UnclassifiedObject thirdObj = DataFactory.newUnclassifiedObject("thirdObj",
            Collections.singleton(DataFactory.newParameter("param", "thirdObj", BasicDataTypes.STRING)));

    private final PairwiseTrainingObject firstPair = DataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstObj), Collections.singleton(secondObj)
    );
    private final PairwiseTrainingObject secondPair = DataFactory.newPairwiseTrainingObject(
            Collections.singleton(firstObj), Collections.singleton(thirdObj)
    );

    private final PairwiseTrainingSet obj = DataFactory.newPairwiseTrainingSet(Collections.singleton(firstPair));
    private final PairwiseTrainingSet similarObj = DataFactory.newPairwiseTrainingSet(Collections.singleton(firstPair));
    private final PairwiseTrainingSet differentObj = DataFactory.newPairwiseTrainingSet(Collections.singleton(secondPair));

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
    }

    @Test
    public void testEqual() {
        testEquals(obj, differentObj, similarObj);
    }
}
