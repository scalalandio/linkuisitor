# Linkuisitor

Plebeian-simple library for generating HATEOAS JSONs with Circe.

## Usage

    import io.circe._
    import io.circe.generic.auto._
    import io.circe.syntax._

    case class Test(string: String, int: Int)

    implicit val testLinks: LinkProvider[Test] = (test: Test) => Map(
      ...
    )

    {
      // convert class into plain (unstandardized format)
      import linkuisitor.format.plain._
      WithHateoas(Test("test", 1)).asJson
    }

One can change format by using different import.

## Notices

Library will only generate links for entities directly wrapped using
`WithHateoas` wrapper. If one need to have nested HATEOAS entities,
then each of them in a nested structure should be wrapped separately.
