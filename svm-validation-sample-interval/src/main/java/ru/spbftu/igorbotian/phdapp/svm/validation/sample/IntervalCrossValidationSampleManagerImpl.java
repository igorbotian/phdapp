package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация средства формирования выборки для кросс-валидации ранжирующего попарного классификатора, поддерживающего
 * групповые экспертные оценки
 */
@Singleton
class IntervalCrossValidationSampleManagerImpl implements IntervalCrossValidationSampleManager {

    private static final Logger LOGGER = Logger.getLogger(IntervalCrossValidationSampleManagerImpl.class);

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Средство генерации выборки для кросс-валидации классификатора
     */
    private CrossValidationSampleGenerator sampleGenerator;

    @Inject
    public IntervalCrossValidationSampleManagerImpl(DataFactory dataFactory, MathDataFactory mathDataFactory,
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

        LOGGER.debug("Generating sample of size = " + sampleSize);
        sampleGenerator.regeneratePoints(sampleSize);

        Set<DataClass> dataClasses = Stream.of(
                sampleGenerator.firstSupportingPoint().dataClass(),
                sampleGenerator.secondSupportingPoint().dataClass()
        ).collect(Collectors.toSet());
        Set<ClassifiedObject> data = new LinkedHashSet<>();
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

        LOGGER.debug(String.format("Dividing sample of size %d into two groups with ratio %d",
                sample.objects().size(), ratio));

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

        LOGGER.debug(String.format("The first group size = %s and the second group size = %d", firstSetSize, secondSetSize));
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
            assert !sampleItemsByClasses.get(clazz).isEmpty();

            if (sampleItemsByClasses.get(clazz).size() < UnclassifiedData.MIN_NUMBER_OF_CLASSES) {
                result++;
            } else {
                result += UnclassifiedData.MIN_NUMBER_OF_CLASSES;
            }
        }

