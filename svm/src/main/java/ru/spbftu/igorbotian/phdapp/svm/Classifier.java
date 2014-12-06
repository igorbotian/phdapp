package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.ClassifiedData;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedData;

/**
 * Классификатор данных
 */
public interface Classifier {

    /**
     * Классификация заданного набора данных с заданными значениями параметров классификатора
     *
     * @param input  данные, которые требуется классификацировать
     * @param params параметры класссификатора
     * @return классифицированные исходные данные
     * @throws ru.spbftu.igorbotian.phdapp.svm.ClassificationException в случае проблемы классификации заданных данных
     *                                                                 с заданными параметрами
     * @throws java.lang.NullPointerException                          если хотя бы один из параметров не задан
     */
    ClassifiedData classify(UnclassifiedData input, ClassifierParams params) throws ClassificationException;
}
