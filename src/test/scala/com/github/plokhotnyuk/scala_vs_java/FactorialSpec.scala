package com.github.plokhotnyuk.scala_vs_java

import java.math.BigInteger

import org.scalacheck.Gen._
import org.scalacheck.Prop
import org.scalacheck.Prop._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class FactorialSpec extends Specification with ScalaCheck {
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

  def checkJavaFactorial(f: JavaFactorial => BigInteger): Prop =
    forAll(choose(1, 1000))(i => f(new JavaFactorial { n = i }) must_== factorial(i).bigInteger)

  def checkScalaFactorial(f: ScalaFactorial => BigInt): Prop =
    forAll(choose(1, 1000))(i => f(new ScalaFactorial { n = i }) must_== factorial(i))

  val factorial: Stream[BigInt] = 1 #:: factorial.zip(Stream.from(1)).map { case (p, i) => p * i }
}