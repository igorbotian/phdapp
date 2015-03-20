package ru.spbftu.igorbotian.phdapp.quadprog;

import jdk.nashorn.api.scripting.JSObject;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Реализация метода решения задачи квадратичного программирования, предложенного Гольдфарбом и Иднани.
 * Используется реализация R-библиотеки 'quadprog' на языке JavaScript.
 *
 * @see ActiveDualSetAlgorithm
 * @see <a href="https://github.com/albertosantini/node-quadprog">https://github.com/albertosantini/node-quadprog</a>
 * @author Igor Botian <igor.botian@gmail.com>
 */
class JSQuadProgSolver implements ActiveDualSetAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(JSQuadProgSolver.class);

    /**
     * В пакете quadprog неправильно реализована проверка на то, является ли матрица квадратичной функции положительно
     * определённой или нет.
     * Чтобы обойти данную ошибку, необходимо к диагонали матрицы прибавить малое число, не влияющее на результат
     */
    private static final double FIX = 0.000000000001;

    /**
     * Идентификатор JavaScript-движка
     */
    private static final String JAVASCRIPT_ENGINE_NAME = "nashorn";

    /**
     * Название скрипта, реализующего библиотеку 'quadprog' и находящегося в ресурсах приложения
     */
    private static final String QUADPROG_SCRIPT = "quadprog.js";

    /* Названия переменных JavaScript-функции solveQP */
    private static final String OBJECTIVE_FUNCTION_MATRIX = "Dmat";
    private static final String OBJECTIVE_VECTOR = "dvec";
    private static final String CONSTRAINT_MATRIX = "Amat";
    private static final String CONSTRAINT_VECTOR = "bvec";

    /**
     * Средство вычисления JavaScript-выражений
     */
    private final ScriptEngine jsEngine;

    public JSQuadProgSolver() {
        ScriptEngineManager manager = new ScriptEngineManager();
        jsEngine = manager.getEngineByName(JAVASCRIPT_ENGINE_NAME);

        if (jsEngine == null) {
            String errorMessage = "Failed to initialize JavaScript engine. No engine identified by '"
                    + JAVASCRIPT_ENGINE_NAME + "' found";
            LOGGER.fatal(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        try {
            jsEngine.eval(new InputStreamReader(
                    JSQuadProgSolver.class.getResourceAsStream(QUADPROG_SCRIPT)
            ));

            init(OBJECTIVE_FUNCTION_MATRIX, "[]");
            init(OBJECTIVE_VECTOR, "[]");
            init(CONSTRAINT_MATRIX, "[]");
            init(CONSTRAINT_VECTOR, "[]");
        } catch (ScriptException e) {
            String errorMessage = "Failed to initialize 'quadprog' JavaScript package";
            LOGGER.fatal(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public double[] apply(double[][] objectiveFunctionMatrix, double[] objectiveFunctionVector,
                          double[][] constraintMatrix, double[] constraintVector) throws Exception {

        Objects.requireNonNull(objectiveFunctionMatrix);
        Objects.requireNonNull(objectiveFunctionVector);
        Objects.requireNonNull(constraintMatrix);
        Objects.requireNonNull(constraintVector);

        try {
            setObjectiveFunctionMatrix(objectiveFunctionMatrix);
            setObjectiveFunctionVector(objectiveFunctionVector);
            setConstraintMatrix(constraintMatrix);
            setConstraintVector(constraintVector);

            return jsNumberArrayToJavaDoubleArray(solveQP());
        } catch (ScriptException e) {
            throw new Exception("Unable to solve the quadratic programming problem. " +
                    "An error occurred on executing JavaScript code", e);
        }
    }

    /**
     * Конвертация JavaScript-объекта, представляющего из себя массив чисел, в Java-массив вещественных чисел
     */
    private double[] jsNumberArrayToJavaDoubleArray(JSObject jsArray) {
        Collection<Object> jsSolutionItems = jsArray.values();
        double[] result = new double[jsSolutionItems.size()];
        int i = 0;

        for (Object item : jsSolutionItems) {
            result[i] = Double.parseDouble(item.toString());
            i++;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("QP solution = " + Arrays.toString(result));
        }

        return result;
    }

    /**
     * Решение задачи квадратичного программирования на JavaScript
     */
    private JSObject solveQP() throws ScriptException {
        String args = String.join(",", OBJECTIVE_FUNCTION_MATRIX, OBJECTIVE_VECTOR,
                CONSTRAINT_MATRIX, CONSTRAINT_VECTOR);
        return (JSObject) eval("solveQP(" + args + ").solution");
    }

    /**
     * Объявление JavaScript-переменной с указанным значением (необязательным)
     */
    private void init(String var, String value) throws ScriptException {
        String script = "var " + var;

        if (value != null) {
            script += " = " + value;
        }

        eval(script);
    }

    /**
     * Задание матрицы целевой функции.
     * Данная операция дополнительно включает в себя обход ошибки определения того,
     * является ли матрица положительно определённой или нет
     */
    private void setObjectiveFunctionMatrix(double[][] values) throws ScriptException {
        assert values != null;
        assert values.length > 0;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Objection function matrix = " + Arrays.toString(values));
        }

        eval(OBJECTIVE_FUNCTION_MATRIX + " = []");

        for (int i = 0; i < values[0].length; i++) {
            // solveQP требует, чтобы индексы матриц и векторов начинались с 1
            eval(OBJECTIVE_FUNCTION_MATRIX + "[" + (i + 1) + "] = []");
        }

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                // solveQP требует, чтобы индексы матриц и векторов начинались с 1
                double value = values[i][j];

                if(i == j) {
                    value += FIX;
                }

                eval(OBJECTIVE_FUNCTION_MATRIX + "[" + (i + 1) + "][" + (j + 1) + "] = " + value);
            }
        }
    }

    /**
     * Задание вектора целевой функции
     */
    private void setObjectiveFunctionVector(double[] values) throws ScriptException {
        assert values != null;
        assert values.length > 0;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Objection function vector = " + Arrays.toString(values));
        }

        eval(OBJECTIVE_VECTOR + " = []");

        for (int i = 0; i < values.length; i++) {
            // solveQP требует, чтобы индексы матриц и векторов начинались с 1
            eval(OBJECTIVE_VECTOR + "[" + (i + 1) + "] = " + values[i]);
        }
    }

    /**
     * Задание матрицы ограничений.
     * Метод solveQP из пакета quadprog требует, чтобы данная матрица была представлена в транспонированном виде
     */
    private void setConstraintMatrix(double[][] values) throws ScriptException {
        assert values != null;
        assert values.length > 0;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Constraint vector = " + Arrays.toString(values));
        }

        eval(CONSTRAINT_MATRIX + " = []");

        for (int i = 0; i < values[0].length; i++) {
            // solveQP требует, чтобы индексы матриц и векторов начинались с 1
            eval(CONSTRAINT_MATRIX + "[" + (i + 1) + "] = []");
        }

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                // solveQP требует, чтобы индексы матриц и векторов начинались с 1
                eval(CONSTRAINT_MATRIX + "[" + (j + 1) + "][" + (i + 1) + "] = " + values[i][j]);
            }
        }
    }

    /**
     * Задание вектора ограничений
     */
    private void setConstraintVector(double[] values) throws ScriptException {
        assert values != null;
        assert values.length > 0;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Constraint matrix = " + Arrays.toString(values));
        }

        eval(CONSTRAINT_VECTOR + " = []");

        for (int i = 0; i < values.length; i++) {
            // solveQP требует, чтобы индексы матриц и векторов начинались с 1
            eval(CONSTRAINT_VECTOR + "[" + (i + 1) + "] = " + values[i]);
        }
    }

    private Object eval(String script) throws ScriptException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(script);
        }
        System.out.println(script);
        return jsEngine.eval(script);
    }
}
