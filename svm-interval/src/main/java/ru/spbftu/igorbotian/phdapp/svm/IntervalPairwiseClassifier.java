package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.*;

/**
 * Попарный классификатор, поддерживающий интервальные экспертные оценки
 */
@Singleton
class IntervalPairwiseClassifier implements PairwiseClassifier {

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Решающая функция, построенная на основе результатов обучения классификатора с помощью заданной обучающей выбороки
     */
    private DecisionFunction<UnclassifiedObject> decisionFunction;

    /**
     * Средство решения задачи квадратичного программирования, применяемое в ходе построения решающей функции
     * для заданной обучающей выборки
     */
    private QuadraticProgrammingSolver<UnclassifiedObject> qpSolver;

    /**
     * Конструктор объекта
     *
     * @param dataFactory фабрика объектов предметной области
     * @throws java.lang.NullPointerException если параметр не задан
     */
    @Inject
    public IntervalPairwiseClassifier(DataFactory dataFactory, QuadraticProgrammingSolver<UnclassifiedObject> qpSolver) {
        this.dataFactory = Objects.requireNonNull(dataFactory);
        this.qpSolver = Objects.requireNonNull(qpSolver);
    }

    @Override
    public void train(PairwiseTrainingSet trainingSet, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        Objects.requireNonNull(trainingSet);
        Objects.requireNonNull(params);

        double sigma = getParameter(IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_ID, params);
        double penalty = getParameter(IntervalClassifierParameterFactory.PENALTY_PARAM_ID, params);
        decisionFunction = buildDecisionFunction(trainingSet, sigma, penalty);
    }

    private DecisionFunction<UnclassifiedObject> buildDecisionFunction(PairwiseTrainingSet trainingSet,
                                                                       double sigma, double penalty)
            throws ClassifierTrainingException {

        assert trainingSet != null;

        try {
            KernelFunction<UnclassifiedObject> kernelFunction = new GaussianKernelFunction(sigma);
            Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> lagrangianMultipliers
                    = qpSolver.solve(trainingSet, kernelFunction, penalty);

            return new DecisionFunction<>(lagrangianMultipliers, kernelFunction);
        } catch (QuadraticProgrammingException e) {
            throw new ClassifierTrainingException("Can't build a decision function for a given training set", e);
        }
    }

    private double getParameter(String paramId, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        assert paramId != null;

        for(ClassifierParameter param : params) {
            if(paramId.equals(param.name())) {
                if(!Double.class.equals(param.valueClass())) {
                    throw new ClassifierTrainingException("Parameter " + paramId + " should be of double type");
                }

                return (Double) param.value();
            }
        }

        throw new ClassifierTrainingException(paramId + " parameter should be set");
    }

    @Override
    public ClassifiedData classify(UnclassifiedData input, Set<? extends ClassifierParameter<?>> params)
            throws ClassificationException {

        Objects.requireNonNull(input);
        Objects.requireNonNull(params);

        throw new UnsupportedOperationException("Classifier is not implemented yet");
    }
}
