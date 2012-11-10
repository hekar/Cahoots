import sbt._
import sbt.Keys._

name := "cahoot-eclipse"

version := "1.0"

scalaVersion := "2.9.1"

retrieveManaged := true

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases" at "http://oss.sonatype.org/content/repositories/releases",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies += "com.miglayout" % "miglayout-swt" % "4.2"

libraryDependencies += "junit" % "junit" % "4.10"

libraryDependencies += "commons-httpclient" % "commons-httpclient" % "3.1"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.2"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

libraryDependencies += "com.google.guava" % "guava" % "13.0.1"

unmanagedJars in Compile <++= baseDirectory map { base =>
  val baseDirectories = (base / "lib") +++
    (file(System.getenv("ECLIPSE_HOME") + "/plugins"))
    val customJars = (baseDirectories ** "*.jar") filter { !_.getAbsolutePath().contains("source") }
      customJars.classpath
}
