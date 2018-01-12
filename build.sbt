import sbt._
import Settings._

scalaVersion in ThisBuild := scalaVersionUsed
scalafmtVersion in ThisBuild := scalaFmtVersionUsed

lazy val root = project.root
  .configureRoot
  .aggregate(core)
  .settings(
    publish := { () },
    publishLocal := { () },
    publishArtifact := false
  )

lazy val core = project.from("core")
  .setName("linkuisitor-core")
  .setDescription("Serializes objects into HATEOAS representation using Circe.io")
  .setInitialCommand("_")
  .configureModule
  .configureTests()
  .settings(resourceGenerators in Compile += task[Seq[File]] {
    val file = (resourceManaged in Compile).value / "linkuisitor-version.conf"
    IO.write(file, s"version=${version.value}")
    Seq(file)
  })
  .settings(
    homepage := Some(url("http://github.com/MateuszKubuszok/linkuisitor")),
    licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    scmInfo := Some(ScmInfo(
      url("https://github.com/scalalandio/linkuisitor"),
      "scm:git:git@github.com:scalalandio/linkuisitor.git"
    )),
    publishTo := Some({
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) "snapshots" at s"${nexus}content/repositories/snapshots"
      else "releases" at s"${nexus}service/local/staging/deploy/maven2"
    }),
    developers := List(
      Developer(
        id = "MateuszKubuszok",
        name = "Mateusz Kubuszok",
        email = "mateusz.kubuszok@gmail.com",
        url = url("http://github.com/MateuszKubuszok")
      )
    ),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false }
  )
