package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Тест для средства преобразования обучающей выборки, состоящей из интервальных экспертных оценок предпочтений,
 * в эквивалентную обучающую выборку, состояющую только из точных оценок предпочтений
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class HausdorffTrainingSetTransformerTest {

    /**
     * Фабрика объектов предметной области
     */
    private DataFactory dataFactory;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new DataModule());
        dataFactory = injector.getInstance(DataFactory.class);
    }

    @Test
    public void testTransformation() throws ClassifierTrainingException {
        PairwiseTrainingSet expectedSolution = makeExpectedSolution();
        PairwiseTrainingSet solution = HausdorffTrainingSetTransformer.transformToPrecise(
                makeTrainingSet(),
                dataFactory
        );

        Assert.assertTrue(areTrainingSetsEquals(expectedSolution, solution));
    }

    //-------------------------------------------------------------------------

    private PairwiseTrainingSet makeTrainingSet() {
        return dataFactory.newPairwiseTrainingSet(Stream.of(
                dataFactory.newJudgement(
                        Stream.of(
                                newPoint(10, 15),
                                newPoint(15, 20)
                        ).collect(Collectors.toSet()),
                        Stream.of(
                                newPoint(1, 2),
                                newPoint(3, 4)
                        ).collect(Collectors.toSet())
                ),
                dataFactory.newJudgement(
                        Stream.of(
                                newPoint(5, 6),
                                newPoint(7, 8)
                        ).collect(Collectors.toSet()),
                        Stream.of(
                                newPoint(-1, -2),
                                newPoint(-3, -4)
                        ).collect(Collectors.toSet())
                )
        ).collect(Collectors.toSet()));
    }

    private UnclassifiedObject newPoint(double x, double y) {
        return dataFactory.newUnclassifiedObject(
                String.format("(%f, %f)", x, y),
                Stream.of(
                        dataFactory.newParameter("x", x, BasicDataTypes.REAL),
                        dataFactory.newParameter("y", y, BasicDataTypes.REAL)
                ).collect(Collectors.toSet())
        );
    }

    private PairwiseTrainingSet makeExpectedSolution() {
        return dataFactory.newPairwiseTrainingSet(Stream.of(
                dataFactory.newJudgement(
                        Collections.singleton(newPoint(15, 20)),
                        Collections.singleton(newPoint(2.12078, 3.12078))
                ),
                dataFactory.newJudgement(
                        Collections.singleton(newPoint(6.70912, 5.70921)),
                        Collections.singleton(newPoint(-3, -4))
                )
        ).collect(Collectors.toSet()));
    }

    //-------------------------------------------------------------------------

    private boolean areTrainingSetsEquals(PairwiseTrainingSet first, PairwiseTrainingSet second) {
        if (first.judgements().size() != second.judgements().size()) {
            return false;
        }

        Iterator<? extends Judgement> firstIt = first.judgements().iterator();
        Iterator<? extends Judgement> secondIt = first.judgements().iterator();

        while (firstIt.hasNext()) {
            if (!areJudgementsEqual(firstIt.next(), secondIt.next())) {
                return false;
            }
        }

        return true;
    }

    private boolean areJudgementsEqual(Judgement first, Judgement second) {
        return areJudgementPartsEqual(first.preferable(), second.preferable())
                && areJudgementPartsEqual(first.inferior(), second.inferior());
    }

    private boolean areJudgementPartsEqual(Set<? extends UnclassifiedObject> first,
                                           Set<? extends UnclassifiedObject> second) {
        if(first.size() != second.size()) {
            return false;
        }

        Iterator<? extends UnclassifiedObject> firstIt = first.iterator();
        Iterator<? extends UnclassifiedObject> secondIt = second.iterator();

        while(firstIt.hasNext()) {
            if(!areObjectsEqualByValue(firstIt.next(), secondIt.next())) {
                return false;
            }
        }

        return true;
    }

    private boolean areObjectsEqualByValue(UnclassifiedObject first, UnclassifiedObject second) {
        if(first.parameters().size() != second.parameters().size()) {
            return false;
        }

        Iterator<? extends Parameter<?>> firstIt = first.parameters().iterator();
        Iterator<? extends Parameter<?>> secondIt = first.parameters().iterator();

        while(firstIt.hasNext()) {
            if(!Objects.equals(firstIt.next(), secondIt.next())) {
                return false;
            }
        }

        return true;
    }
}
