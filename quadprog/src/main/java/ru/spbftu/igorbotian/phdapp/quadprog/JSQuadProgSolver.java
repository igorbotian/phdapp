package ru.spbftu.igorbotian.phdapp.quadprog;

import jdk.nashorn.api.scripting.JSObject;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Objects;

/**
 * Реализация метода решения задачи квадратичного программирования, предложенного Гольдфарбом и Иднани.
 * В качестве реализация R-библиотеки 'quadprog' на языке JavaScript.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ActiveDualSetAlgorithm
 * @see <a href="https://github.com/albertosantini/node-quadprog">https://github.com/albertosantini/node-quadprog</a>
 */
class JSQuadProgSolver implements ActiveDualSetAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(JSQuadProgSolver.class);

    /**
     * Идентификатор JavaScript-движка
     */
    private static final String JAVASCRIPT_ENGINE_NAME = "nashorn";

    /**
     * Название скрипта, реализующего библиотеку 'quadprog' и находящегося в ресурсах приложения
     */
    private static final String QUADPROG_SCRIPT = "quadprog.js";

    /* Названия переменных JavaScript-функции solveQP */
    private static final String D_MATRIX = "Dmat";
    private static final String D_VECTOR = "dvec";
    private static final String A_MATRIX = "Amat";
    private static final String B_VECTOR = "bvec";

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

            register(D_MATRIX, "[]");
            register(D_VECTOR, "[]");
            register(A_MATRIX, "[]");
            register(B_VECTOR, "[]");
        } catch (ScriptException e) {
            String errorMessage = "Failed to initialize 'quadprog' JavaScript package";
            LOGGER.fatal(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public double[] apply(double[][] matrix, double[] vector,
                          double[][] constraintMatrix, double[] constraintVector) throws Exception {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(vector);
        Objects.requireNonNull(constraintMatrix);
        Objects.requireNonNull(constraintVector);

        try {
            setMatrix(D_MATRIX, matrix);
            setVector(D_VECTOR, vector);
            setMatrix(A_MATRIX, constraintMatrix);
            setVector(B_VECTOR, constraintVector);
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

        return result;
    }

    /**
     * Решение задачи квадратичного программирования на JavaScript
     */
    private JSObject solveQP() throws ScriptException {
        String args = String.join(",", D_MATRIX, D_VECTOR, A_MATRIX, B_VECTOR);
        return (JSObject) jsEngine.eval("solveQP(" + args + ").solution");
    }

    /**
     * Объявление JavaScript-переменной с указанным значением (необязательным)
     */
    private void register(String var, String value) throws ScriptException {
        String script = "var " + var;

        if (value != null) {
            script += " = " + value;
        }

        jsEngine.eval(script);
    }

    /**
     * Заполнение JavaScript-вектора указанными значениями
     */
    private void setVector(String name, double[] values) throws ScriptException {
        jsEngine.eval(name + " = []");

        for (int i = 0; i < values.length; i++) {
            // solveQP requires arrays with indices starting at 1
            jsEngine.eval(name + "[" + (i + 1) + "] = " + values[i]);
        }
    }

    /**
     * Задание JavaScript-матрицы указанными значениями.
     * В JavaScript матрица будет представлена в транспонированном виде, как того требует реализация quadprog
     * на JavaScript
     */
    private void setMatrix(String name, double[][] values) throws ScriptException {
        jsEngine.eval(name + " = []");

        for (int i = 0; i < values[0].length; i++) {
            // solveQP requires arrays with indices starting at 1
            jsEngine.eval(name + "[" + (i + 1) + "] = []");
        }

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                // solveQP requires arrays with indices starting at 1
                jsEngine.eval(name + "[" + (j + 1) + "][" + (i + 1) + "] = " + values[i][j]);
            }
        }
    }
}
