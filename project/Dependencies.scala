import sbt._

import Dependencies._

object Dependencies {

  // scala version
  val scalaVersion = "2.12.2"

  val circeVersion   = "0.7.0"
  val specs2Version = "3.8.9"

  // resolvers
  val resolvers = Seq(
    Resolver sonatypeRepo "public",
    Resolver typesafeRepo "releases",
    Resolver.bintrayRepo("cakesolutions", "maven")
  )

  val circeCore    = "io.circe" %% "circe-core"    % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  val circeParser  = "io.circe" %% "circe-parser"  % circeVersion

  // testing
  val spec2Core       = "org.specs2" %% "specs2-core"       % specs2Version
  val spec2JUnit      = "org.specs2" %% "specs2-junit"      % specs2Version
}

trait Dependencies {

  val scalaVersionUsed = scalaVersion

  // resolvers
  val commonResolvers = resolvers

  val mainDeps = Seq(circeCore, circeGeneric, circeParser)

  val testDeps = Seq(spec2Core, spec2JUnit)

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