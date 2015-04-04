package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.util.Set;

/**
 * Средство проведения кросс-валидации ранжирующего попарного классификатора с заданными параметрами
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier
 */
public interface RankingPairwiseClassifierCrossValidator<R extends Report> {

    /**
     * Кросс-валидация заданного ранжирующего попарного классификатора с заданными параметрами кросс-валидации
     *
     * @param classifier      попарный классификатор
     * @param validatorParams множество параметров кросс-валидации
     * @return отчёт, содержащий результаты кросс-валидации указанного классификатора
     * @throws CrossValidationException в случае ошибки кросс-валидации
     * @throws NullPointerException     если хотя бы один из параметров не задан
     */
    R validate(RankingPairwiseClassifier classifier, Set<? extends CrossValidatorParameter<?>> validatorParams)
            throws CrossValidationException;
}
