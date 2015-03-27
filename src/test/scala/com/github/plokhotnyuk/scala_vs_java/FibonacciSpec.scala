package com.github.plokhotnyuk.scala_vs_java

import java.math.BigInteger

import org.scalacheck.Gen._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

class FibonacciSpec extends WordSpec with PropertyChecks with Matchers {
  "JavaFibonacci" should {
    "calculate fibonacci by loop" in checkJavaSumOfArithmeticSeries(_.loop())
    "calculate fibonacci by doubling loop" in checkJavaSumOfArithmeticSeries(_.doublingLoop())
  }

  "ScalaFibonacci" should {
    "calculate fibonacci by loop" in checkScalaSumOfArithmeticSeries(_.loop())
    "calculate fibonacci by doubling loop" in checkScalaSumOfArithmeticSeries(_.doublingLoop())
  }

  def checkJavaSumOfArithmeticSeries(f: JavaFibonacci => BigInteger): Unit =
    forAll(choose(1, 10000))(i => f(new JavaFibonacci { n = i }) should be (fibonacci(i).bigInteger))

  def checkScalaSumOfArithmeticSeries(f: ScalaFibonacci => BigInt): Unit =
    forAll(choose(1, 10000))(i => f(new ScalaFibonacci { n = i }) should be (fibonacci(i)))

  val fibonacci: Stream[BigInt] = 0 #:: 1 #:: fibonacci.zip(fibonacci.tail).map { case (a, b) => a + b }
}
