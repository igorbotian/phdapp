package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.UniformedRandom;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация средства формирования выборки для кросс-валидации классификатора
 */
@Singleton
class CrossValidationSampleManagerImpl implements CrossValidationSampleManager {

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Средство генерации выборки для кросс-валидации классификатора
     */
    private CrossValidationSampleGenerator sampleGenerator;

    @Inject
    public CrossValidationSampleManagerImpl(DataFactory dataFactory, MathDataFactory mathDataFactory,
                                            ApplicationConfiguration appConfig) {
        this.dataFactory = Objects.requireNonNull(dataFactory);

        sampleGenerator = new CrossValidationSampleGeneratorImpl(dataFactory,
                Objects.requireNonNull(mathDataFactory),
                Objects.requireNonNull(appConfig));
    }

    @Override
    public CrossValidationSampleGenerator sampleGenerator() {
        return sampleGenerator;
    }

    //-------------------------------------------------------------------------

    @Override
    public ClassifiedData generateSample(int sampleSize) throws CrossValidationSampleException {
        checkSampleSize(sampleSize);

        sampleGenerator.regeneratePoints(sampleSize);

        Set<DataClass> dataClasses = Stream.of(
                sampleGenerator.firstSupportingPoint().dataClass(),
                sampleGenerator.secondSupportingPoint().dataClass()
        ).collect(Collectors.toSet());
        Set<ClassifiedObject> data = new HashSet<>();
        sampleGenerator.firstSetOfPoints().forEach(data::add);
        sampleGenerator.secondSetOfPoints().forEach(data::add);

        try {
            return dataFactory.newClassifiedData(dataClasses, data);
        } catch (DataException e) {
            throw new CrossValidationSampleException("Failed to generate sample", e);
        }
    }

    /**
     * Проверка того, что задаваемый размер выборки имеет допустимое значение
     */
    private void checkSampleSize(int sampleSize) {
        if (sampleSize < CrossValidatorParameterFactory.SAMPLE_SIZE_MIN) {
            throw new IllegalArgumentException(
                    String.format("Sample size (%d) cannot have a value lesser than the minimum (%d)",
                            sampleSize, CrossValidatorParameterFactory.SAMPLE_SIZE_MIN));
        }

        if (CrossValidatorParameterFactory.SAMPLE_SIZE_MAX - sampleSize < 0) {
            throw new IllegalArgumentException(
                    String.format("Sample size (%d) cannot have a value greater than the maximum (%d)",
                            sampleSize, CrossValidatorParameterFactory.SAMPLE_SIZE_MAX));
        }
    }

    //-------------------------------------------------------------------------

    @Override
    public Pair<ClassifiedData, ClassifiedData> divideSampleIntoTwoGroups(ClassifiedData sample, int ratio)
            throws CrossValidationSampleException {
        Objects.requireNonNull(sample);
        checkTrainingTestingSetsSizeRatio(ratio);

        Map<DataClass, LinkedList<ClassifiedObject>> sampleByClasses = groupSampleItemsByClasses(sample);
        checkSampleIsDividable(sampleByClasses);

        Pair<Set<ClassifiedObject>, Set<ClassifiedObject>> smallestValidSets = composeSmallestValidGroups(sampleByClasses);
        int firstSetSize = (int) Math.ceil(sample.objects().size() * ratio / 100);
        int secondSetSize = sample.objects().size() - firstSetSize;

        if (secondSetSize < UnclassifiedData.MIN_NUMBER_OF_CLASSES) {
            // проверка на минимально допустимый размер тестирующей/обучающей выборки
            secondSetSize = UnclassifiedData.MIN_NUMBER_OF_CLASSES;
            firstSetSize = sample.objects().size() - secondSetSize;
        }

        return divideSampleIntoTwoGroups(sample, smallestValidSets, firstSetSize);
    }

    /**
     * Проверка того, что процентное соотношение обучающей/тестирующей выборок имеет допустимое значение
     */
    private void checkTrainingTestingSetsSizeRatio(int ratio) {
        if (ratio < CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MIN) {
            throw new IllegalArgumentException(String.format("Ratio (%d) cannot be less than the minimum (%d)",
                    ratio, CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MIN));
        }

        if (ratio > CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MAX) {
            throw new IllegalArgumentException(String.format("Ratio (%d) cannot be greater than the maximum (%d)",
                    ratio, CrossValidatorParameterFactory.TRAINING_TESTING_SETS_SIZE_RATIO_MAX));
        }
    }

