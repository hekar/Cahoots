import sbt._
import sbt.Keys._

name := "cahoot-op"

version := "1.0"

scalaVersion := "2.9.1"

retrieveManaged := true

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases" at "http://oss.sonatype.org/content/repositories/releases",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies += "org.scalatest" % "scalatest_2.9.1" % "2.0.M4"

libraryDependencies += "junit" % "junit" % "4.10"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.2"