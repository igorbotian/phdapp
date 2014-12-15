package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.util.Set;

/**
 * Средство проведения кросс-валидации попарного классификатора с заданными параметрами
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier
 */
public interface PairwiseClassifierCrossValidator<R extends Report> {

    /**
     * Кросс-валидация заданного попарного классификатора с заданными параметрами кросс-валидации
     *
     * @param classifier      попарный классификатор
     * @param validatorParams множество параметров кросс-валидации
     * @return отчёт, содержащий результаты кросс-валидации указанного классификатора
     * @throws CrossValidationException в случае ошибки кросс-валидации
     * @throws NullPointerException     если хотя бы один из параметров не задан
     */
    R validate(PairwiseClassifier classifier, Set<? extends CrossValidatorParameter<?>> validatorParams)
            throws CrossValidationException;
}
