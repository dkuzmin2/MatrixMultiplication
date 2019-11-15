package com.example.matmultiply.mat;

/**
 * Executes the matrix multiplication sequentially, i.e. in the current thread.
 */
public final class SequentialMatrixMultiplication extends MatrixMultiplication {

    @Override
    protected Matrix multiply(Matrix mat1, Matrix mat2) {
        int size = mat1.size();
        Matrix result = new Matrix(size);
        for (int row = 0; row < size; ++row) {
            calculateSingleRow(row, result, size, mat1, mat2);
        }
        return result;
    }
}
