package com.github.plokhotnyuk.scala_vs_java

import java.math.BigInteger

import org.scalacheck.Gen._
import org.scalacheck.Prop
import org.scalacheck.Prop._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class FibonacciSpec extends Specification with ScalaCheck {
  "JavaFibonacci" should {
    "calculate fibonacci by loop" in checkJavaSumOfArithmeticSeries(_.loop())
    "calculate fibonacci by doubling loop" in checkJavaSumOfArithmeticSeries(_.doublingLoop())
  }

  "ScalaFibonacci" should {
    "calculate fibonacci by loop" in checkScalaSumOfArithmeticSeries(_.loop())
    "calculate fibonacci by doubling loop" in checkScalaSumOfArithmeticSeries(_.doublingLoop())
  }

  def checkJavaSumOfArithmeticSeries(f: JavaFibonacci => BigInteger): Prop =
    forAll(choose(1, 10000))(i => f(new JavaFibonacci { n = i }) must_== fibonacci(i).bigInteger)

  def checkScalaSumOfArithmeticSeries(f: ScalaFibonacci => BigInt): Prop =
    forAll(choose(1, 10000))(i => f(new ScalaFibonacci { n = i }) must_== fibonacci(i))

  val fibonacci: Stream[BigInt] = 0 #:: 1 #:: fibonacci.zip(fibonacci.tail).map { case (a, b) => a + b }
}
