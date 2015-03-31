package com.github.plokhotnyuk.scala_vs_java

import org.scalacheck.Gen._
import org.scalacheck.Prop
import org.scalacheck.Prop._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class SumOfArithmeticSeriesSpec extends Specification with ScalaCheck {
  "JavaSumOfArithmeticSeries" should {
    "calculate sum by loop" in checkJavaSumOfArithmeticSeries(_.loop())
    "calculate sum by recursion" in checkJavaSumOfArithmeticSeries(_.formula())
  }

  "ScalaSumOfArithmeticSeries" should {
    "calculate sum by loop" in checkScalaSumOfArithmeticSeries(_.loop())
    "calculate sum by formula" in checkScalaSumOfArithmeticSeries(_.formula())
  }

  def checkJavaSumOfArithmeticSeries(f: JavaSumOfArithmeticSeries => Long): Prop =
    forAll(choose(1, 10000))(i => f(new JavaSumOfArithmeticSeries { n = i }) must_== sumOfSerie(i))

  def checkScalaSumOfArithmeticSeries(f: ScalaSumOfArithmeticSeries => Long): Prop =
    forAll(choose(1, 10000))(i => f(new ScalaSumOfArithmeticSeries { n = i }) must_== sumOfSerie(i))

  val sumOfSerie: Stream[Long] = 0 #:: sumOfSerie.zip(Stream.from(1)).map { case (s, i) => s + i }
}
