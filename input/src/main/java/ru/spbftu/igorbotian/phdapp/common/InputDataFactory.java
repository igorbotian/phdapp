package ru.spbftu.igorbotian.phdapp.common;

import java.util.Set;

/**
 * Фабрика объектов наборов исходных данных
 */
public interface InputDataFactory {

    /**
     * Создание объекта типа <code>PointwiseInputData</code>
     *
     * @param classes     набор классов, которые будут участвовать в классификации объектов (не меньше двух)
     * @param trainingSet обучающая выборка (классифицированные объекты)
     * @param objects     множество объектов, подлежащих классификации
     * @return объект типа <code>PointwiseInputData</code>
     * @throws DataException если набор классов содержит меньше, чем минимально необходимое, количество элементов;
     *                       если множество объектов является пустым;
     *                       если множество объектов имеет хотя бы один объект,
     *                       отличающийся от других множеством определяюмых его параметров
     * @see ru.spbftu.igorbotian.phdapp.common.PointwiseInputData
     */
    PointwiseInputData newPointwiseData(Set<? extends DataClass> classes,
                                        Set<? extends PointwiseTrainingObject> trainingSet,
                                        Set<? extends UnclassifiedObject> objects) throws DataException;

    /**
     * Создание объекта типа <code>PairwiseInputData</code>
     *
     * @param classes     набор классов, которые будут участвовать в классификации объектов (не меньше двух)
     * @param trainingSet обучающая выборка (множество пар предпочтений)
     * @param objects     множество объектов, подлежащих классификации
     * @return объект типа <code>PairwiseInputData</code>
     * @throws DataException если набор классов содержит меньше, чем минимально необходимое, количество элементов
     * @see ru.spbftu.igorbotian.phdapp.common.PairwiseInputData
     */
    PairwiseInputData newPairwiseData(Set<? extends DataClass> classes,
                                      Set<? extends PairwiseTrainingObject> trainingSet,
                                      Set<? extends UnclassifiedObject> objects) throws DataException;
}
