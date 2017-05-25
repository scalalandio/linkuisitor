# Linkuisitor

[![Build Status](https://travis-ci.org/scalalandio/linkuisitor.svg?branch=master)](https://travis-ci.org/scalalandio/linkuisitor)
[![Maven Central](https://img.shields.io/maven-central/v/io.scalaland/linkuisitor-core_2.12.svg)](http://search.maven.org/#search%7Cga%7C1%7Clinkuisitor)

Plebeian-simple library for generating HATEOAS JSONs with Circe.

## Adding to project

    libraryDependencies += "io.scalaland" %% "linkuisitor-core" % "0.1.0"

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
