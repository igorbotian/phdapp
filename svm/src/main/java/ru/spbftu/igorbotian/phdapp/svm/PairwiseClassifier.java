package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;

/**
 * Попарный классификатор с функцией обучения
 */
public interface PairwiseClassifier extends Classifier {

    /**
     * Обучение попарного классификатора с помощью обучающей выборки
     *
     * @param trainingSet обучающая выборка
     * @throws ru.spbftu.igorbotian.phdapp.svm.ClassifierTrainingException в случае проблемы обучаения
     * @throws java.lang.NullPointerException                              если обучающая выбора не задана
     */
    void train(PairwiseTrainingSet trainingSet) throws ClassifierTrainingException;
}
