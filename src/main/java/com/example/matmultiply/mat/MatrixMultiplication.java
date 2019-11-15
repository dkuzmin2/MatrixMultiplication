package com.example.matmultiply.mat;

import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A multiplication of two equal size matrices.
 *
 * <p>As matrices contain {@code boolean} data, all the data in the resulting matrix is calculated
 * with the modulo {@code 2}.
 */
abstract class MatrixMultiplication implements BiFunction<Matrix, Matrix, Matrix> {

    @Override
    public Matrix apply(Matrix mat1, Matrix mat2) {
        checkEqualSize(mat1, mat2);
        Matrix result = multiply(mat1, mat2);
        return result;
    }

    /**
     * Does the matrix multiplication.
     *
     * @see #calculateSingleRow(int, Matrix, int, Matrix, Matrix) for calculations that may be
     *      common for all descendants
     */
    abstract Matrix multiply(Matrix mat1, Matrix mat2);

    /**
     * Calculates a single row in the {@code result} matrix applying the rules of matrix multiplication.
     */
    void calculateSingleRow(int rowIndex, Matrix result, int matSize, Matrix mat1, Matrix mat2) {
        for (int col = 0; col < matSize; ++col) {
            boolean cellValue = cellValue(rowIndex, col, mat1, mat2);
            result.set(rowIndex, col, cellValue);
        }
    }

    /**
     * Obtains the value of a single cell in the result matrix.
     */
    private static boolean cellValue(int row, int col, Matrix mat1, Matrix mat2) {
        int cell = 0;
        for (int i = 0; i < mat1.size(); ++i) {
            cell += mat1.valueAsInt(row, i) * mat2.valueAsInt(i, col);
        }
        boolean result = cell % 2 > 0;
        return result;
    }

    private static void checkEqualSize(Matrix mat1, Matrix mat2) {
        checkArgument(mat1.isEqualSize(mat2),
                "Can only multiply matrices of the same size, got sizes `%s`, `%s` instead.",
                mat1.size(), mat2.size());
    }
}
