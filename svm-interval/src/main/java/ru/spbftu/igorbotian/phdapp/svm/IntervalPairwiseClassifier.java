package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Попарный классификатор, поддерживающий интервальные экспертные оценки
 */
public class IntervalPairwiseClassifier implements PairwiseClassifier {

    @Override
    public void train(PairwiseTrainingSet trainingSet) throws ClassifierTrainingException {
        // TODO
    }

    @Override
    public ClassifiedData classify(UnclassifiedData input, ClassifierParams params) throws ClassificationException {
        try {
            return classifyRandomly(input); // TODO
        } catch (DataException e) {
            throw new IllegalStateException("Failed to create a classification data set", e);
        }
    }

    private ClassifiedData classifyRandomly(UnclassifiedData unclassifiedData) throws DataException {
        Set<DataClass> classes = new HashSet<>();
        Set<ClassifiedObject> classifiedObjects = new HashSet<>();

        for(UnclassifiedObject obj : unclassifiedData.objects()) {
            DataClass clazz = chooseRandomClass(unclassifiedData.classes());
            classes.add(clazz);
            classifiedObjects.add(DataFactory.newClassifiedObject(obj.id(), obj.parameters(), clazz));
        }

        return DataFactory.newClassifiedData(classes, classifiedObjects);
    }

    private DataClass chooseRandomClass(Set<? extends DataClass> classes) {
        int chosenClassIndex = randomInt(0, classes.size() - 1);
        int i = 0;

        for (DataClass clazz : classes) {
            if (i == chosenClassIndex) {
                return clazz;
            }

            i++;
        }

        throw new IllegalStateException("Random class hasn't been chosen");
    }

    // нижняя граница включается, нижняя - нет
    private int randomInt(int lowerBound, int upperBound) {
        return lowerBound + (int) (Math.random() * (upperBound - lowerBound));
    }
}
