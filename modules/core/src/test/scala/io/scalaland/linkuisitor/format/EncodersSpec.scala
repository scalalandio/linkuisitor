package io.scalaland.linkuisitor.format

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.scalaland.linkuisitor._
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class EncodersSpec extends Specification {

  trait TestDataSerialization extends Scope {

    case class TestInner(
        module: String,
        page: Int
    )

    implicit val testInnerLinks: LinkProvider[TestInner] =
      (testInner: TestInner) =>
        Map(
          "self" -> FlatLinkDetails(
            s"/api/${testInner.module}/page/${testInner.page}"),
          "next" -> FlatLinkDetails(
            s"/api/${testInner.module}/page/${testInner.page + 1}")
      )

    case class TestOuter(
        seq: Seq[WithHateoas[TestInner]]
    )

    implicit val testOuterLinks: LinkProvider[TestOuter] = (_: TestOuter) =>
      Map(
        "self" -> GroupedLinkDetails(
          Seq(
            FlatLinkDetails("/api/stuff"),
            FlatLinkDetails("/api/other")
          ))
    )

    val testOuter = WithHateoas(
      TestOuter(
        Seq(
          WithHateoas(TestInner("fizz", 1)),
          WithHateoas(TestInner("buzz", 2)),
          WithHateoas(TestInner("bar", 3))
        )))
  }

  "plain HATEOAS" should {

    "encode data into { entity: { [properties] }, links: { [links] } }" in new TestDataSerialization {
      // given
      import io.scalaland.linkuisitor.format.plain._

      // when
      val result =
        testOuter.asJson.pretty(Printer.spaces2.copy(dropNullValues = true))

      // then
      result mustEqual
        s"""{
           |  "entity" : {
           |    "seq" : [
           |      {
           |        "entity" : {
           |          "module" : "fizz",
           |          "page" : 1
           |        },
           |        "links" : [
           |          {
           |            "rel" : "self",
           |            "href" : "/api/fizz/page/1"
           |          },
           |          {
           |            "rel" : "next",
           |            "href" : "/api/fizz/page/2"
           |          }
           |        ]
           |      },
           |      {
           |        "entity" : {
           |          "module" : "buzz",
           |          "page" : 2
           |        },
           |        "links" : [
           |          {
           |            "rel" : "self",
           |            "href" : "/api/buzz/page/2"
           |          },
           |          {
           |            "rel" : "next",
           |            "href" : "/api/buzz/page/3"
           |          }
           |        ]
           |      },
           |      {
           |        "entity" : {
           |          "module" : "bar",
           |          "page" : 3
           |        },
           |        "links" : [
           |          {
           |            "rel" : "self",
           |            "href" : "/api/bar/page/3"
           |          },
           |          {
           |            "rel" : "next",
           |            "href" : "/api/bar/page/4"
           |          }
           |        ]
           |      }
           |    ]
           |  },
           |  "links" : [
           |    {
           |      "rel" : "self",
           |      "href" : "/api/stuff"
           |    },
           |    {
           |      "rel" : "self",
           |      "href" : "/api/other"
           |    }
           |  ]
           |}""".stripMargin
    }
  }

  "HAL HATEOAS" should {

    "encode data into { [properties], _links: { [links] } }" in new TestDataSerialization {
      // given
      import io.scalaland.linkuisitor.format.hal._

      // when
      val result =
        testOuter.asJson.pretty(Printer.spaces2.copy(dropNullValues = true))

      // then
      result mustEqual
        s"""{
           |  "seq" : [
           |    {
           |      "module" : "fizz",
           |      "page" : 1,
           |      "_links" : {
           |        "self" : {
           |          "href" : "/api/fizz/page/1"
           |        },
           |        "next" : {
           |          "href" : "/api/fizz/page/2"
           |        }
           |      }
           |    },
           |    {
           |      "module" : "buzz",
           |      "page" : 2,
           |      "_links" : {
           |        "self" : {
           |          "href" : "/api/buzz/page/2"
           |        },
           |        "next" : {
           |          "href" : "/api/buzz/page/3"
           |        }
           |      }
           |    },
           |    {
           |      "module" : "bar",
           |      "page" : 3,
           |      "_links" : {
           |        "self" : {
           |          "href" : "/api/bar/page/3"
           |        },
           |        "next" : {
           |          "href" : "/api/bar/page/4"
           |        }
           |      }
           |    }
           |  ],
           |  "_links" : {
           |    "self" : [
           |      {
           |        "href" : "/api/stuff"
           |      },
           |      {
           |        "href" : "/api/other"
           |      }
           |    ]
           |  }
           |}""".stripMargin
    }
  }
}
