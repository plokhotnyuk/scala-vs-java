package com.github.plokhotnyuk.scala_vs_java;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class JavaSumOfArithmeticSeries {
    @Param({"10", "100", "1000", "10000"})
    public int n;

    @Benchmark
    public int formula() {
        return ((n + 1) * n) >> 1;
    }

    @Benchmark
    public int loop() {
        int i = n;
        int s = 0;
        while (i > 0) {
            s += i;
            i--;
        }
        return s;
    }
}