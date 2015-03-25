package com.github.plokhotnyuk.scala_vs_java

import org.scalacheck.Gen._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

class SumOfArithmeticSeriesSpec extends WordSpec with PropertyChecks with Matchers {
  "JavaSumOfArithmeticSeries" should {
    "calculate sum by loop" in checkJavaSumOfArithmeticSeries(_.loop())
    "calculate sum by recursion" in checkJavaSumOfArithmeticSeries(_.formula())
  }

  "ScalaSumOfArithmeticSeries" should {
    "calculate sum by loop" in checkScalaSumOfArithmeticSeries(_.loop())
    "calculate sum by formula" in checkScalaSumOfArithmeticSeries(_.formula())
  }

  private def checkJavaSumOfArithmeticSeries(f: JavaSumOfArithmeticSeries => Long): Unit =
    forAll(choose(1, 10000))((i: Int) => f(new JavaSumOfArithmeticSeries { n = i }) should be (sumOfSeries(i)))

  private def checkScalaSumOfArithmeticSeries(f: ScalaSumOfArithmeticSeries => Long): Unit =
    forAll(choose(1, 10000))((i: Int) => f(new ScalaSumOfArithmeticSeries { n = i }) should be (sumOfSeries(i)))

  val sumOfSeries: Stream[Long] = 0L #:: sumOfSeries.zip(Stream.from(1)).map { case (s, i) => s + i }
}
