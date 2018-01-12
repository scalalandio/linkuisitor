import sbt._

import Dependencies._

object Dependencies {

  // scala version
  val scalaVersion = "2.12.4"

  // build tools versions
  val scalaFmtVersion = "1.4.0"

  val circeVersion  = "0.9.0"
  val specs2Version = "4.0.2"

  // resolvers
  val resolvers = Seq(
    Resolver typesafeRepo "releases"
  )

  val circeCore    = "io.circe" %% "circe-core"    % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion

  // testing
  val spec2Core = "org.specs2" %% "specs2-core" % specs2Version
}

trait Dependencies {

  val scalaVersionUsed = scalaVersion

  val scalaFmtVersionUsed = scalaFmtVersion

  // resolvers
  val commonResolvers = resolvers

  val mainDeps = Seq(circeCore, circeGeneric)

  val testDeps = Seq(spec2Core)

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
