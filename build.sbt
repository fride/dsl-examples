
scalaVersion := "2.9.0"

name := "dsl-examples"

version := "0.1"

scalaVersion := "2.9.0"

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                    "releases" at "http://scala-tools.org/repo-releases")


libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "1.6.1"
    , "org.scalaz" % "scalaz-core_2.9.0-1" % "6.0.1"
    , "org.specs2" %% "specs2" % "1.5"
    , "org.neo4j" % "neo4j" % "1.4.M06"
    , "org.specs2" %% "specs2-scalaz-core" % "6.0.RC2" % "test")

//testListeners += customTestListener
testListeners <<= target.map(t => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath)))

// enable publishing the jar produced by `test:package`
publishArtifact in (Test, packageBin) := true

// enable publishing the test API jar
publishArtifact in (Test, packageDoc) := true

// enable publishing the test sources jar
publishArtifact in (Test, packageSrc) := true
