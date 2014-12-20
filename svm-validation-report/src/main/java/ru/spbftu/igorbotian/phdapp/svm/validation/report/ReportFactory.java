package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;

import java.util.List;
import java.util.Set;

/**
 * Фабрика различных отчётов по работе классификатора
 */
public interface ReportFactory {

    /**
     * Создание отчёта, содержащего различные метрики по работе классификатора с заданными параметрами
     *
     * @param classifierParams     множество значений параметров классификатора, использовавшихся в процессе кросс-валидации
     * @param crossValidatorParams множество значений параметров кросс-валидатора, при которых происходила кросс-валидация
     * @param accuracy             метрика <code>Accuracy</code> (значение в пределах [0.0;1.0])
     * @param precision            метрика <code>Precision</code> (значение в пределах [0.0;1.0])
     * @param recall               метрика <code>Recall</code> (значение в пределах [0.0;1.0])
     * @return отчёт заданного типа
     * @throws java.lang.IllegalArgumentException если значение хотя бы одного параметра выходит за пределы допустимых значений
     */
    SingleClassificationReport newSingleClassificationReport(Set<? extends ClassifierParameter<?>> classifierParams,
                                                             Set<? extends CrossValidatorParameter<?>> crossValidatorParams,
                                                             final float accuracy,
                                                             final float precision,
                                                             final float recall);

    /**
     * Создание сводного отчёта по работе классификатора на основе ряда одиночных классификаций
     *
     * @param classifications список из отчётов по работе одиночных классификаций
     * @return отчёт заданного типа
     * @throws java.lang.NullPointerException     если список отчётов не задан
     * @throws java.lang.IllegalArgumentException если список отчётов пустой
     */
    MultiClassificationReport newMultiClassificationReport(List<SingleClassificationReport> classifications);
}
