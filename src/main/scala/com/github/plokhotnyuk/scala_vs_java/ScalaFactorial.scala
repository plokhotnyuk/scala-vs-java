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
  def loop(): BigInt = if (n > 20) loop(1, n, 1) else fastLoop(1, n, 1)

  @Benchmark
  def recursion(): BigInt = if (n > 20) recursion(1, n) else fastLoop(1, n, 1)

  @Benchmark
  def recursionPar(): BigInt = if (n > 20) recursionPar(1, n) else fastLoop(1, n, 1)

  @tailrec
  private def fastLoop(n1: Int, n2: Int, p: Long): Long = if (n2 > n1) fastLoop(n1, n2 - 1, p * n2) else p

  @tailrec
  private def loop(n1: Int, n2: Int, p: BigInt): BigInt = if (n2 > n1) loop(n1, n2 - 1, p * n2) else p

  private def recursion(n1: Long, n2: Long): BigInt = n2 - n1 match {
    case 0 => BigInt(n1)
    case 1 => BigInt(n1 * n2)
    case 2 => BigInt(n1 * (n1 + 1)) * BigInt(n2)
    case 3 => BigInt(n1 * (n1 + 1)) * BigInt((n2 - 1) * n2)
    case d => recursion(n1, n1 + (d >> 1)) * recursion(n1 + (d >> 1) + 1, n2)
  }

  private def recursionPar(n1: Long, n2: Long): BigInt = {
    val d = n2 - n1
    if (d < 333) recursion(n1, n2)
    else {
      val f = Future(recursionPar(n1 + (d >> 1) + 1, n2))
      recursionPar(n1, n1 + (d >> 1)) * Await.result(f, Duration(1, TimeUnit.MINUTES))
    }
  }
}