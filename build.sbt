

scalaVersion := "2.9.0"

name := "dsl-examples"

version := "0.1"

scalaVersion := "2.9.0"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "1.6.1"
    , "org.scalaz" % "scalaz-core_2.9.0-1" % "6.0.1"
)

//testListeners += customTestListener
testListeners <<= target.map(t => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath)))

