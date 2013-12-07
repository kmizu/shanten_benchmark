organization := "com.github.kmizu"

name := "shanten-benchmark"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")

testOptions += Tests.Argument(TestFrameworks.Specs2, "console", "junitxml")

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.7" % "test"
)

initialCommands in console += {
  Iterator("com.github.kmizu.shanten_benchmark._").map("import "+).mkString("\n")
}
