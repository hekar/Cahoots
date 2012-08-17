import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "cahootServer"
    val appVersion      = "1.0-SNAPSHOT"
     
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
      println("Testing. Jooq")
      "java -classpath jooq-2.4.0.jar;jooq-meta-2.4.0.jar;jooq-codegen-2.4.0.jar;mysql-connector-java-5.1.18-bin.jar;. org.jooq.util.GenerationTool /guestbook.xml" !
    }

       retrieveManaged := true
    /**
     * Add your own project settings here
     */
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
       // ==============================================
       // Tasks
       // ==============================================
       jooqTask
 
       // ==============================================
       // Settings
       // ==============================================
    )

}
