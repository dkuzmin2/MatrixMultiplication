package com.example.matmultiply;

import com.example.matmultiply.mat.Matrix;
import com.example.matmultiply.mat.SequentialMatrixMultiplication;
import com.example.matmultiply.mat.ParallelMatrixMultiplication;
import com.google.common.base.Stopwatch;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A small demo which executes the matrix multiplication sequentially and
 * in parallel and compares the processing time of both operations.
 *
 * <p>Matrices are of {@code boolean} type and are generated randomly based on the size {@code N}
 * read from the console.
 */
final class Demo {

    /**
     * The thread count for parallel execution.
     *
     * <p>Is equal to the number of logical threads in the system.
     */
    private static final int THREAD_COUNT = getRuntime().availableProcessors();

    private Demo() {
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr") // OK for a demo.
    private static void printLine(String format, Object... args) {
        System.out.printf(format, args);
        System.out.println();
    }

    private static int readNextInt() {
        try {
            Scanner in = new Scanner(System.in);
            int result = in.nextInt();
            return result;
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException("Please enter a valid integer.", e);
        }
    }

    /**
     * Creates a simple executor based on the {@linkplain #THREAD_COUNT number of available threads}.
     */
    private static ExecutorService initExecutor() {
        ExecutorService result = newFixedThreadPool(THREAD_COUNT);
        return result;
    }

    @SuppressWarnings({"OverlyBroadCatchBlock", "ThrowCaughtLocally"}) // OK for a demo.
    public static void main(String[] args) {
        try {
            printLine("Please enter a matrix size (in range [1..10_000]):");

            // Init matrices.
            int matSize = readNextInt();
            Matrix mat1 = Matrix.withRandomData(matSize);
            Matrix mat2 = Matrix.withRandomData(matSize);

            // Run matrix multiplication sequentially.
            Stopwatch stopwatch1 = Stopwatch.createStarted();
            Matrix resultSequential = new SequentialMatrixMultiplication()
                    .apply(mat1, mat2);
            long sequentialExecutionTimeMs = stopwatch1.elapsed(MILLISECONDS);

            // Run matrix multiplication in parallel.
            ExecutorService executor = initExecutor();
            Stopwatch stopwatch2 = Stopwatch.createStarted();
            Matrix resultParallel = new ParallelMatrixMultiplication(executor)
                    .apply(mat1, mat2);
            long parallelExecutionTimeMs = stopwatch2.elapsed(MILLISECONDS);
            executor.shutdown();

            if (!resultParallel.equals(resultSequential)) {
                throw new IllegalStateException(
                        format("Results of sequential and parallel execution are not equal: %s%s vs %s%s.",
                                resultSequential, lineSeparator(), resultParallel, lineSeparator()));
            }

            if (matSize < 10) {
                // Print small matrices for convenience of result checks.
                printLine("Matrix 1: %s", mat1);
                printLine("--------------");
                printLine("Matrix 2: %s", mat2);
                printLine("--------------");
                printLine("Product: %s", resultParallel);
                printLine("--------------");
            }
            printLine("Elapsed time when executing sequentially: %d ms", sequentialExecutionTimeMs);
            printLine("Elapsed time when executing in parallel: %d ms", parallelExecutionTimeMs);

        } catch (Exception e) {
            printLine("Error: %s.", e.getMessage());
        }
    }
}
