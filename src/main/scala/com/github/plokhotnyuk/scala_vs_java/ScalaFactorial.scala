package com.github.plokhotnyuk.scala_vs_java

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
class ScalaFactorial {
  @Param(Array("10", "100", "1000", "10000"))
  var n: Int = _

  @Benchmark
  def loop(): BigInt = if (n > 20) loop(1, n) else fastLoop(1, n)

  @Benchmark
  def recursion(): BigInt = if (n > 20) recursion(1, n) else fastLoop(1, n)

  @Benchmark
  def recursionPar(): BigInt = if (n > 20) recursionPar(1, n) else fastLoop(1, n)

  @Benchmark
  def split(): BigInt = if (n > 180) split(n) else if (n > 20) recursion(1, n) else fastLoop(1, n)

  @annotation.tailrec
  private def fastLoop(n1: Int, n2: Int, p: Long = 1): Long = if (n2 > n1) fastLoop(n1, n2 - 1, p * n2) else p

  private def loop(n1: Int, n2: Int): BigInt = {
    @annotation.tailrec
    def loop(n1: Int, n2: Int, l: Long, pp: Long, p: BigInt): BigInt =
      if (n1 <= n2) {
        if (pp < l) loop(n1 + 1, n2, l, pp * n1, p)
        else loop(n1 + 1, n2, l, n1, p * pp)
      } else p * pp

    loop(n1, n2, Long.MaxValue >> (32 - Integer.numberOfLeadingZeros(n2)), 1, 1)
  }

  private def recursion(n1: Int, n2: Int): BigInt =
    if (n2 - n1 < 65) loop(n1, n2)
    else {
      val nm = (n1 + n2) >> 1
      recursion(nm + 1, n2) * recursion(n1, nm)
    }

  private def recursionPar(n1: Int, n2: Int): BigInt =
    if (n2 - n1 < 700) recursion(n1, n2)
    else {
      val nm = (n1 + n2) >> 1
      val f = Future(recursionPar(nm + 1, n2))
      recursionPar(n1, nm) * Await.result(f, Duration.Inf)
    }

  private def split(n: Int): BigInt = {
    @annotation.tailrec
    def loop2(n1: Int, n2: Int, l: Long, pp: Long, p: BigInt): BigInt =
      if (n1 <= n2) {
        if (pp < l) loop2(n1 + 2, n2, l, pp * n1, p)
        else loop2(n1 + 2, n2, l, n1, p * pp)
      } else p * pp

    def recursion2(n1: Int, n2: Int): BigInt =
      if (n2 - n1 < 65) loop2(n1, n2, Long.MaxValue >> (32 - Integer.numberOfLeadingZeros(n2)), 1, 1)
      else {
        val nm = ((n1 + n2) >> 2) << 1
        recursion2(nm + 1, n2) * recursion2(n1, nm - 1)
      }

    @annotation.tailrec
    def split(n: Int, i: Int, s: Int, h: Int, o: Int, p: BigInt, r: BigInt): BigInt =
      if (i >= 0) {
        val h1 = n >> i
        val o1 = h1 - 1 + (h1 & 1)
        val p1 = if (o < o1) p * recursion2(o + 2, o1) else p
        val r1 = if (o < o1) r * p1 else r
        split(n, i - 1, s + h, h1, o1, p1, r1)
      } else r << s

    split(n, 31 - Integer.numberOfLeadingZeros(n), 0, 0, 1, 1, 1)
  }
}