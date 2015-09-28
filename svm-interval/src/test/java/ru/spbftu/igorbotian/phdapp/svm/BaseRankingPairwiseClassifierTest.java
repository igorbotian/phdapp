package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingModule;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * Базовый тест для средства классификации данных
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public abstract class BaseRankingPairwiseClassifierTest {

    /**
     * Идентификатора тестового параметра
     */
    protected static final String PARAM_ID = "test_param";

    /**
     * Фабрика объектов предметной области
     */
    private DataFactory dataFactory;

    /**
     * Средство классификации
     */
    private RankingPairwiseClassifier classifier;

    /**
     * Параметры средства классификации
     */
    private IntervalClassifierParameterFactory parameters;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(Arrays.asList(
                new ApplicationConfigurationModule(Paths.get("..")),
                new DataModule(),
                new QuadraticProgrammingModule(),
                rankingPairwiseClassifierModule()
        ));
        dataFactory = injector.getInstance(DataFactory.class);
        classifier = injector.getInstance(IntervalRankingPairwiseClassifier.class);
        parameters = injector.getInstance(IntervalClassifierParameterFactory.class);
    }

    @Test
    public void testClassifier() throws ClassificationException {
        classifier.train(makeTrainingSet(), parameters.defaultValues());

        for(Pair<UnclassifiedObject, UnclassifiedObject> pair : pairsToClassify()) {
            Assert.assertTrue(classifier.classify(pair.first, pair.second, Collections.emptySet()));
        }
    }

    protected abstract PhDAppModule rankingPairwiseClassifierModule();

    protected abstract Set<Pair<UnclassifiedObject, UnclassifiedObject>> pairsToClassify();

    protected abstract PairwiseTrainingSet makeTrainingSet();

    protected PairwiseTrainingSet makeTrainingSet(Set<? extends Judgement> judgements) {
        return dataFactory.newPairwiseTrainingSet(judgements);
    }

    protected Judgement makeJudgement(Set<? extends UnclassifiedObject> preferable, Set<? extends UnclassifiedObject> inferior) {
        return dataFactory.newJudgement(preferable, inferior);
    }

    protected Judgement makeJudgement(UnclassifiedObject preferable, UnclassifiedObject inferior) {
        return makeJudgement(Collections.singleton(preferable), Collections.singleton(inferior));
    }

    protected UnclassifiedObject makeObject(String id, double value) {
        Parameter<?> param = dataFactory.newParameter(PARAM_ID, value, BasicDataTypes.REAL);
        return dataFactory.newUnclassifiedObject(id, Collections.singleton(param));
    }
}
