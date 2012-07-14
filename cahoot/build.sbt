import sbt._
import sbt.Keys._

name := "Cahoot"

version := "1.0"

scalaVersion := "2.9.1"

retrieveManaged := true

libraryDependencies += "com.miglayout" % "miglayout-swt" % "4.2"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

unmanagedJars in Compile <++= baseDirectory map { base =>
  val baseDirectories = (base / "lib") +++
      (base / "../../../bin/escala/plugins")
    val customJars = (baseDirectories ** "*.jar") filter { !_.getAbsolutePath().contains("source") }
      customJars.classpath
}
