package ru.spbftu.igorbotian.phdapp.ui.common;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationProgressListener;
import ru.spbftu.igorbotian.phdapp.svm.validation.PairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация модели окна, отображающего прогресс выполнения кросс-валидации классификатора с заданными параметрами
 */
class CrossValidationProgressWindowDirectorImpl implements CrossValidationProgressWindowDirector {

    private static final Logger LOGGER = Logger.getLogger(CrossValidationProgressWindowDirectorImpl.class);

    private final UIHelper uiHelper;
    private final PairwiseClassifier classifier;
    private final Set<CrossValidationProgressListener> listeners = new CopyOnWriteArraySet<>();

    @Inject
    public CrossValidationProgressWindowDirectorImpl(UIHelper uiHelper, PairwiseClassifier classifier) {
        this.uiHelper = Objects.requireNonNull(uiHelper);
        this.classifier = Objects.requireNonNull(classifier);
    }

    @Override
    public void addProgressListener(CrossValidationProgressListener listener) {
        listeners.add(listener);
    }

    @Override
    public void validate() {
        PairwiseClassifierCrossValidator<?> validator = uiHelper.mainFrameDirector().selectedCrossValidator();

        if (validator == null) {
            throw new IllegalStateException("Pairwise classifier cross-validator is expected to be initialized");
        }

        try {
            final Report report = validator.validate(classifier, Stream.of(
                    uiHelper.crossValidatorParamsFrameDirector().penaltyParameter(),
                    uiHelper.crossValidatorParamsFrameDirector().gaussianKernelParameter(),
                    uiHelper.crossValidatorParamsFrameDirector().samplesToGenerateCount(),
                    uiHelper.crossValidatorParamsFrameDirector().sampleSize(),
                    uiHelper.crossValidatorParamsFrameDirector().trainingTestingSetsSizeRatio(),
                    uiHelper.crossValidatorParamsFrameDirector().preciseIntervalJudgmentsCountRatio()
            ).collect(Collectors.toSet()));

            listeners.forEach(listener -> listener.crossValidationCompleted(report));
        } catch (CrossValidationException e) {
            LOGGER.error("An error occurred during cross-validation", e);
            listeners.forEach(listeners -> listeners.crossValidationFailed(e));
        }
    }
}