    /**
     * Перегруппировка объектов заданной выборки по классам её объектов
     */
    private Map<DataClass, LinkedList<ClassifiedObject>> groupSampleItemsByClasses(ClassifiedData sample) {
        Map<DataClass, LinkedList<ClassifiedObject>> sampleItemsByClasses = new HashMap<>();

        for (ClassifiedObject obj : sample.objects()) {
            if (!sampleItemsByClasses.containsKey(obj.dataClass())) {
                sampleItemsByClasses.put(obj.dataClass(), new LinkedList<>());
            }

            sampleItemsByClasses.get(obj.dataClass()).add(obj);
        }

        return sampleItemsByClasses;
    }

    /**
     * Проверка того, что выборка может разделена на две независимые и корректные выборки
     */
    private void checkSampleIsDividable(Map<DataClass, LinkedList<ClassifiedObject>> sampleItemsByClasses)
            throws CrossValidationSampleException {

        // нам необходимо, чтобы выборка была разделима, то есть в обучающей и тестирующей части
        // были объекты как минимум двух классов.
        // этого можно добиться, если у нас есть как минимум четыре объекта.
        // два из них должны принадлежать одному классу, а два других - либо одному, либо разным классам

        int result = 0;

        for (DataClass clazz : sampleItemsByClasses.keySet()) {
            assert (!sampleItemsByClasses.get(clazz).isEmpty());

            if (sampleItemsByClasses.get(clazz).size() < 2) {
                result++;
            } else {
                result += 2;
            }
        }

        if (result < 4) {
            throw new CrossValidationSampleException("The sample is not dividable into training and testing sets: " +
                    "there should be objects of at least two classes in each set");
        }
    }

    /**
     * Наполнение корректных обучающей и тестирующей выборок минимально допустимого размер на основе
     * заданного множества объектов
     */
    private Pair<ClassifiedData, ClassifiedData> divideSampleIntoTwoGroups(ClassifiedData sample,
                                                                           Pair<Set<ClassifiedObject>, Set<ClassifiedObject>> smallestValidSets,
                                                                           int firstGroupSize)
            throws CrossValidationSampleException {

        Set<ClassifiedObject> firstGroup = new HashSet<>(smallestValidSets.first);
        Set<ClassifiedObject> secondGroup = new HashSet<>(smallestValidSets.second);
        Iterator<? extends ClassifiedObject> it = sample.objects().iterator();
        int i = 0;

        // фомирование первой группы
        while (i < firstGroupSize) {
            assert (it.hasNext());
            ClassifiedObject obj = it.next();

            if (smallestValidSets.first.contains(obj)) {
                i++; // первая группа уже содержит этот объект
            } else if (!smallestValidSets.second.contains(obj)) {
                firstGroup.add(obj);
                i++;
            }
        }

        // фомирование второй группы
        while (it.hasNext()) {
            assert (it.hasNext());
            ClassifiedObject obj = it.next();

            if (!smallestValidSets.first.contains(obj)
                    && !smallestValidSets.second.contains(obj)) {
                secondGroup.add(obj);
            }
        }

        assert (!it.hasNext());

        try {
            return new Pair<>(
                    dataFactory.newClassifiedData(dataClassesOf(firstGroup), firstGroup),
                    dataFactory.newClassifiedData(dataClassesOf(secondGroup), secondGroup)
            );
        } catch (DataException e) {
            throw new CrossValidationSampleException(
                    "Failed to divide the validation sample into two sets with ratio", e);
        }
    }

    /**
     * Формирование корректных тестирующей и обучающей выборки, имеющих минимальный размер, на основе заданного
     * множества объектов
     */
    private Pair<Set<ClassifiedObject>, Set<ClassifiedObject>> composeSmallestValidGroups(
            Map<DataClass, LinkedList<ClassifiedObject>> sampleItemsByClasses) {

        Set<ClassifiedObject> firstGroup = new HashSet<>();
        Set<ClassifiedObject> secondGroup = new HashSet<>();

        Map<DataClass, LinkedList<ClassifiedObject>> smallestGroups = extractSmallestGroups(sampleItemsByClasses);
        boolean putNextObjectToFirstGroup = true;

        for (DataClass clazz : smallestGroups.keySet()) {
            for (ClassifiedObject obj : smallestGroups.get(clazz)) {
                if (putNextObjectToFirstGroup) {
                    firstGroup.add(obj);
                } else {
                    secondGroup.add(obj);
                }

                putNextObjectToFirstGroup = !putNextObjectToFirstGroup;
            }
        }

        return new Pair<>(firstGroup, secondGroup);
    }

