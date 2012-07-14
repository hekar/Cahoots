import sbt._
import sbt.Keys._

name := "Cahoot"

version := "1.0"

scalaVersion := "2.9.1"

retrieveManaged := true

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases" at "http://oss.sonatype.org/content/repositories/releases")

libraryDependencies += "com.miglayout" % "miglayout-swt" % "4.2"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"

libraryDependencies += "org.specs2" %% "specs2" % "1.11" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration"

unmanagedJars in Compile <++= baseDirectory map { base =>
  val baseDirectories = (base / "lib") +++
    (base / "../../../bin/escala/plugins")
    val customJars = (baseDirectories ** "*.jar") filter { !_.getAbsolutePath().contains("source") }
      customJars.classpath
}
