import com.typesafe.sbt.GitVersioning
import sbt.TestFrameworks.Specs2
import sbt.Tests.Argument
import sbt._
import sbt.Keys._
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import scoverage.ScoverageSbtPlugin
import org.scalastyle.sbt.ScalastylePlugin.autoImport._
import wartremover._

object Settings extends Dependencies {

  val FunctionalTest: Configuration = config("fun") extend Test describedAs "Runs only functional tests"

  private val commonSettings = Seq(
    organization := "io.scalaland",
    scalaVersion := scalaVersionUsed
  )

  private val rootSettings = commonSettings

  private val modulesSettings = commonSettings ++ Seq(
    scalacOptions ++= Seq(
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-extra-implicit",
      "-Ywarn-inaccessible",
      "-Ywarn-infer-any",
      "-Ywarn-macros:after",
      "-Ywarn-nullary-override",
      "-Ywarn-nullary-unit",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates",
      "-Xfatal-warnings",
      "-Xfuture",
      "-Xlint",
      "-Xlint:adapted-args",
      "-Xlint:by-name-right-associative",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-override",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Xlint:unsound-match"
    ),
    scalacOptions in (Compile, console) --= Seq(
      "-Xlint",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates",
      "-Xfatal-warnings"
    ),

    resolvers ++= commonResolvers,

    libraryDependencies ++= mainDeps,

    scalafmtOnCompile in Compile := true,

    scalastyleFailOnError := true,

    wartremoverWarnings in (Compile, compile) ++= Warts.allBut(
      Wart.Any,
      Wart.DefaultArguments,
      Wart.ExplicitImplicitTypes,
      Wart.ImplicitConversion,
      Wart.ImplicitParameter,
      Wart.Overloading,
      Wart.PublicInference,
      Wart.NonUnitStatements,
      Wart.Nothing
    )
  )

  abstract class TestConfigurator(project: Project, config: Configuration) {

    protected def configure(requiresFork: Boolean): Project = project
      .configs(config)
      .settings(inConfig(config)(Defaults.testSettings): _*)
      .settings(inConfig(config)(scalafmtSettings))
      .settings(scalafmtOnCompile in config := true)
      .settings(scalastyleConfig in config := baseDirectory.value / "scalastyle-test-config.xml")
      .settings(scalastyleFailOnError in config := false)
      .settings(fork in config := requiresFork)
      .settings(testFrameworks := Seq(Specs2))
      .settings(libraryDependencies ++= testDeps map (_ % config.name))
      .enablePlugins(ScoverageSbtPlugin)

    protected def configureSequential(requiresFork: Boolean): Project = configure(requiresFork)
      .settings(testOptions in config += Argument(Specs2, "sequential"))
      .settings(parallelExecution in config := false)
  }

  implicit class DataConfigurator(project: Project) {

    def setName(newName: String): Project = project.settings(name := newName)

    def setDescription(newDescription: String): Project = project.settings(description := newDescription)

    def setInitialCommand(newInitialCommand: String): Project =
      project.settings(initialCommands := s"import io.scalaland.linkuisitor.$newInitialCommand")
  }

  implicit class RootConfigurator(project: Project) {

    def configureRoot: Project = project.settings(rootSettings: _*)
  }

  implicit class ModuleConfigurator(project: Project) {

    def configureModule: Project = project.settings(modulesSettings: _*).enablePlugins(GitVersioning)
  }

  implicit class UnitTestConfigurator(project: Project) extends TestConfigurator(project, Test) {

    def configureTests(requiresFork: Boolean = false): Project = configure(requiresFork)

    def configureTestsSequential(requiresFork: Boolean = false): Project = configureSequential(requiresFork)
  }

  implicit class FunctionalTestConfigurator(project: Project) extends TestConfigurator(project, FunctionalTest) {

    def configureFunctionalTests(requiresFork: Boolean = false): Project = configure(requiresFork)

    def configureFunctionalTestsSequential(requiresFork: Boolean = false): Project = configureSequential(requiresFork)
  }

  implicit class IntegrationTestConfigurator(project: Project) extends TestConfigurator(project, IntegrationTest) {

    def configureIntegrationTests(requiresFork: Boolean = false): Project = configure(requiresFork)

    def configureIntegrationTestsSequential(requiresFork: Boolean = false): Project = configureSequential(requiresFork)
  }
}
