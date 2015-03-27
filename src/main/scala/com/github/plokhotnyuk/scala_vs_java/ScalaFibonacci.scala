package com.github.plokhotnyuk.scala_vs_java

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
class ScalaFibonacci {
  @Param(Array("10", "100", "1000", "10000"))
  var n: Int = _

  @Benchmark
  def loop(): BigInt =
    if (n > 92) loop(n - 91, 4660046610375530309L, 7540113804746346429L)
    else fastLoop(n)

  @Benchmark
  def doublingLoop(): BigInt =
    if (n > 150) doublingLoop(n, 31 - Integer.numberOfLeadingZeros(n), 0, 1)
    else if (n > 92) loop(n - 91, 4660046610375530309L, 7540113804746346429L)
    else fastLoop(n)

  @annotation.tailrec
  private def fastLoop(n: Int, a: Long = 0, b: Long = 1): Long = if (n > 1) fastLoop(n - 1, b, a + b) else b

  @annotation.tailrec
  private def loop(n: Int, a: BigInt = 0, b: BigInt = 1): BigInt = if (n > 1) loop(n - 1, b, a + b) else b

  @annotation.tailrec
  private def doublingLoop(n: Int, i: Int, a: BigInt, b: BigInt): BigInt =
    if (i >= 0) {
      val d = a * ((b << 1) - a)
      val e = a * a + b * b
      if ((n & (1 << i)) != 0) doublingLoop(n, i - 1, e, e + d) else doublingLoop(n, i - 1, d, e)
    } else a
}