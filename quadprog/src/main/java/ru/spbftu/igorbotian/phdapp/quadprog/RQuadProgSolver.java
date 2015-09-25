package ru.spbftu.igorbotian.phdapp.quadprog;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import rcaller.RCaller;
import rcaller.RCode;
import rcaller.exception.ExecutionException;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Реализация решения задачи квадратичного программирования средствами R-пакета 'quadprog'
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="http://cran.r-project.org/web/packages/quadprog/index.html">http://cran.r-project.org/web/packages/quadprog/index.html</a>
 */
class RQuadProgSolver implements ActiveDualSetAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(RQuadProgSolver.class);

    /**
     * Количество знаков после запятой после округления значений
     */
    private static final int PRECISION = 10;

    /**
     * Малое вещественное значение, близкое к нулю
     */
    private static final double TINY_VALUE = 0.000000001;

    /**
     * Название параметра, который содержит путь к среде выполнения R-скриптов
     */
    private static final String RSCRIPT_PATH_PARAM = "RScript";

    /**
     * Путь к среде выполнения R-скриптов по умолчанию
     */
    private static final String UNIX_RSCRIPT_PATH = "/usr/bin/RScript";

    // переменные, которые используются в генерируемом R-скрипте
    private static final String DMAT_VAR = "Dmat";
    private static final String DVEC_VAR = "dvec";
    private static final String AMAT_VAR = "Amat";
    private static final String BVEC_VAR = "bvec";
    private static final String RESULT_VAR = "result";
    private static final String SOLUTION_VAR = "solution";

    /**
     * Путь к исполняемому файлу среды R
     */
    private final Path rScriptExecutablePath;

    @Inject
    public RQuadProgSolver(ApplicationConfiguration config) {
        rScriptExecutablePath = Paths.get(Objects.requireNonNull(config).getString(RSCRIPT_PATH_PARAM, UNIX_RSCRIPT_PATH));

        if (!Files.exists(rScriptExecutablePath)) {
            throw new IllegalStateException("RScript executable is not set");
        }
    }

    @Override
    public double[] apply(double[][] matrix, double[] vector, double[][] constraintMatrix, double[] constraintVector)
            throws QuadraticProgrammingException {

        roundValues(matrix, PRECISION);
        fixPositiveDefinition(matrix);

        /* Генерация R-скрипта на основе заданных параметров */

        RCode code = new RCode();

        code.addRCode("require(quadprog)");
        code.addDoubleMatrix(DMAT_VAR, matrix);
        code.addDoubleArray(DVEC_VAR, vector);
        code.addDoubleMatrix(AMAT_VAR, constraintMatrix);
        code.addDoubleArray(BVEC_VAR, constraintVector);
        code.addRCode(String.format("%s <- solve.QP(%s, %s, t(%s), %s)", RESULT_VAR, DMAT_VAR, DVEC_VAR, AMAT_VAR, BVEC_VAR));

        try {
            RCaller rCaller = new RCaller();
            rCaller.setRscriptExecutable(rScriptExecutablePath.toAbsolutePath().toString());
            rCaller.setRCode(code);
            LOGGER.debug("Executing generated R script to solve quadratic programming problem");
            rCaller.runAndReturnResult(RESULT_VAR);
            LOGGER.debug("Parsing results of the R script executed");
            return rCaller.getParser().getAsDoubleArray(SOLUTION_VAR);
        } catch (ExecutionException e) {
            throw new QuadraticProgrammingException("Unable to solve a quadratic programming problem", e);
        }
    }

    private void roundValues(double[][] matrix, int mantissa) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(i != j) {
                    matrix[i][j] = round(matrix[i][j], mantissa);
                }
            }
        }
    }

    private double round(double value, int mantissa) {
        double scale = Math.pow(10.0, mantissa);
        return Math.round(value * scale) / scale;
    }

    private void fixPositiveDefinition(double[][] matrix) {
        // В реализации quadprog содержится ошибка определения, является ли матрица квадратичной функции
        // положительно определённой или нет.
        // Добавление малого значения к элементам диагонали позволяет обойти эту проблему.
        fixDiagonal(matrix);
    }

    private void fixDiagonal(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            if (i < matrix[i].length) {
                matrix[i][i] += TINY_VALUE;
            }
        }
    }
}
