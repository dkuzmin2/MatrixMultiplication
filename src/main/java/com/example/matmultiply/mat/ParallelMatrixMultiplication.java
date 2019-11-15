package com.example.matmultiply.mat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Uninterruptibles.awaitUninterruptibly;

/**
 * A matrix multiplication which is run in parallel with the given executor.
 *
 * <p>The calculations are divided between threads row-by-row. This gives an overhead when the
 * matrix dimensions are small, but with the matrix size of several hundreds should already give a
 * visible performance boost.
 */
public final class ParallelMatrixMultiplication extends MatrixMultiplication {

    private final ExecutorService executor;

    public ParallelMatrixMultiplication(ExecutorService executor) {
        this.executor = checkNotNull(executor);
    }

    @Override
    protected Matrix multiply(Matrix mat1, Matrix mat2) {
        int size = mat1.size();
        Matrix result = new Matrix(size);
        CountDownLatch latch = new CountDownLatch(size);
        for (int row = 0; row < size; ++row) {
            int rowIndex = row;
            executor.submit(() -> {
                calculateSingleRow(rowIndex, result, size, mat1, mat2);
                latch.countDown();
            });
        }
        awaitUninterruptibly(latch);
        return result;
    }
}
