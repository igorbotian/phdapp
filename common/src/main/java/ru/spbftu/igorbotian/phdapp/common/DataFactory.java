package ru.spbftu.igorbotian.phdapp.common;

import java.util.Set;

/**
 * Фабрика объектов предметной области
 */
public interface DataFactory {

    /**
     * Создание объекта типа <code>DataClass</code>
     *
     * @param name идентификатор класса (не может быть пустым)
     * @throws java.lang.NullPointerException     если идентификатор класса не задан
     * @throws java.lang.IllegalArgumentException если идентификатор класса пустой
     * @see ru.spbftu.igorbotian.phdapp.common.DataClass
     */
    DataClass newClass(String name);

    /**
     * Создание множества объектов классов классификации на основе их имён
     *
     * @param names названия классов (каждое не может быть пустым)
     * @return множество классов
     */
    Set<DataClass> newClasses(String... names);

    /**
     * Создание множества объектов классов классификации на основе их имён
     *
     * @param names названия классов (каждое не может быть пустым)
     * @return множество классов
     */
    Set<DataClass> newClasses(Set<String> names);

    /**
     * Создание объекта типа <code>Parameter</code>
     *
     * @param name  название объекта (непустое)
     * @param value значение объекта
     * @return объект типа <code>Parameter</code> с заданными параметрами
     * @throws java.lang.IllegalArgumentException если название объекта пустое
     * @throws java.lang.NullPointerException     если хотя бы один из параметров не задан
     * @see ru.spbftu.igorbotian.phdapp.common.Parameter
     */
    <V> Parameter<V> newParameter(String name, V value, DataType<V> valueType);

    /**
     * Создание объекта типа <code>UnclassifiedObject</code>
     *
     * @param id     строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param params множество параметров, которыми характеризуется объект (непустое)
     * @return объект типа <code>UnclassifiedObject</code> с заданными параметрами
     * @throws java.lang.NullPointerException     если множества параметров равно <code>null</code>
     * @throws java.lang.IllegalArgumentException если идентификатор объекта пустой или множество параметров
     *                                            не содержит ни одного элемента
     * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject
     */
    UnclassifiedObject newUnclassifiedObject(String id, Set<Parameter<?>> params);

    /**
     * Создание объекта типа <code>ClassifiedObject</code>
     *
     * @param id        строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param params    множество параметров, которыми характеризуется объект (непустое)
     * @param dataClass класс, которому соответствует объект и который получен в результате классификации
     * @return объект типа <code>ClassifiedObject</code> с заданными параметрами
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedObject
     */
    ClassifiedObject newClassifiedObject(String id, Set<Parameter<?>> params, DataClass dataClass);

    /**
     * Создание объекта типа <code>ClassifiedObject</code>
     *
     * @param id        строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param params    множество параметров, которыми характеризуется объект (непустое)
     * @param realClass реальный класса классификации, которому соответствует объект
     * @return объект типа <code>PointwiseTrainingObject</code> с заданными параметрами
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     * @see ru.spbftu.igorbotian.phdapp.common.PointwiseTrainingObject
     */
    PointwiseTrainingObject newPointwiseTrainingObject(String id, Set<Parameter<?>> params, DataClass realClass);

    /**
     * Создание объекта типа <code>Judgement</code>
     *
     * @param preferable набор исходных объектов, который предпочтителен другого набора
     * @param inferior   набор исходных объектов, над которым другой набор имеет предпочтение
     * @return объект типа <code>Judgement</code> с заданными параметрами
     * @throws java.lang.NullPointerException     если хотя бы один из параметров не задан
     * @throws java.lang.IllegalArgumentException если хотя бы один объект из одного набора также содержится и во втором
     */
    Judgement newJudgement(Set<? extends UnclassifiedObject> preferable,
                           Set<? extends UnclassifiedObject> inferior);

    /**
     * Создание объекта типа <code>UnclassifiedData</code>
     *
     * @param classes непустой набор классов классификации размером не меньше двух
     * @param objects непустое множество объектов
     * @return объект типа <code>UnclassifiedData</code> с заданными параметрами
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если набор классов содержит меньше,
     *                                                          чем минимально необходимое, количество элементов;
     *                                                          если множество объектов является пустым;
     *                                                          если множество объектов имеет хотя бы один объект,
     *                                                          отличающийся от других множеством определяюмых его параметров
     * @throws java.lang.NullPointerException                   если множество классов или множество объектов равно <code>null</code>
     * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedData
     */
    UnclassifiedData newUnclassifiedData(Set<? extends DataClass> classes,
                                         Set<? extends UnclassifiedObject> objects) throws DataException;

    /**
     * Создание объекта типа <code>ClassifiedData</code>
     *
     * @param classes непустой набор классов классификации размером не меньше двух
     * @param objects непустое множество классифицированных объектов
     * @return объект типа <code>ClassifiedData</code> с заданными параметрами
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если набор классов содержит меньше,
     *                                                          чем минимально необходимое, количество элементов;
     *                                                          если множество объектов является пустым;
     *                                                          если множество объектов имеет хотя бы один объект,
     *                                                          отличающийся от других множеством определяюмых его параметров
     * @throws java.lang.NullPointerException                   если множество классов или множество объектов равно <code>null</code>
     * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedData
     */
    ClassifiedData newClassifiedData(Set<? extends DataClass> classes,
                                     Set<? extends ClassifiedObject> objects) throws DataException;

    /**
     * Создание объекта типа <code>PointwiseTrainingSet</code>
     *
     * @param classes непустой набор классов классификации размером не меньше двух
     * @param objects непустое множество классифицированных объектов
     * @return объект типа <code>PointwiseTrainingSet</code> с заданными параметрами
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если набор классов содержит меньше,
     *                                                          чем минимально необходимое, количество элементов;
     *                                                          если множество объектов является пустым;
     *                                                          если множество объектов имеет хотя бы один объект,
     *                                                          отличающийся от других множеством определяюмых его параметров
     * @throws java.lang.NullPointerException                   если множество классов или множество объектов равно <code>null</code>
     * @see ru.spbftu.igorbotian.phdapp.common.PointwiseTrainingSet
     */
    PointwiseTrainingSet newPointwiseTrainingSet(Set<? extends DataClass> classes,
                                                 Set<? extends PointwiseTrainingObject> objects) throws DataException;

    /**
     * Создание объекта типа <code>PairwiseTrainingSet</code>
     *
     * @param objects элементы обучающей выборки
     * @return объект типа <code>PairwiseTrainingSet</code> с заданными параметрами
     * @throws java.lang.NullPointerException если множество элементов обучающей выборки не задано
     */
    PairwiseTrainingSet newPairwiseTrainingSet(Set<? extends Judgement> objects);
}
