package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.*;

/**
 * Базовый класс для модульных тестов, направленных на проверку реализации задачи квадратичного программирования.
 * Обучающая выборка состоит из следующих экспертных оценок:
 * {x1, x2} > {z1}
 * {x3} > {z2}
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class BaseQuadProgTest {

    /**
     * Идентификатора тестового параметра
     */
    protected static final String PARAM_ID = "test_param";

    /**
     * Точность сравнения вещественных чисел
     */
    protected static final double PRECISION = 0.01;

    /**
     * Множество множеств элементов (X), которые имеют предпочтения над другими множествами элементов
     * в экспертных оценках.
     * В данном тесте x1 = 11, x2 = 12, x3 = 13
     */
    protected static final double[][] preferable = new double[][] {
            {11.0, 12.0},
            {13.0}
    };

    /**
     * Множество множеств элементов (Z), над которыми имеются предпочтения со стороны других множествами элементов
     * в экспертных оценках.
     *
     * В данном тесте z1 = 1.0, z2 = 12.0
     */
    protected static final double[][] inferior = new double[][] {
            {1.0},
            {2.0}
    };

    /**
     * Генератор случайных чисел
     */
    protected static Random random;

    /**
     * Фабрика объектов предметной области
     */
    protected static DataFactory dataFactory;

    /**
     * Обучающая выборка
     */
    protected LinkedHashSet<Judgement> trainingSet;

    @BeforeClass
    public static void init() {
        Injector injector = Guice.createInjector(Arrays.asList(new DataModule()));

        dataFactory = injector.getInstance(DataFactory.class);
        random = new Random(System.currentTimeMillis());
    }

    @Before
    public void setUp() {
        trainingSet = new LinkedHashSet<>();

        int preferableCount = 1;
        int inferiorCount = 1;

        for(int i = 0; i < preferable.length; i++) {
            Assert.assertTrue(preferable.length == inferior.length);

            Map<String, Double> preferableItems = new LinkedHashMap<>();
            Map<String, Double> inferiorItems = new LinkedHashMap<>();

            for(int j = 0; j < preferable[i].length; j++) {
                preferableItems.put("x" + preferableCount, preferable[i][j]);
                preferableCount++;
            }

            for(int j = 0; j < inferior[i].length; j++) {
                inferiorItems.put("z" + inferiorCount, inferior[i][j]);
                inferiorCount++;
            }

            trainingSet.add(makeJudgement(
                    makeJudgementItems(preferableItems),
                    makeJudgementItems(inferiorItems)
            ));
        }
    }

    private Set<JudgementItem> makeJudgementItems(Map<String, Double> data) {
        Set<JudgementItem> items = new LinkedHashSet<>();
        data.forEach((id, value) -> items.add(new JudgementItem(id, value)));
        return items;
    }

    private Judgement makeJudgement(Set<JudgementItem> preferable, Set<JudgementItem> inferior) {
        Set<UnclassifiedObject> preferableItems = new LinkedHashSet<>();
        preferable.forEach(item -> preferableItems.add(
                        dataFactory.newUnclassifiedObject(
                                item.id,
                                Collections.singleton(
                                        dataFactory.newParameter(PARAM_ID, item.value, BasicDataTypes.REAL))
                        )
                )
        );

        Set<UnclassifiedObject> inferiorItems = new HashSet<>();
        inferior.forEach(item -> inferiorItems.add(
                        dataFactory.newUnclassifiedObject(
                                item.id,
                                Collections.singleton(
                                        dataFactory.newParameter(PARAM_ID, item.value, BasicDataTypes.REAL))
                        )
                )
        );

        return dataFactory.newPairwiseTrainingObject(preferableItems, inferiorItems);
    }

    //-------------------------------------------------------------------------

    /**
     * Элемент множества более предпочтительных или менее предпочтительных данных, из которых состоит экспертная оценка
     */
    protected static class JudgementItem {

        /**
         * Идентификатор объекта
         */
        public final String id;

        /**
         * Значение параметра объекта (любое; а также любой тип)
         */
        public final Double value;

        public JudgementItem(String id, double value) {
            this.id = id;
            this.value = value;
        }
    }
}
