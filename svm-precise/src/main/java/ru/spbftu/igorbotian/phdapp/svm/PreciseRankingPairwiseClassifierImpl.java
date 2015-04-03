package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.common.Judgement;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;
import java.util.Set;

/**
 * Реализация ранжирующего попарного классификатора, поддерживающего только точные экспертные оценки (негрупповые)
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class PreciseRankingPairwiseClassifierImpl implements PreciseRankingPairwiseClassifier {

    /**
     * Ранжирующий попарный классификатор, поддерживающий групповые экспертные оценки
     */
    private final IntervalRankingPairwiseClassifier classifier;

    @Inject
    public PreciseRankingPairwiseClassifierImpl(IntervalRankingPairwiseClassifier classifier) {
        this.classifier = Objects.requireNonNull(classifier);
    }

    @Override
    public void train(PairwiseTrainingSet trainingSet, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        checkAllJudgementsPrecise(trainingSet);
        classifier.train(trainingSet, params);
    }

    private void checkAllJudgementsPrecise(PairwiseTrainingSet trainingSet) throws ClassifierTrainingException {
        assert trainingSet != null;

        for (Judgement judgement : trainingSet.judgements()) {
            if (judgement.preferable().size() > 1 || judgement.inferior().size() > 1) {
                throw new ClassifierTrainingException(
                        String.format("Judgements should be precise: preferable = %s, inferior = %s",
                                judgement.preferable(), judgement.inferior()));
            }
        }
    }

    @Override
    public boolean classify(UnclassifiedObject first, UnclassifiedObject second,
                            Set<? extends ClassifierParameter<?>> params) throws ClassificationException {
        // TODO
        return classifier.classify(first, second, params);
    }
}