    /**
     * Из заданного множества объектов выделить минимально допустимую часть, корректную для формирования тестирующей и
     * обучающей выборок
     */
    private Map<DataClass, LinkedList<ClassifiedObject>> extractSmallestGroups(
            Map<DataClass, LinkedList<ClassifiedObject>> sampleItemsByClasses) {

        Map<DataClass, LinkedList<ClassifiedObject>> smallestGroups = new HashMap<>();
        Iterator<DataClass> classIt = sampleItemsByClasses.keySet().iterator();
        int i = 0;

        while (i < CrossValidatorParameterFactory.SAMPLE_SIZE_MIN) {
            Iterator<ClassifiedObject> objIt = sampleItemsByClasses.get(classIt.next()).iterator();
            int j = 0;

            while (j < UnclassifiedData.MIN_NUMBER_OF_CLASSES) {
                if (objIt.hasNext()) {
                    ClassifiedObject obj = objIt.next();
                    if (!smallestGroups.containsKey(obj.dataClass())) {
                        smallestGroups.put(obj.dataClass(), new LinkedList<>());
                    }

                    smallestGroups.get(obj.dataClass()).add(obj);
                    i++;
                    j++;
                } else {
                    assert classIt.hasNext();
                    objIt = sampleItemsByClasses.get(classIt.next()).iterator();
                }
            }
        }

        return smallestGroups;
    }

    /**
     * Формирование набора классов, которым принадлежат объект из заданного множества
     */
    private Set<? extends DataClass> dataClassesOf(Set<? extends ClassifiedObject> objects) {
        Set<DataClass> classes = new HashSet<>();

        for (ClassifiedObject obj : objects) {
            if (!classes.contains(obj.dataClass())) {
                classes.add(obj.dataClass());
            }
        }

        return classes;
    }

    //-------------------------------------------------------------------------

    @Override
    public PairwiseTrainingSet generateTrainingSet(ClassifiedData source, int ratio, int maxJudgementGroupSize,
                                                   BiFunction<Set<? extends ClassifiedObject>,
                                                           Set<? extends ClassifiedObject>,
                                                           Integer> expertFunction)
            throws CrossValidationSampleException {

        Objects.requireNonNull(source);
        checkPreciseIntervalJudgementsCountRatio(ratio);
        checkMaxJudgementPartSize(maxJudgementGroupSize);

        Pair<Map<DataClass, LinkedList<ClassifiedObject>>, Map<DataClass, LinkedList<ClassifiedObject>>>
                sampleItemsByClasses = divideIntoPreciseAndIntervalJudgements(source, ratio);
        PairwiseTrainingSet intervalJudgements = composeSetOfIntervalJudgements(sampleItemsByClasses.first,
                UnclassifiedData.MIN_NUMBER_OF_CLASSES, maxJudgementGroupSize, expertFunction);
        PairwiseTrainingSet preciseJudgements = composeSetOfPreciseJudgements(sampleItemsByClasses.second, expertFunction);

        return combinePairwiseTrainingSets(intervalJudgements, preciseJudgements);
    }

    /**
     * Проверка того, что процентное соотношение между интервальными и точными экспертными оценками имеет допустимое значение
     */
    private void checkPreciseIntervalJudgementsCountRatio(int ratio) {
        if (ratio < CrossValidatorParameterFactory.PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MIN) {
            throw new IllegalArgumentException(
                    String.format("Ratio of the precise/interval judgements count (%d) cannot have a value lesser " +
                                    "than the minimum (%d)", ratio,
                            CrossValidatorParameterFactory.PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MIN));
        }

        if (CrossValidatorParameterFactory.PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MAX - ratio < 0) {
            throw new IllegalArgumentException(
                    String.format("Ratio of the precise/interval judgements count (%d) cannot have a value greater " +
                                    "than the maximum (%d)", ratio,
                            CrossValidatorParameterFactory.PRECISE_INTERVAL_JUDGEMENTS_COUNT_RATIO_MAX));
        }
    }

    /**
     * Проверка того, что максимально допустимый размер одной из двух частей интервальной экспертной оценки имеет
     * минимально допустимое значение
     */
    private void checkMaxJudgementPartSize(int size) {
        if (size < UnclassifiedData.MIN_NUMBER_OF_CLASSES) {
            throw new IllegalArgumentException(String.format("Maximum value of an interval judgement (%d) " +
                            "cannot have a value lesser than the minimum (%d)",
                    size, UnclassifiedData.MIN_NUMBER_OF_CLASSES));
        }
    }

