import sbt._
import sbt.Keys._

name := "cahoot-eclipse"

version := "1.0"

scalaVersion := "2.9.1"

retrieveManaged := true

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases" at "http://oss.sonatype.org/content/repositories/releases",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
	"ibiblio" at "http://mirrors.ibiblio.org/maven/")

libraryDependencies += "com.miglayout" % "miglayout-swt" % "4.2" withSources ()

libraryDependencies += "junit" % "junit" % "4.10" withSources ()

libraryDependencies += "commons-httpclient" % "commons-httpclient" % "3.1" withSources ()

libraryDependencies += "commons-collections" % "commons-collections" % "3.2.1" withSources ()

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.2" withSources ()

libraryDependencies += "com.googlecode.lambdaj" % "lambdaj" % "2.3.3" withSources ()

libraryDependencies += "com.google.inject" % "guice" % "3.0" withSources ()

libraryDependencies += "com.google.guava" % "guava" % "13.0.1" withSources ()

libraryDependencies += "org.eclipse.jetty" % "jetty-websocket" % "7.6.8.v20121106" withSources ()

libraryDependencies += "com.google.code.gson" % "gson" % "2.2.2" withSources ()

libraryDependencies += "com.ning" % "async-http-client" % "1.7.9" withSources ()

libraryDependencies += "io.netty" % "netty" % "3.6.1.Final" withSources ()

libraryDependencies += "joda-time" % "joda-time" % "2.1" withSources ()

libraryDependencies += "org.hamcrest" % "hamcrest-library" % "1.3" withSources ()

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" withSources ()

unmanagedJars in Compile <++= baseDirectory map { base =>
  val baseDirectories = (base / "lib") +++
    (file(System.getenv("ECLIPSE_HOME") + "/plugins"))
    val customJars = (baseDirectories ** "*.jar") filter { !_.getAbsolutePath().contains(".source") }
      customJars.classpath
}