        if (result < 2 /* две группы */ * UnclassifiedData.MIN_NUMBER_OF_CLASSES) {
            throw new CrossValidationSampleException("The sample is not dividable into training and testing sets: " +
                    "there should be objects of at least two classes in each set");
        }
    }

    /**
     * Наполнение корректных обучающей и тестирующей выборок минимально допустимого размер на основе
     * заданного множества объектов
     */
    private Pair<ClassifiedData, ClassifiedData> divideSampleIntoTwoGroups(ClassifiedData sample,
                                                                           Pair<Set<ClassifiedObject>,
                                                                                   Set<ClassifiedObject>> smallestValidSets,
                                                                           int firstGroupSize)
            throws CrossValidationSampleException {

        Set<ClassifiedObject> firstGroup = new LinkedHashSet<>(smallestValidSets.first);
        Set<ClassifiedObject> secondGroup = new LinkedHashSet<>(smallestValidSets.second);
        Iterator<? extends ClassifiedObject> it = sample.objects().iterator();
        int i = 0;

        // фомирование первой группы
        while (i < firstGroupSize) {
            assert it.hasNext();
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
            assert it.hasNext();
            ClassifiedObject obj = it.next();

            if (!smallestValidSets.first.contains(obj)
                    && !smallestValidSets.second.contains(obj)) {
                secondGroup.add(obj);
            }
        }

        assert !it.hasNext();

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

        Set<ClassifiedObject> firstGroup = new LinkedHashSet<>();
        Set<ClassifiedObject> secondGroup = new LinkedHashSet<>();

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
        Set<DataClass> classes = new LinkedHashSet<>();

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
        checkSourceSize(source);
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
     * Проверка, что из заданного количества объектов могут быть сформированы экспертные оценки
     */
    private void checkSourceSize(ClassifiedData source) throws CrossValidationSampleException {
        if (source.objects().size() == 1 || source.objects().size() == 3) {
            throw new CrossValidationSampleException(source.objects().size() + " objects cannot be divided to judgement groups");
        }
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
    divideIntoPreciseAndIntervalJudgements(ClassifiedData source, int ratio) throws CrossValidationSampleException {

        int objectCount = source.objects().size();
        assert objectCount >= 2;

        int preciseJudgementsSetSize = (int) Math.floor(objectCount * (100.0 - ratio) / 100.0);

        /*
         * Из нечётного количества точных экспертных оценок невозможно сформировать пары, поэтому уменьшаем количество
         * нечётных экспертных оценок
         */
        if (preciseJudgementsSetSize % 2 != 0) {
            if (preciseJudgementsSetSize < objectCount) {
                preciseJudgementsSetSize++;
            } else {
                throw new CrossValidationSampleException("Unable to compose precise judgements from odd number of items: "
                        + objectCount);
            }
        }

        if (objectCount - preciseJudgementsSetSize == 1) {
            throw new CrossValidationSampleException(String.format("Unable to divide %d items to precise/interval judgements " +
                    "with ratio %d: there is only one item left for interval judgements", objectCount, ratio));
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

        assert preciseJudgementsSetSize % 2 == 0;
        assert numberOfItems(preciseJudgementObjects) == preciseJudgementsSetSize;
        assert numberOfItems(intervalJudgementObjects) != 1;

        return new Pair<>(intervalJudgementObjects, preciseJudgementObjects);
    }

    private int numberOfItems(Map<DataClass, LinkedList<ClassifiedObject>> items) {
        int count = 0;

        for (DataClass clazz : items.keySet()) {
            count += items.get(clazz).size();
        }

        return count;
    }

    /**
     * Возвращает первые элементы из списков, составленных из объектов одного и того же класса
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

        // каждая оценка связана ровно с двумя объектами
        return composeSetOfIntervalJudgements(sample, 1, 1, expertFunction);
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

        Set<Judgement> trainingSetItems = new HashSet<>();

        // проверка для точных экспертных оценок
        if (maxJudgementGroupSize == 1) {
            assert sample.size() % 2 == 0;
        }

        while (!sample.isEmpty()) {
            Pair<Set<? extends ClassifiedObject>, Set<? extends ClassifiedObject>> pair =
                    grabPairOfJudgementGroups(sample, minJudgementGroupSize, maxJudgementGroupSize);

            trainingSetItems.add(newPairwiseTrainingSetItem(pair.first, pair.second, expertFunction));
        }

        return dataFactory.newPairwiseTrainingSet(trainingSetItems);
    }

    private <K, V> List<K> listOfMapKeys(Map<K, V> map) {
        List<K> list = new ArrayList<>(map.size());
        map.keySet().forEach(list::add);
        return list;
    }

    /**
     * Формирование пары групп элементов для экспертной оценки со случайным количеством элементов из заданного диапазона
     * на основе списка объектов.
     * Объекты сформированной группы удаляются из исходного списка объектов
     */
    private Pair<Set<? extends ClassifiedObject>, Set<? extends ClassifiedObject>> grabPairOfJudgementGroups(
            Map<DataClass, LinkedList<ClassifiedObject>> sample, int minJudgementGroupSize, int maxJudgementGroupSize) {

        assert !sample.isEmpty();

        Pair<Set<? extends ClassifiedObject>, Set<? extends ClassifiedObject>> pair;

        if (sample.size() == 1) {
            pair = grabPairOfJudgementGroupsOfSameClass(sample, minJudgementGroupSize, maxJudgementGroupSize);
        } else if (sample.size() == 2) {
            pair = grabPairOfJudgementGroupsOfTwoClasses(sample, minJudgementGroupSize, maxJudgementGroupSize);
        } else {
            pair = grabPairOfJudgementGroupsOfManyClasses(sample, minJudgementGroupSize, maxJudgementGroupSize);
        }

        assert !pair.first.isEmpty();
        assert !pair.second.isEmpty();

        Stream.of(
                pair.first.iterator().next().dataClass(),
                pair.second.iterator().next().dataClass()
        ).forEach(clazz -> {
            if (sample.containsKey(clazz) && sample.get(clazz).isEmpty()) {
                sample.remove(clazz);
            }
        });

        assert sample.size() != 1 || sample.entrySet().iterator().next().getValue().size() != 1;

        return pair;
    }

    /**
     * Формирование пары групп объектов для экспертной оценки со случайным количеством элементов из заданного диапазона
     * на основе списка объектов.
     * При условии, что в исходном множестве представлены объекты, принадлежащее лишь одному классам.
     */
    private Pair<Set<? extends ClassifiedObject>, Set<? extends ClassifiedObject>> grabPairOfJudgementGroupsOfSameClass(
            Map<DataClass, LinkedList<ClassifiedObject>> sample, int minJudgementGroupSize, int maxJudgementGroupSize) {

        assert sample.size() == 1;

        int firstGroupMinSize = minJudgementGroupSize;
        int firstGroupMaxSize = maxJudgementGroupSize;
        int secondGroupMinSize = minJudgementGroupSize;
        int secondGroupMaxSize = maxJudgementGroupSize;
        LinkedList<ClassifiedObject> objects = sample.entrySet().iterator().next().getValue();

        assert objects.size() > 1; // условие возможности формирования пары

        // по данному алгоритму допускается формирование группы объектов размером меньше допустимого (minJudgementGroupSize)
        // это неизбежно

        if (objects.size() <= maxJudgementGroupSize) {
            // исключить ситуацию, когда все объекты уйдут в первую группу, а для второй ничего не останется
            if (objects.size() > minJudgementGroupSize) {
                firstGroupMaxSize = minJudgementGroupSize;
                firstGroupMinSize = firstGroupMaxSize;
                secondGroupMaxSize = objects.size() - firstGroupMaxSize;
                secondGroupMinSize = secondGroupMaxSize;
            } else {
                firstGroupMaxSize = minJudgementGroupSize - 1;
                firstGroupMinSize = firstGroupMaxSize;

                secondGroupMaxSize = objects.size() - firstGroupMaxSize;
                secondGroupMinSize = secondGroupMaxSize;
            }
        } else if (objects.size() <= 2 * maxJudgementGroupSize) {
            // ещё один способ исключить ситуацию выше
            firstGroupMinSize = firstGroupMaxSize;
            secondGroupMinSize = secondGroupMaxSize;
        } else if (objects.size() == 2 * maxJudgementGroupSize + 1) {
            // исключить ситуацию, когда после данной итерации останется лишь один объект

            assert maxJudgementGroupSize > 1;
            // мы из трёх объектов никак не сможем сформировать пары из одиночных объектов
            // проверка на это реализована раньше (на чётность общего количества объектов)

            secondGroupMaxSize--;

            if (secondGroupMinSize > secondGroupMaxSize) {
                secondGroupMinSize = secondGroupMaxSize;
            }
        }

        Pair<Set<? extends ClassifiedObject>, Set<? extends ClassifiedObject>> pair = new Pair<>(
                grabJudgementGroup(objects, firstGroupMinSize, firstGroupMaxSize),
                grabJudgementGroup(objects, secondGroupMinSize, secondGroupMaxSize)
        );

        assert objects.size() != 1;

        return pair;
    }

    /**
     * Формирование пары групп объектов для экспертной оценки со случайным количеством элементов из заданного диапазона
     * на основе списка объектов.
     * При условии, что в исходном множестве представлены объекты, принадлежащие лишь двум классам.
     */
    private Pair<Set<? extends ClassifiedObject>, Set<? extends ClassifiedObject>> grabPairOfJudgementGroupsOfTwoClasses(
            Map<DataClass, LinkedList<ClassifiedObject>> sample, int minJudgementGroupSize, int maxJudgementGroupSize) {

        assert sample.size() == 2;

        List<DataClass> classes = listOfMapKeys(sample);
        LinkedList<ClassifiedObject> firstClassObjects = sample.get(classes.get(0));
        LinkedList<ClassifiedObject> secondClassObjects = sample.get(classes.get(1));
        int firstGroupMinSize = minJudgementGroupSize;
        int firstGroupMaxSize = maxJudgementGroupSize;
        int secondGroupMinSize = minJudgementGroupSize;
        int secondGroupMaxSize = maxJudgementGroupSize;

        if (firstClassObjects.size() <= maxJudgementGroupSize
                && secondClassObjects.size() <= maxJudgementGroupSize) {
            firstGroupMinSize = firstGroupMaxSize;
            secondGroupMinSize = secondGroupMaxSize;
        } else if (firstClassObjects.size() == maxJudgementGroupSize + 1
                && secondClassObjects.size() <= maxJudgementGroupSize) {

            assert firstGroupMaxSize > 1;
            // мы из трёх объектов никак не сможем сформировать пары из одиночных объектов
            // проверка на это реализована раньше (на чётность общего количества объектов)

            firstGroupMaxSize--;
        } else if (secondClassObjects.size() == maxJudgementGroupSize + 1
                && firstClassObjects.size() <= maxJudgementGroupSize) {

            assert secondGroupMaxSize > 1;
            // мы из трёх объектов никак не сможем сформировать пары из одиночных объектов
            // проверка на это реализована раньше (на чётность общего количества объектов)

            secondGroupMaxSize--;
        }

        return new Pair<>(
                grabJudgementGroup(firstClassObjects, firstGroupMinSize, firstGroupMaxSize),
                grabJudgementGroup(secondClassObjects, secondGroupMinSize, secondGroupMaxSize)
        );
    }

    /**
     * Формирование пары групп объектов для экспертной оценки со случайным количеством элементов из заданного диапазона
     * на основе списка объектов.
     * При условии, что в исходном множестве представлены объекты, принадлежащие больше чем двум классам.
     * (Два класса, потому что у нас пара групп объектов, которые должны иметь разные классы).
     */
    private Pair<Set<? extends ClassifiedObject>, Set<? extends ClassifiedObject>> grabPairOfJudgementGroupsOfManyClasses(
            Map<DataClass, LinkedList<ClassifiedObject>> sample, int minJudgementGroupSize, int maxJudgementGroupSize) {

        assert sample.size() > 2;

        List<DataClass> classes = listOfMapKeys(sample);
        List<Integer> indexes = UniformedRandom.nextIntegerSequence(0, classes.size() - 1);
        assert indexes.size() >= 2;

        return new Pair<>(
                grabJudgementGroup(sample.get(classes.get(indexes.get(0))), minJudgementGroupSize, maxJudgementGroupSize),
                grabJudgementGroup(sample.get(classes.get(indexes.get(1))), minJudgementGroupSize, maxJudgementGroupSize)
        );
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
    private Judgement newPairwiseTrainingSetItem(Set<? extends ClassifiedObject> firstGroup,
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

        return dataFactory.newJudgement(preferable, inferior);
    }

    /**
     * Объединенение нескольких обучающих выборок
     */
    private PairwiseTrainingSet combinePairwiseTrainingSets(PairwiseTrainingSet... sets) {
        Set<Judgement> objects = new LinkedHashSet<>();

        for (PairwiseTrainingSet set : sets) {
            objects.addAll(set.judgements());
        }

        return dataFactory.newPairwiseTrainingSet(objects);
    }
}
