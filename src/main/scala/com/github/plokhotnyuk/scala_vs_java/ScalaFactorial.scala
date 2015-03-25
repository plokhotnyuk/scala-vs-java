package com.github.plokhotnyuk.scala_vs_java

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

import scala.annotation.tailrec
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

  @tailrec
  private def fastLoop(n1: Int, n2: Int, p: Long = 1): Long = if (n2 > n1) fastLoop(n1, n2 - 1, p * n2) else p

  @tailrec
  private def loop(n1: Int, n2: Int, p: BigInt = BigInt(1), pp: Long = 1): BigInt = {
    if (n1 <= n2) {
      if (pp < Int.MaxValue) loop(n1 + 1, n2, p, pp * n1)
      else loop(n1 + 1, n2, p * pp, n1)
    } else p * pp
  }

  private def recursion(n1: Int, n2: Int): BigInt = {
    val d = n2 - n1
    if (d < 50) loop(n1, n2)
    else recursion(n1, n1 + (d >> 1)) * recursion(n1 + (d >> 1) + 1, n2)
  }

  private def recursionPar(n1: Int, n2: Int): BigInt = {
    val d = n2 - n1
    if (d < 500) recursion(n1, n2)
    else {
      val f = Future(recursionPar(n1 + (d >> 1) + 1, n2))
      recursionPar(n1, n1 + (d >> 1)) * Await.result(f, Duration(1, TimeUnit.MINUTES))
    }
  }
}