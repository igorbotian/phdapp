package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.*;

/**
 * Попарный классификатор, поддерживающий интервальные экспертные оценки
 */
@Singleton
class IntervalPairwiseClassifier implements PairwiseClassifier {

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Конструктор объекта
     *
     * @param dataFactory фабрика объектов предметной области
     * @throws java.lang.NullPointerException если параметр не задан
     */
    @Inject
    public IntervalPairwiseClassifier(DataFactory dataFactory) {
        this.dataFactory = Objects.requireNonNull(dataFactory);
    }

    @Override
    public void train(PairwiseTrainingSet trainingSet) throws ClassifierTrainingException {
        // TODO
    }

    @Override
    public ClassifiedData classify(UnclassifiedData input, Set<? extends ClassifierParameter<?>> params)
            throws ClassificationException {

        try {
            return classifyRandomly(input); // TODO
        } catch (DataException e) {
            throw new IllegalStateException("Failed to create a classification data set", e);
        }
    }

    private ClassifiedData classifyRandomly(UnclassifiedData unclassifiedData) throws DataException {
        Set<ClassifiedObject> classifiedObjects = new HashSet<>();
        List<DataClass> classes = new ArrayList<>(unclassifiedData.classes());

        for (UnclassifiedObject obj : unclassifiedData.objects()) {
            DataClass clazz = chooseRandomClass(classes);
            classifiedObjects.add(dataFactory.newClassifiedObject(obj.id(), obj.parameters(), clazz));
        }

        return dataFactory.newClassifiedData(unclassifiedData.classes(), classifiedObjects);
    }

    private DataClass chooseRandomClass(List<DataClass> classes) {
        return classes.get(UniformedRandom.nextInteger(0, classes.size() - 1));
    }
}
