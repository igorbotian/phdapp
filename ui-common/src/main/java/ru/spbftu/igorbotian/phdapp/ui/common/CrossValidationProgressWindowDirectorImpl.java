package ru.spbftu.igorbotian.phdapp.ui.common;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.AsyncRankingPairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationProgressListener;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация модели окна, отображающего прогресс выполнения кросс-валидации классификатора с заданными параметрами
 */
class CrossValidationProgressWindowDirectorImpl implements CrossValidationProgressWindowDirector {

    private final UIHelper uiHelper;
    private final RankingPairwiseClassifier classifier;

    @Inject
    public CrossValidationProgressWindowDirectorImpl(UIHelper uiHelper, RankingPairwiseClassifier classifier) {
        this.uiHelper = Objects.requireNonNull(uiHelper);
        this.classifier = Objects.requireNonNull(classifier);
    }

    @Override
    public void addProgressListener(CrossValidationProgressListener listener) {
        selectedValidator().addProgressListener(listener);
    }

    @Override
    public void removeProgressListener(CrossValidationProgressListener listener) {
        selectedValidator().removeProgressListener(listener);
    }

    @Override
    public void validate() {
        selectedValidator().validateAsync(classifier, Stream.of(
                uiHelper.crossValidatorParamsFrameDirector().penaltyParameter(),
                uiHelper.crossValidatorParamsFrameDirector().gaussianKernelParameter(),
                uiHelper.crossValidatorParamsFrameDirector().samplesToGenerateCount(),
                uiHelper.crossValidatorParamsFrameDirector().sampleSize(),
                uiHelper.crossValidatorParamsFrameDirector().trainingTestingSetsSizeRatio(),
                uiHelper.crossValidatorParamsFrameDirector().preciseIntervalJudgmentsCountRatio()
        ).collect(Collectors.toSet()));
    }

    private AsyncRankingPairwiseClassifierCrossValidator<? extends Report> selectedValidator() {
        AsyncRankingPairwiseClassifierCrossValidator<? extends Report> validator
                = uiHelper.mainFrameDirector().selectedCrossValidator();

        if (validator == null) {
            throw new IllegalStateException("Pairwise classifier cross-validator is expected to be initialized");
        }

        return validator;
    }

    @Override
    public void cancelValidation() throws CrossValidationException {
        selectedValidator().interrupt();
    }
}
