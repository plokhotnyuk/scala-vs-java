package com.github.plokhotnyuk.scala_vs_java;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class JavaFactorial {
    @Param({"10", "100", "1000", "10000"})
    public int n;

    private static ForkJoinPool pool = new ForkJoinPool();

    @Benchmark
    public BigInteger loop() {
        return n > 20 ? loop(1, n) : BigInteger.valueOf(fastLoop(1, n));
    }

    @Benchmark
    public BigInteger recursion() {
        return n > 20 ? recursion(1, n) : BigInteger.valueOf(fastLoop(1, n));
    }

    @Benchmark
    public BigInteger recursionPar() {
        return n > 20 ? recursePar(1, n) : BigInteger.valueOf(fastLoop(1, n));
    }

    private long fastLoop(final int n1, int n2) {
        long p = n1;
        while (n2 > n1) {
            p = p * n2;
            n2--;
        }
        return p;
    }

    private BigInteger loop(final int n1, int n2) {
        BigInteger p = BigInteger.valueOf(n1);
        while (n2 > n1) {
            p = p.multiply(BigInteger.valueOf(n2));
            n2--;
        }
        return p;
    }

    private BigInteger recursion(final long n1, final long n2) {
        long d = n2 - n1;
        if (d == 0) return BigInteger.valueOf(n1);
        else if (d == 1) return BigInteger.valueOf(n1 * n2);
        else if (d == 2) return BigInteger.valueOf(n1 * (n1 + 1)).multiply(BigInteger.valueOf(n2));
        else if (d == 3) return BigInteger.valueOf(n1 * (n1 + 1)).multiply(BigInteger.valueOf((n2 - 1) * n2));
        return recursion(n1, n1 + (d >> 1)).multiply(recursion(n1 + (d >> 1) + 1, n2));
    }

    private BigInteger recursePar(final long n1, final long n2) {
        final long d = n2 - n1;
        if (d < 333) return recursion(n1, n2);
        RecursiveTask<BigInteger> t = new RecursiveTask<BigInteger>() {
            protected BigInteger compute() {
                return recursePar(n1 + (d >> 1) + 1, n2);
            }
        };
        if (ForkJoinTask.getPool() == pool) {
            t.fork();
        } else {
            pool.execute(t);
        }
        return recursePar(n1, n1 + (d >> 1)).multiply(t.join());
    }
}