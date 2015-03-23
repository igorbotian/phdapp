package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;

import java.util.Set;

/**
 * Попарный классификатор с функцией обучения
 */
public interface PairwiseClassifier extends Classifier {

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
}
