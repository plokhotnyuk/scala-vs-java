import sbt._
import sbt.Keys._

object Build extends Build {
  val jreVersion = sys.props("java.runtime.version").take(3)
  val `scala-vs-java` = Project("scala-vs-java", file(".")).settings(
    organization := "com.github.plokhotnyuk",
    description := "Scala vs. Java benchmarks",
    scalaVersion := "2.11.6",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.1" % "test",
      "org.scalacheck" %% "scalacheck" % "1.12.2" % "test"
    ),
    javacOptions := Seq(
      "-target", jreVersion,
      "-Xlint"
    ),
    scalacOptions := Seq(
      "-target:jvm-" + jreVersion,
      "-optimize",
      "-deprecation",
      "-unchecked",
      "-feature",
      "-language:implicitConversions",
      "-Xlog-reflective-calls",
      "-Xfuture",
      "-Xlint"
    ),
    javaOptions := Seq(
      "-server",
      "-Xms1024m",
      "-Xmx1024m",
      "-XX:NewSize=896m",
      "-Xss256k",
      "-XX:+UseG1GC",
      "-XX:+TieredCompilation",
      "-XX:+UseNUMA",
      "-XX:-UseBiasedLocking",
      "-XX:+AlwaysPreTouch"
    )
  ).settings(pl.project13.scala.sbt.SbtJmh.jmhSettings: _*)
}