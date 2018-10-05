import sbt._
import Settings._
import sbtcrossproject.CrossPlugin.autoImport.{ crossProject, CrossType }

lazy val root = project.root
  .setName("linquisitor")
  .setDescription("Serializes objects into HATEOAS representation using Circe.io")
  .configureRoot
  .noPublish
  .aggregate(coreJVM, coreJS)

lazy val core = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).build.from("core")
  .setName("linkuisitor-core")
  .setDescription("Serializes objects into HATEOAS representation using Circe.io")
  .configureModule
  .configureTests()
  .publish
  .settings(libraryDependencies ++= Seq(
    "org.specs2" %%% "specs2-core"          % Dependencies.specs2Version % "test",
    "org.specs2" %%% "specs2-scalacheck"    % Dependencies.specs2Version % "test"
  ))

lazy val coreJVM = core.jvm
lazy val coreJS = core.js
