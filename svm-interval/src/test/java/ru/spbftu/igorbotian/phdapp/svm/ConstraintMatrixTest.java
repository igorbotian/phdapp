/*
 * Copyright (c) 2015 Igor Botian
 *
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
 */

package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.*;

/**
 * Модульные тесты для класса <code>ConstraintMatrix</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ConstraintMatrixTest {

    /**
     * Значение параметра штрафа (любое значение)
     */
    private static final double PENALTY_PARAM = 0.5;

    /**
     * Точность сравнения вещественных чисел
     */
    private static final double PRECISION = 0.1;

    /**
     * Идентификатор тестового параметра
     */
    private static final String PARAM_ID = "test_param";

    /**
     * Фабрика объектов предметной области
     */
    private static DataFactory dataFactory;

    /**
     * Генератор случайных чисел
     */
    private static Random random;

    /**
     * Обучающая выборка
     */
    private LinkedHashSet<? extends PairwiseTrainingObject> trainingSet;

    /**
     * Первая экспертная оценка, входящая в обучающую выборку
     */
    private PairwiseTrainingObject firstJudgement;

    /**
     * Вторая экспертная оценка, входящая в обучающую выборку
     */
    private PairwiseTrainingObject secondJudgement;

    /**
     * Ожидаемое значение строк и столбцов матрицы ограничений
     */
    private double[][] expectedCoefficients = new double[][]{
            {1.0, 1.0, 0.0},
            {-1.0, -1.0, 0.0},
            {0.0, 0.0, 1.0},
            {0.0, 0.0, -1.0}
    };

    @BeforeClass
    public static void init() {
        Injector injector = Guice.createInjector(Arrays.asList(new DataModule()));

        dataFactory = injector.getInstance(DataFactory.class);
        random = new Random(System.currentTimeMillis());
    }

    @Before
    public void setUp() {
        Map<String, Integer> firstJudgementPreferableItems = new HashMap<>();
        firstJudgementPreferableItems.put("x1", random.nextInt());
        firstJudgementPreferableItems.put("x2", random.nextInt());

        Map<String, Integer> firstJudgementInferiorItems = Collections.singletonMap("z3", random.nextInt());
        Map<String, Integer> secondJudgementPreferableItems = Collections.singletonMap("x3", random.nextInt());
        Map<String, Integer> secondJudgementInferiorItems = Collections.singletonMap("z1", random.nextInt());

        firstJudgement = makeJudgement(
                makeJudgementItems(firstJudgementPreferableItems),
                makeJudgementItems(firstJudgementInferiorItems)
        );

        secondJudgement = makeJudgement(
                makeJudgementItems(secondJudgementPreferableItems),
                makeJudgementItems(secondJudgementInferiorItems)
        );

        this.trainingSet = new LinkedHashSet<>(Arrays.asList(firstJudgement, secondJudgement));
    }

    private Set<JudgementItem> makeJudgementItems(Map<String, Integer> data) {
        Set<JudgementItem> items = new HashSet<>();
        data.forEach((id, value) -> items.add(new JudgementItem(id, value)));
        return items;
    }

    private PairwiseTrainingObject makeJudgement(Set<JudgementItem> preferable, Set<JudgementItem> inferior) {
        Set<UnclassifiedObject> preferableItems = new HashSet<>();
        preferable.forEach(item -> preferableItems.add(
                        dataFactory.newUnclassifiedObject(
                                item.id,
                                Collections.singleton(
                                        dataFactory.newParameter(PARAM_ID, item.value, BasicDataTypes.INTEGER))
                        )
                )
        );

        Set<UnclassifiedObject> inferiorItems = new HashSet<>();
        inferior.forEach(item -> inferiorItems.add(
                        dataFactory.newUnclassifiedObject(
                                item.id,
                                Collections.singleton(
                                        dataFactory.newParameter(PARAM_ID, item.value, BasicDataTypes.INTEGER))
                        )
                )
        );

        return dataFactory.newPairwiseTrainingObject(preferableItems, inferiorItems);
    }

    //-------------------------------------------------------------------------


    @Test
    public void testCoefficientsForJudgement() {
        ConstraintMatrix matrix = new ConstraintMatrix(PENALTY_PARAM, trainingSet);
        double[][] coeffs = matrix.coefficientsForJudgement(firstJudgement);

        Assert.assertEquals(2, coeffs.length);
        Assert.assertArrayEquals(expectedCoefficients[0], coeffs[0], PRECISION);
        Assert.assertArrayEquals(expectedCoefficients[1], coeffs[1], PRECISION);

        coeffs = matrix.coefficientsForJudgement(secondJudgement);

        Assert.assertEquals(2, coeffs.length);
        Assert.assertArrayEquals(expectedCoefficients[2], coeffs[0], PRECISION);
        Assert.assertArrayEquals(expectedCoefficients[3], coeffs[1], PRECISION);
    }

    @Test
    public void testConstraintVector() {
        ConstraintMatrix matrix = new ConstraintMatrix(PENALTY_PARAM, trainingSet);
        double[] data = matrix.constraintVector();
        Assert.assertArrayEquals(new double[]{0.0, -PENALTY_PARAM, 0.0, -PENALTY_PARAM}, data, PRECISION);
    }

    @Test
    public void testCoefficientVector() {
        ConstraintMatrix matrix = new ConstraintMatrix(PENALTY_PARAM, trainingSet);
        double[][] data = matrix.coefficientVector();

        Assert.assertEquals(expectedCoefficients.length, data.length);

        for (int i = 0; i < expectedCoefficients.length; i++) {
            Assert.assertArrayEquals(expectedCoefficients[i], data[i], PRECISION);
        }
    }

    //-------------------------------------------------------------------------

    /**
     * Элемент множества более предпочтительных или менее предпочтительных данных, из которых состоит экспертная оценка
     */
    private static class JudgementItem {

        /**
         * Идентификатор объекта
         */
        public final String id;

        /**
         * Значение параметра объекта (любое; а также любой тип)
         */
        public final Integer value;

        public JudgementItem(String id, int value) {
            this.id = id;
            this.value = value;
        }
    }
}
