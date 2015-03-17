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
 * Используется реализация R-библиотеки 'quadprog' на языке JavaScript.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ActiveDualSetAlgorithm
 * @see <a href="https://github.com/albertosantini/node-quadprog">https://github.com/albertosantini/node-quadprog</a>
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
    private static final String D_MATRIX = "Dmatrix";
    private static final String D_VECTOR = "Dvector";
    private static final String A_MATRIX = "Amatrix";
    private static final String B_VECTOR = "Bvector";

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
            setMatrix(D_MATRIX, fix(matrix));
            setVector(D_VECTOR, vector);
            setMatrix(A_MATRIX, transpose(constraintMatrix));
            setVector(B_VECTOR, constraintVector);
            return jsNumberArrayToJavaDoubleArray(solveQP());
        } catch (ScriptException e) {
            throw new Exception("Unable to solve the quadratic programming problem. " +
                    "An error occurred on executing JavaScript code", e);
        }
    }

    /**
     * Обход ошибки определения того, является ли матрица положительно определённой или нет
     */
    private double[][] fix(double[][] matrix) {
        double[][] fixed = new double[matrix.length][matrix[0].length];

        for(int i = 0; i < fixed.length; i++) {
            for(int j = 0; j < fixed[i].length; j++) {
                fixed[i][j] = matrix[i][j];

                if(i == j) {
                    fixed[i][j] += FIX;
                }
            }
        }

        return fixed;
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
        return (JSObject) eval("solveQP(" + args + ").solution");
    }

    /**
     * Объявление JavaScript-переменной с указанным значением (необязательным)
     */
    private void register(String var, String value) throws ScriptException {
        String script = "var " + var;

        if (value != null) {
            script += " = " + value;
        }

        eval(script);
    }

    /**
     * Заполнение JavaScript-вектора указанными значениями
     */
    private void setVector(String name, double[] values) throws ScriptException {
        eval(name + " = []");

        for (int i = 0; i < values.length; i++) {
            // solveQP requires arrays with indices starting at 1
            eval(name + "[" + (i + 1) + "] = " + values[i]);
        }
    }

    /**
     * Задание JavaScript-матрицы указанными значениями.
     * В JavaScript матрица будет представлена в транспонированном виде, как того требует реализация quadprog
     * на JavaScript
     */
    private void setMatrix(String name, double[][] values) throws ScriptException {
        eval(name + " = []");

        for (int i = 0; i < values.length; i++) {
            // solveQP requires arrays with indices starting at 1
            eval(name + "[" + (i + 1) + "] = []");
        }

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                // solveQP requires arrays with indices starting at 1
                eval(name + "[" + (i + 1) + "][" + (j + 1) + "] = " + values[i][j]);
            }
        }
    }

    private Object eval(String script) throws ScriptException {
        System.out.println(script);
        return jsEngine.eval(script);
    }

    /**
     * Транспонирование матрицы
     */
    private double[][] transpose(double[][] matrix) {
        double[][] transposed = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }
}
