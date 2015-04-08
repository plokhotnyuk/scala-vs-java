package com.github.plokhotnyuk.scala_vs_java

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
class ScalaSumOfArithmeticSeries {
  @Param(Array("10", "100", "1000", "10000"))
  var n: Int = _

  @Benchmark
  def formula(): Long = ((n + 1L) * n) >> 1

  @Benchmark
  def loop(): Long = {
    @annotation.tailrec
    def loop(n: Int, s: Long): Long = if (n > 0) loop(n - 1, s + n) else s

    loop(n, 0)
  }
}