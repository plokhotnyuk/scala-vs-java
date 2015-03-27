package com.github.plokhotnyuk.scala_vs_java

import org.scalacheck.Gen._
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.prop.PropertyChecks

class SumOfArithmeticSeriesSpec extends WordSpec with PropertyChecks with Matchers {
  "JavaSumOfArithmeticSeries" should {
    "calculate sum by loop" in checkJavaSumOfArithmeticSeries(_.loop())
    "calculate sum by recursion" in checkJavaSumOfArithmeticSeries(_.formula())
  }

  "ScalaSumOfArithmeticSeries" should {
    "calculate sum by loop" in checkScalaSumOfArithmeticSeries(_.loop())
    "calculate sum by formula" in checkScalaSumOfArithmeticSeries(_.formula())
  }

  def checkJavaSumOfArithmeticSeries(f: JavaSumOfArithmeticSeries => Long): Unit =
    forAll(choose(1, 10000))(i => f(new JavaSumOfArithmeticSeries { n = i }) should be (sumOfSerie(i)))

  def checkScalaSumOfArithmeticSeries(f: ScalaSumOfArithmeticSeries => Long): Unit =
    forAll(choose(1, 10000))(i => f(new ScalaSumOfArithmeticSeries { n = i }) should be (sumOfSerie(i)))

  val sumOfSerie: Stream[Long] = 0 #:: sumOfSerie.zip(Stream.from(1)).map { case (s, i) => s + i }
}
