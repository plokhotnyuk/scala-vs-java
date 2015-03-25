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
  }

  "ScalaFactorial" should {
    "calculate factorial by loop" in checkScalaFactorial(_.loop())
    "calculate factorial by recursion" in checkScalaFactorial(_.recursion())
    "calculate factorial by recursionPar" in checkScalaFactorial(_.recursionPar())
  }

  private def checkJavaFactorial(f: JavaFactorial => BigInteger): Unit =
    forAll(choose(1, 1000))((i: Int) => f(new JavaFactorial { n = i }) should be (factorials(i).bigInteger))

  private def checkScalaFactorial(f: ScalaFactorial => BigInt): Unit =
    forAll(choose(1, 1000))((i: Int) => f(new ScalaFactorial { n = i }) should be (factorials(i)))

  val factorials: Stream[BigInt] = BigInt(1) #:: factorials.zip(Stream.from(1)).map { case (p, i) => p * i }
}
