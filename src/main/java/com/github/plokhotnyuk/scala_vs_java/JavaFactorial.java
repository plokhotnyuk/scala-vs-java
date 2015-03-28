package com.github.plokhotnyuk.scala_vs_java;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.concurrent.*;

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

    private BigInteger loop(int n1, final int n2) {
        long l = Long.MAX_VALUE >> (32 - Integer.numberOfLeadingZeros(n2));
        BigInteger p = BigInteger.ONE;
        long pp = 1;
        while (n1 <= n2) {
            if (pp <= l) {
                pp *= n1;
            } else {
                p = p.multiply(BigInteger.valueOf(pp));
                pp = n1;
            }
            n1++;
        }
        return p.multiply(BigInteger.valueOf(pp));
    }

    private BigInteger recursion(final int n1, final int n2) {
        if (n2 - n1 < 65) {
            return loop(n1, n2);
        }
        final int nm = (n1 + n2) >> 1;
        return recursion(nm + 1, n2).multiply(recursion(n1, nm));
    }

    private BigInteger recursePar(final int n1, final int n2) {
        if (n2 - n1 < 700) {
            return recursion(n1, n2);
        }
        final int nm = (n1 + n2) >> 1;
        RecursiveTask<BigInteger> t = new RecursiveTask<BigInteger>() {
            protected BigInteger compute() {
                return recursePar(nm + 1, n2);
            }
        };
        if (ForkJoinTask.getPool() == pool) {
            t.fork();
        } else {
            pool.execute(t);
        }
        return recursePar(n1, nm).multiply(t.join());
    }
}