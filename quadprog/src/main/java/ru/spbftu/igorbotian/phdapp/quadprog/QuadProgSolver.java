package ru.spbftu.igorbotian.phdapp.quadprog;

/**
 * Реализация метода решения задачи квадратичного программирования, предложенного Гольдфарбом и Иднани.
 * Используется реализация R-библиотеки 'quadprog' на языке Java.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ActiveDualSetAlgorithm
 * @see QuadProg
 */
class QuadProgSolver implements ActiveDualSetAlgorithm {

    @Override
    public double[] apply(double[][] matrix,
                          double[] vector,
                          double[][] constraintMatrix,
                          double[] constraintVector) throws QuadraticProgrammingException {

        QuadProg.Solution solution = QuadProg.solveQP(
                toFortranArray(matrix),
                toFortranArray(vector),
                toFortranArray(transpose(constraintMatrix)),
                toFortranArray(constraintVector)
        );

        return toJavaArray(solution.solution);
    }

    private double[] toJavaArray(Double[] array) {
        int arrayLength = array.length;
        double[] result = new double[arrayLength > 0 ? arrayLength - 1 : 0];

        for (int i = 0; i < result.length; i++) {
            result[i] = array[i + 1];
        }

        return result;
    }

    private Double[][] toFortranArray(double[][] array) {
        Double[][] result = new Double[array.length + 1][];
        result[0] = null;

        for (int i = 0; i < array.length; i++) {
            result[i + 1] = toFortranArray(array[i]);
        }

        return result;
    }

    private Double[] toFortranArray(double[] array) {
        Double[] result = new Double[array.length + 1];
        result[0] = null;

        for (int i = 0; i < array.length; i++) {
            result[i + 1] = array[i];
        }

        return result;
    }

    private double[][] transpose(double[][] matrix) {
        if (matrix.length == 0) {
            return new double[0][0];
        }

        // предполагается, что длина всех строк одинакова
        double[][] transposed = new double[matrix[0].length][];

        for (int i = 0; i < transposed.length; i++) {
            transposed[i] = new double[matrix.length];

            for (int j = 0; j < transposed[i].length; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }

        return transposed;
    }
}
