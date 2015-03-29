package com.github.plokhotnyuk.scala_vs_java

import java.math.BigInteger

import org.scalacheck.Gen._
import org.scalatest.{WordSpec, Matchers}
import org.scalatest.prop.PropertyChecks

class FactorialSpec extends WordSpec with PropertyChecks with Matchers {
  "JavaFactorial" should {
    "calculate factorial by loop" in checkJavaFactorial(_.loop())
    "calculate factorial by recursion" in checkJavaFactorial(_.recursion())
    "calculate factorial by recursionPar" in checkJavaFactorial(_.recursionPar())
    "calculate factorial by split" in checkJavaFactorial(_.split())
  }

  "ScalaFactorial" should {
    "calculate factorial by loop" in checkScalaFactorial(_.loop())
    "calculate factorial by recursion" in checkScalaFactorial(_.recursion())
    "calculate factorial by recursionPar" in checkScalaFactorial(_.recursionPar())
    "calculate factorial by split" in checkScalaFactorial(_.split())
  }

  def checkJavaFactorial(f: JavaFactorial => BigInteger): Unit =
    forAll(choose(1, 1000))(i => f(new JavaFactorial { n = i }) should be (factorial(i).bigInteger))

  def checkScalaFactorial(f: ScalaFactorial => BigInt): Unit =
    forAll(choose(1, 1000))(i => f(new ScalaFactorial { n = i }) should be (factorial(i)))

  val factorial: Stream[BigInt] = 1 #:: factorial.zip(Stream.from(1)).map { case (p, i) => p * i }
}