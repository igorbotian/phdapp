package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Set;

/**
 * Ранжирующий попарный классификатор данных
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public interface RankingPairwiseClassifier {

    /**
     * Обучение попарного классификатора с помощью обучающей выборки
     *
     * @param trainingSet обучающая выборка
     * @param params      параметры обучения
     * @throws ClassifierTrainingException в случае проблемы обучаения
     * @throws NullPointerException        если хотя бы один из параметров не задан
     */
    void train(PairwiseTrainingSet trainingSet, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException;

    /**
     * Ранжирующая классификация для двух заданных объектов.
     * Заключается в выявлении того, какой из них является более предпочтительным на основе значений параметров объектов.
     *
     * @param first  первый объект в паре
     * @param second второй объект в паре
     * @param params параметры классификации
     * @return <code>true</code>, если первый объект предпочтительнее второго; иначе <code>false</code>
     * @throws ClassificationException  в случае ошибки классификации
     * @throws NullPointerException     если хотя бы один аргумент на задан
     * @throws IllegalArgumentException если оба объекта идентичны
     */
    boolean classify(UnclassifiedObject first, UnclassifiedObject second, Set<? extends ClassifierParameter<?>> params)
            throws ClassificationException;
}