    /**
     * Разделение классифицированных данных на две части: одна для формирования интервальных экспертных оценок,
     * а другая - для точных
     */
    private Pair<Map<DataClass, LinkedList<ClassifiedObject>>, Map<DataClass, LinkedList<ClassifiedObject>>>
    divideIntoPreciseAndIntervalJudgements(ClassifiedData source, int ratio) {

        int objectsCount = source.objects().size();
        assert (objectsCount % 2 == 0);

        int preciseJudgementsSetSize = (int) Math.floor(objectsCount * (100.0 - ratio) / 100.0);

        /*
         * Из нечётного количества точных экспертных оценок невозможно сформировать пары, поэтому уменьшаем количество
         * нечётных экспертных оценок
         */
        if (preciseJudgementsSetSize % 2 != 0) {
            preciseJudgementsSetSize++;
        }

        Map<DataClass, LinkedList<ClassifiedObject>> sourceByClasses = groupSampleItemsByClasses(source);
        Map<DataClass, LinkedList<ClassifiedObject>> preciseJudgementObjects = new HashMap<>();
        Map<DataClass, LinkedList<ClassifiedObject>> intervalJudgementObjects = new HashMap<>();
        int preciseJudgementObjectsCount = 0;

        /*
         * Заполняем сначала выборку для точных экспертных оценок.
         * Объекты, что остались на этом шаге, помещаем в выборку интервальных оценок.
         */
        while (preciseJudgementObjectsCount < preciseJudgementsSetSize) {
            for (ClassifiedObject obj : popFirstObjects(sourceByClasses)) {
                DataClass clazz = obj.dataClass();

                if (preciseJudgementObjectsCount < preciseJudgementsSetSize) {
                    if (!preciseJudgementObjects.containsKey(clazz)) {
                        preciseJudgementObjects.put(clazz, new LinkedList<>());
                    }

                    preciseJudgementObjects.get(clazz).add(obj);
                    preciseJudgementObjectsCount++;
                } else {
                    if (!intervalJudgementObjects.containsKey(clazz)) {
                        intervalJudgementObjects.put(clazz, new LinkedList<>());
                    }

                    intervalJudgementObjects.get(clazz).add(obj);
                }
            }
        }

        /*
         * Все оставшиеся элементы помещаем в выборку для интервальных экспертных оценок
         */
        for (Map.Entry<DataClass, LinkedList<ClassifiedObject>> entry : sourceByClasses.entrySet()) {
            if (!intervalJudgementObjects.containsKey(entry.getKey())) {
                intervalJudgementObjects.put(entry.getKey(), new LinkedList<>());
            }

            intervalJudgementObjects.get(entry.getKey()).addAll(entry.getValue());
        }

        return new Pair<>(intervalJudgementObjects, preciseJudgementObjects);
    }

    /**
     * Возвращает первые объектов из списков по каждому классу
     */
    private List<ClassifiedObject> popFirstObjects(Map<DataClass, LinkedList<ClassifiedObject>> sample) {
        int sampleSize = sample.size();
        List<ClassifiedObject> firstObjects = new ArrayList<>(sampleSize);
        sample.forEach((clazz, items) -> firstObjects.add(items.pop()));

        for (ClassifiedObject obj : firstObjects) {
            if (sample.get(obj.dataClass()).isEmpty()) {
                sample.remove(obj.dataClass());
            }
        }

        return firstObjects;
    }

    /**
     * Формирование множества точных экспертных оценок на базе заданной выборки с чётным количеством объектов
     */
    private PairwiseTrainingSet composeSetOfPreciseJudgements(Map<DataClass, LinkedList<ClassifiedObject>> sample,
                                                              BiFunction<Set<? extends ClassifiedObject>,
                                                                      Set<? extends ClassifiedObject>,
                                                                      Integer> expertFunction)
            throws CrossValidationSampleException {

        return composeSetOfIntervalJudgements(sample, 1, 1, expertFunction); // каждая оценка связана ровно с двумя объектами
    }

