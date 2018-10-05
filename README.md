# Linkuisitor

[![Build Status](https://travis-ci.org/scalalandio/linkuisitor.svg?branch=master)](https://travis-ci.org/scalalandio/linkuisitor)
[![Maven Central](https://img.shields.io/maven-central/v/io.scalaland/linkuisitor-core_2.12.svg)](http://search.maven.org/#search%7Cga%7C1%7Clinkuisitor)
[![License](http://img.shields.io/:license-Apache%202-green.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

Plebeian-simple library for generating HATEOAS JSONs with Circe.

## Adding to project

Scala 2.11, 2.12, 2.13.0-M4:

    libraryDependencies += "io.scalaland" %% "linkuisitor-core" % "0.4.0"

Scala.js

    libraryDependencies += "io.scalaland" %%% "linkuisitor-core" % "0.4.0"

## Usage

    import io.circe._
    import io.circe.generic.auto._
    import io.circe.syntax._

    case class Test(string: String, int: Int)

    implicit val testLinks: LinkProvider[Test] = (test: Test) => Map(
      ...
    )

    {
      // convert class into plain (unstandardized) format
      import linkuisitor.format.plain._
      WithHateoas(Test("test", 1)).asJson
    }

    {
      // convert class into hal format
      import linkuisitor.format.hal._
      WithHateoas(Test("test", 1)).asJson
    }

One can change format by using different import.

## Notices

Library will only generate links for entities directly wrapped using
`WithHateoas` wrapper. If one need to have nested HATEOAS entities,
then each of them in a nested structure should be wrapped separately.

Library support no `entities` section at the moment.

Library support no custom link properties.

PRs are welcome.
