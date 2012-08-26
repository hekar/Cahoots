import sbt._
import sbt.IO._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "cahootServer"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "com.typesafe" % "slick_2.10.0-M6" % "0.11.0",
    "org.jooq" % "jooq" % "2.4.0"
  )

  /**
   * Generate the jOOQ database classes
   */
  val jooq = TaskKey[Unit]("jooq", "Generate the jOOQ class files")
  val jooqTask = jooq := {
    println("Downloading jOOQ jars")
    sys.process.Process(Seq("jooq/download_jooq.sh")) !!
    
    println("Generating jOOQ classes")
    sys.process.Process(Seq("jooq/generate_jooq.sh")) !!
  }

  /**
   * Add your own project settings here
   */
  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    jooqTask

  )

}
