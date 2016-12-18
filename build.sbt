import com.typesafe.config._

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()

name := """painting"""

version := conf.getString("app.version")

lazy val root = (project in file(".")).enablePlugins(PlayJava, JavaServerAppPackaging)

scalaVersion := "2.11.8"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "uk.co.panaxiom" %% "play-jongo" % "2.0.0-jongo1.3"
)

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := false
