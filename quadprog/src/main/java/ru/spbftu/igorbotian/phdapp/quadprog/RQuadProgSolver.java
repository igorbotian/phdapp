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
     * Средство запуска R-скриптов из Java
     */
    private final RCaller rCaller = new RCaller();

    @Inject
    public RQuadProgSolver(ApplicationConfiguration config) {
        Path rScriptPath = Paths.get(Objects.requireNonNull(config).getString(RSCRIPT_PATH_PARAM, UNIX_RSCRIPT_PATH));

        if (Files.exists(rScriptPath)) {
            rCaller.setRscriptExecutable(rScriptPath.toAbsolutePath().toString());
        } else {
            LOGGER.fatal("Please add path to RScript executable as a value of 'RScript' configuration parameter");
            throw new IllegalStateException("RScript executable is not set");
        }
    }

    @Override
    public double[] apply(double[][] matrix, double[] vector, double[][] constraintMatrix, double[] constraintVector)
            throws QuadraticProgrammingException {

        // В реализации quadprog содержится ошибка определения, является ли матрица квадратичной функции
        // положительно определённой или нет.
        // Добавление малого значения позволяет обойти эту проблему
        for (int i = 0; i < matrix.length; i++) {
            if (i < matrix[i].length) {
                matrix[i][i] += 0.000000001;
            }
        }

        /* Генерация R-скрипта на основе заданных параметров */

        RCode code = new RCode();

        code.addRCode("require(quadprog)");
        code.addDoubleMatrix(DMAT_VAR, matrix);
        code.addDoubleArray(DVEC_VAR, vector);
        code.addDoubleMatrix(AMAT_VAR, constraintMatrix);
        code.addDoubleArray(BVEC_VAR, constraintVector);
        code.addRCode(String.format("%s <- solve.QP(%s, %s, t(%s), %s)", RESULT_VAR, DMAT_VAR, DVEC_VAR, AMAT_VAR, BVEC_VAR));

        try {
            rCaller.setRCode(code);
            LOGGER.debug("Executing generated R script to solve quadratic programming problem");
            rCaller.runAndReturnResult(RESULT_VAR);
            LOGGER.debug("Parsing results of the R script executed");
            return rCaller.getParser().getAsDoubleArray(SOLUTION_VAR);
        } catch (ExecutionException e) {
            throw new QuadraticProgrammingException("Unable to solve a quadratic programming problem", e);
        }
    }
}
