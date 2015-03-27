package com.github.plokhotnyuk.scala_vs_java;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class JavaFibonacci {
    @Param({"10", "100", "1000", "10000"})
    public int n;

    @Benchmark
    public BigInteger loop() {
        return n > 92 ?
                loop(n - 91, BigInteger.valueOf(4660046610375530309L), BigInteger.valueOf(7540113804746346429L)) :
                BigInteger.valueOf(fastLoop(n, 0, 1));
    }

    @Benchmark
    public BigInteger doublingLoop() {
        if (n > 150) {
            return doublingLoop(n, BigInteger.ZERO, BigInteger.ONE);
        }
        return n > 92 ?
                loop(n - 91, BigInteger.valueOf(4660046610375530309L), BigInteger.valueOf(7540113804746346429L)) :
                BigInteger.valueOf(fastLoop(n, 0, 1));
    }

    private long fastLoop(int n, long a, long b) {
        while (n > 1) {
            long c = a + b;
            a = b;
            b = c;
            n--;
        }
        return b;
    }

    private BigInteger loop(int n, BigInteger a, BigInteger b) {
        while (n > 1) {
            BigInteger c = a.add(b);
            a = b;
            b = c;
            n--;
        }
        return b;
    }

    private BigInteger doublingLoop(final int n, BigInteger a, BigInteger b) {
        int i = 31 - Integer.numberOfLeadingZeros(n);
        while (i >= 0) {
            BigInteger d = a.multiply(b.shiftLeft(1).subtract(a));
            BigInteger e = a.multiply(a).add(b.multiply(b));
            a = d;
            b = e;
            if ((n & (1 << i)) != 0) {
                BigInteger c = a.add(b);
                a = b;
                b = c;
            }
            i--;
        }
        return a;
    }
}