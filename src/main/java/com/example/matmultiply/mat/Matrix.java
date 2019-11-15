package com.example.matmultiply.mat;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.System.lineSeparator;

/**
 * A matrix which contains {@code boolean} data.
 *
 * <p>The matrix data is a single array which is accessed in a two-dimensional fashion. See
 * {@link #valueAt(int, int)}.
 *
 * <p>Currently, all matrices are considered "square" (i.e. row number = column number). It thus
 * is defined by a single {@link #size} param.
 */
public final class Matrix {

    /**
     * The maximum allowed size for matrices.
     */
    private static final int MAX_SIZE = 10_000;

    /**
     * The number of rows/columns in the matrix.
     */
    private final int size;

    /**
     * The matrix data mapped as one-dimensional array.
     */
    private final boolean[] data;

    /**
     * Creates a matrix of the given size.
     */
    public Matrix(int size) {
        checkSize(size);
        this.size = size;
        this.data = new boolean[size * size];
    }

    /**
     * Creates a matrix of the given size filled with random data.
     */
    public static Matrix withRandomData(int size) {
        Matrix matrix = new Matrix(size);
        fillWithRandomData(matrix);
        return matrix;
    }

    /**
     * Sets the given cell in the matrix.
     */
    public void set(int row, int col, boolean value) {
        data[row * size + col] = value;
    }

    /**
     * Obtains the value of the given cell in the matrix.
     */
    public boolean valueAt(int row, int col) {
        boolean result = data[row * size + col];
        return result;
    }

    /**
     * Obtains the value of the given cell in the matrix as {@code int}.
     */
    public int valueAsInt(int row, int col) {
        int result = valueAt(row, col) ? 1 : 0;
        return result;
    }

    /**
     * Checks if the matrix is of equal size with {@code other}.
     */
    public boolean isEqualSize(Matrix other) {
        return size == other.size;
    }

    public int size() {
        return size;
    }

    /**
     * Checks that the given number is a valid matrix size.
     */
    private static void checkSize(int size) {
        checkArgument(size > 0,
                "The matrix size must be a positive number, got `%s` instead.", size);
        checkArgument(size <= MAX_SIZE,
                "The allowed max size for matrices is `%s`, got `%s` instead.", MAX_SIZE, size);
    }

    @SuppressWarnings("UnsecureRandomNumberGeneration") // True randomness not that important here.
    private static void fillWithRandomData(Matrix matrix) {
        Random random = new Random();
        for (int i = 0; i < matrix.data.length; ++i) {
            matrix.data[i] = random.nextBoolean();
        }
    }

    @SuppressWarnings("MethodWithMultipleLoops") // To conveniently iterate the matrix.
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(data.length);
        for (int row = 0; row < size; ++row) {
            result.append(lineSeparator());
            for (int col = 0; col < size; ++col) {
                result.append(data[row * size + col] ? 1 : 0);
                result.append(' ');
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        return size == matrix.size &&
                Arrays.equals(data, matrix.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
