import sbt._

import Dependencies._

object Dependencies {

  // scala version
  val scalaVersion = "2.12.2"

  val monixVersion = "2.2.3"
  val specs2Version = "3.8.9"

  // resolvers
  val resolvers = Seq(
    Resolver sonatypeRepo "public",
    Resolver typesafeRepo "releases",
    Resolver.bintrayRepo("cakesolutions", "maven")
  )

  val scalaConfig = "com.typesafe" % "config" % "1.3.1"
  val pureConfig  = "com.github.melrief" %% "pureconfig" % "0.6.0"

  // async
  val monix     = "io.monix" %% "monix"      % monixVersion
  val monixCats = "io.monix" %% "monix-cats" % monixVersion

  // functional libraries
  val cats = "org.typelevel" %% "cats" % "0.9.0"

  // logging
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  val logback      = "ch.qos.logback" % "logback-classic" % "1.2.2"

  // testing
  val spec2Core       = "org.specs2" %% "specs2-core"       % specs2Version
  val spec2JUnit      = "org.specs2" %% "specs2-junit"      % specs2Version
  val spec2Mock       = "org.specs2" %% "specs2-mock"       % specs2Version
  val spec2Scalacheck = "org.specs2" %% "specs2-scalacheck" % specs2Version
}

trait Dependencies {

  val scalaVersionUsed = scalaVersion

  // resolvers
  val commonResolvers = resolvers

  val mainDeps = Seq(cats, scalaConfig, pureConfig, monix, monixCats, scalaLogging, logback)

  val testDeps = Seq(spec2Core, spec2JUnit, spec2Mock, spec2Scalacheck)

  implicit class ProjectRoot(project: Project) {

    def root: Project = project in file(".")
  }

  implicit class ProjectFrom(project: Project) {

    private val commonDir = "modules"

    def from(dir: String): Project = project in file(s"$commonDir/$dir")
  }

  implicit class DependsOnProject(project: Project) {

    private val testConfigurations = Set("test", "fun", "it")
    private def findCompileAndTestConfigs(p: Project) =
      (p.configurations.map(_.name).toSet intersect testConfigurations) + "compile"

    private val thisProjectsConfigs = findCompileAndTestConfigs(project)
    private def generateDepsForProject(p: Project) =
      p % (thisProjectsConfigs intersect findCompileAndTestConfigs(p) map (c => s"$c->$c") mkString ";")

    def compileAndTestDependsOn(projects: Project*): Project =
      project dependsOn (projects.map(generateDepsForProject): _*)
  }
}
