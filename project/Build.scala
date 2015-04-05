import sbt._
import sbt.Keys._

object Build extends Build {
  val jreVersion = sys.props("java.runtime.version").take(3)
  val `scala-vs-java` = Project("scala-vs-java", file(".")).settings(
    organization := "com.github.plokhotnyuk",
    description := "Scala vs. Java benchmarks",
    scalaVersion := "2.11.6",
    resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-scalacheck" % "3.3" % "test"
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
      "-Xlint",
      "-Yrangepos"
    ),
    javaOptions := Seq(
      "-server",
      "-Xms1024m",
      "-Xmx1024m",
      "-XX:NewSize=896m",
      "-XX:MaxNewSize=896m",
      "-Xss256k",
      "-XX:+UseG1GC",
      "-XX:+TieredCompilation",
      "-XX:+UseNUMA",
      "-XX:-UseBiasedLocking",
      "-XX:+AlwaysPreTouch"
    )
  ).settings(pl.project13.scala.sbt.SbtJmh.jmhSettings: _*)
}