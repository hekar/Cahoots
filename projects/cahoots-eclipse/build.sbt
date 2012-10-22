import sbt._
import sbt.Keys._

name := "Cahoot"

version := "1.0"

scalaVersion := "2.9.1"

retrieveManaged := true

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases" at "http://oss.sonatype.org/content/repositories/releases",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies += "com.miglayout" % "miglayout-swt" % "4.2"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

libraryDependencies += "org.scalatest" % "scalatest_2.9.1" % "2.0.M4"

libraryDependencies += "junit" % "junit" % "4.10"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.2"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration"

libraryDependencies += "commons-httpclient" % "commons-httpclient" % "3.1"
            
libraryDependencies += "com.chuusai" % "shapeless_2.9.1" % "1.2.2"

unmanagedJars in Compile <++= baseDirectory map { base =>
  val baseDirectories = (base / "lib") +++
    (base / "../../../../bin/eclipse-jee-indigo/plugins")
    val customJars = (baseDirectories ** "*.jar") filter { !_.getAbsolutePath().contains("source") }
      customJars.classpath
}
