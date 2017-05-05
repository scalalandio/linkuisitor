import sbt._
import Settings._

scalaVersion in ThisBuild := scalaVersionUsed

lazy val root = project.root
  .setName("Linkuisotor")
  .setDescription("Serializes objects into HATEOAS representation")
  .configureRoot
  .aggregate(common)

lazy val common = project.from("common")
  .setName("Linkuisotor")
  .setDescription("Serializes objects into HATEOAS representation")
  .setInitialCommand("_")
  .configureModule
  .configureTests()
  .settings(resourceGenerators in Compile += task[Seq[File]] {
    val file = (resourceManaged in Compile).value / "linkuisitor-version.conf"
    IO.write(file, s"version=${version.value}")
    Seq(file)
  })