    /**
     * Формирование множества интервальных экспертных оценок на базе заданной выборки с чётным количеством объектов.
     */
    private PairwiseTrainingSet composeSetOfIntervalJudgements(Map<DataClass, LinkedList<ClassifiedObject>> sample,
                                                               int minJudgementGroupSize, int maxJudgementGroupSize,
                                                               BiFunction<Set<? extends ClassifiedObject>,
                                                                       Set<? extends ClassifiedObject>,
                                                                       Integer> expertFunction)
            throws CrossValidationSampleException {

        Set<PairwiseTrainingObject> trainingSetItems = new HashSet<>();

        // проверка для точных экспертных оценок
        if (maxJudgementGroupSize == 1) {
            assert (sample.size() % 2 == 0);
        }

        while (!sample.isEmpty()) {
            assert (sample.size() != 1);

            List<DataClass> classes = listOfMapKeys(sample);
            List<Integer> indexes = UniformedRandom.nextIntegerSequence(0, classes.size() - 1);
            assert (indexes.size() >= 2);

            int firstIndex = indexes.get(0);
            int secondIndex = indexes.get(1);

            int minGroupSize = minJudgementGroupSize;

            /*
             * Возможна ситуация, когда при формировании групп для экспертной оценки в исходной выборке остаётся
             * лишь группы объектов двух классов, причём количество элементов в каждой из них не больше максимально
             * допустимого.
             * В этом случае необходимо добавить все элементы групп объектов в группы для экспертной оценки.
             * Иначе может возникнуть ситуация, когда в одной группе будут отобраны все объекты, а в другой - нет,
             * и нельзя будет при следующей итерации сформировать новую экспертную оценку.
             */
            if (sample.size() == 2
                    && sample.get(classes.get(0)).size() <= maxJudgementGroupSize
                    && sample.get(classes.get(1)).size() <= maxJudgementGroupSize) {
                minGroupSize = maxJudgementGroupSize;
            }

            trainingSetItems.add(newPairwiseTrainingSetItem(
                    grabJudgementGroup(sample.get(classes.get(firstIndex)), minGroupSize, maxJudgementGroupSize),
                    grabJudgementGroup(sample.get(classes.get(secondIndex)), minGroupSize, maxJudgementGroupSize),
                    expertFunction
            ));

            Stream.of(firstIndex, secondIndex).forEach(i -> {
                if (sample.get(classes.get(i)).isEmpty()) {
                    sample.remove(classes.get(i));
                }
            });
        }

        return dataFactory.newPairwiseTrainingSet(trainingSetItems);
    }

    private <K, V> List<K> listOfMapKeys(Map<K, V> map) {
        List<K> list = new ArrayList<>(map.size());
        map.keySet().forEach(list::add);
        return list;
    }

    /**
     * Формирование группы элементов для экспертной оценки со случайным количеством элементов из заданного диапазона
     * на основе списка объектов.
     * Объекты сформированной группы удаляются из исходного списка объектов
     */
    private Set<? extends ClassifiedObject> grabJudgementGroup(LinkedList<ClassifiedObject> items,
                                                               int minJudgementGroupSize, int maxJudgementGroupSize) {
        assert !items.isEmpty();

        Set<ClassifiedObject> judgementGroup = new HashSet<>();
        int size = UniformedRandom.nextInteger(minJudgementGroupSize, maxJudgementGroupSize);
        assert size > 0;

        if (items.size() < size) {
            size = items.size();
        }

        for (int i = 0; i < size; i++) {
            judgementGroup.add(items.pop());
        }

        return judgementGroup;
    }

    /**
     * Создание экспертной оценки на основе двух групп объектов, имеющих два различных класса
     */
    private PairwiseTrainingObject newPairwiseTrainingSetItem(Set<? extends ClassifiedObject> firstGroup,
                                                              Set<? extends ClassifiedObject> secondGroup,
                                                              BiFunction<Set<? extends ClassifiedObject>,
                                                                      Set<? extends ClassifiedObject>,
                                                                      Integer> expertFunction)
            throws CrossValidationSampleException {

        Set<? extends ClassifiedObject> preferable;
        Set<? extends ClassifiedObject> inferior;

        if (1 == expertFunction.apply(firstGroup, secondGroup)) {
            preferable = firstGroup;
            inferior = secondGroup;
        } else {
            preferable = secondGroup;
            inferior = firstGroup;
        }

        return dataFactory.newPairwiseTrainingObject(preferable, inferior);
    }

    /**
     * Объединенение нескольких обучающих выборок
     */
    private PairwiseTrainingSet combinePairwiseTrainingSets(PairwiseTrainingSet... sets) {
        Set<PairwiseTrainingObject> objects = new HashSet<>();

        for (PairwiseTrainingSet set : sets) {
            objects.addAll(set.objects());
        }

        return dataFactory.newPairwiseTrainingSet(objects);
    }
}
