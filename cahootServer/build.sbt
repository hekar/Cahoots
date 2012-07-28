import sbt._
import sbt.Keys._

name := "Cahoot Server"

version := "1.0"

scalaVersion := "2.9.1"

retrieveManaged := true

libraryDependencies += "com.reportgrid" % "blueeyes_2.9.1" % "0.4.24" % "compile"

resolvers ++= Seq(
    "Sonatype"    at "http://nexus.scala-tools.org/content/repositories/public",
    "Scala Tools" at "http://scala-tools.org/repo-snapshots/",
    "JBoss"       at "http://repository.jboss.org/nexus/content/groups/public/",
    "Akka"        at "http://repo.akka.io/releases/",
    "GuiceyFruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
)

