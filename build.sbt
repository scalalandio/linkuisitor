import sbt._
import Settings._

scalaVersion in ThisBuild := scalaVersionUsed

lazy val root = project.root
  .configureRoot
  .aggregate(core)

lazy val core = project.from("core")
  .setName("linkuisotor-core")
  .setDescription("Serializes objects into HATEOAS representation using Circe.io")
  .setInitialCommand("_")
  .configureModule
  .configureTests()
  .settings(resourceGenerators in Compile += task[Seq[File]] {
    val file = (resourceManaged in Compile).value / "linkuisitor-version.conf"
    IO.write(file, s"version=${version.value}")
    Seq(file)
  })
