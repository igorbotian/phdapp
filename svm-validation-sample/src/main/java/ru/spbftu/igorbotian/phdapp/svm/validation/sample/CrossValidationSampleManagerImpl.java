package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;

import java.util.*;
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
    public CrossValidationSampleManagerImpl(DataFactory dataFactory, MathDataFactory mathDataFactory) {
        this.dataFactory = Objects.requireNonNull(dataFactory);
        sampleGenerator = new CrossValidationSampleGeneratorImpl(dataFactory, Objects.requireNonNull(mathDataFactory));
    }

    @Override
    public CrossValidationSampleGenerator sampleGenerator() {
        return sampleGenerator;
    }

    //-------------------------------------------------------------------------

    @Override
    public ClassifiedData generateSample(int sampleSize) throws CrossValidationSampleException {
        checkSampleSize(sampleSize);

        int eachGroupSize = (sampleSize % 2 == 0) ? sampleSize / 2 : (sampleSize + 1) / 2;
        sampleGenerator.regeneratePoints(eachGroupSize);

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

        if(secondSetSize < UnclassifiedData.MIN_NUMBER_OF_CLASSES) {
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
       Pair<Set<ClassifiedObject>, Set<ClassifiedObject>> smallestValidSets, int firstGroupSize)
            throws CrossValidationSampleException {

        Set<ClassifiedObject> firstGroup = new HashSet<>(smallestValidSets.first);
        Set<ClassifiedObject> secondGroup = new HashSet<>(smallestValidSets.second);
        Iterator<? extends ClassifiedObject> it = sample.objects().iterator();
        int i = 0;

        // фомирование первой группы
        while(i < firstGroupSize) {
            assert (it.hasNext());
            ClassifiedObject obj = it.next();

            if(smallestValidSets.first.contains(obj)) {
                i++; // первая группа уже содержит этот объект
            } else if(!smallestValidSets.second.contains(obj)) {
                firstGroup.add(obj);
                i++;
            }
        }

        // фомирование второй группы
        while(it.hasNext()) {
            assert (it.hasNext());
            ClassifiedObject obj = it.next();

            if(!smallestValidSets.first.contains(obj)
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

        for(DataClass clazz : smallestGroups.keySet()) {
            for(ClassifiedObject obj : smallestGroups.get(clazz)) {
                if(putNextObjectToFirstGroup) {
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

        while(i < CrossValidatorParameterFactory.SAMPLE_SIZE_MIN) {
            Iterator<ClassifiedObject> objIt = sampleItemsByClasses.get(classIt.next()).iterator();
            int j = 0;

            while(j < UnclassifiedData.MIN_NUMBER_OF_CLASSES) {
                if(objIt.hasNext()) {
                    ClassifiedObject obj = objIt.next();
                    if(!smallestGroups.containsKey(obj.dataClass())) {
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
}
