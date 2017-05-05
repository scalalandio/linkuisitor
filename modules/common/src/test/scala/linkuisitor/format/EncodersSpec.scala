package linkuisitor.format

import io.circe.Printer
import linkuisitor._
import org.specs2.mutable.Specification

class EncodersSpec extends Specification {

  case class TestInner(
    module: String,
    page:   Int
  )

  implicit val testInnerLinks: LinkProvider[TestInner] = (testInner: TestInner) => Map(
    "self" -> FlatLinkDetails(s"/api/${testInner.module}/page/${testInner.page}"),
    "next" -> FlatLinkDetails(s"/api/${testInner.module}/page/${testInner.page + 1}")
  )

  case class TestOuter(
    seq: Seq[WithHateoas[TestInner]]
  )

  implicit val testOuterLinks: LinkProvider[TestOuter] = (_: TestOuter) => Map(
    "self" -> GroupedLinkDetails(Seq(
      FlatLinkDetails("/api/stuff"),
      FlatLinkDetails("/api/other")
    ))
  )

  "plain HATEOAS" should {

    "encode data into { entity: { [properties] }, links: { [links] } }" in {
      // given
      import io.circe.generic.auto._
      import io.circe.syntax._
      import linkuisitor.format.plain._

      val testOuter = WithHateoas(TestOuter(Seq(
        WithHateoas(TestInner("fizz", 1)),
        WithHateoas(TestInner("buzz", 2)),
        WithHateoas(TestInner("bar", 3))
      )))

      // when
      val result = testOuter.asJson.pretty(Printer.spaces2.copy(dropNullKeys = true))

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
           |        "links" : {
           |          "self" : {
           |            "href" : "/api/fizz/page/1"
           |          },
           |          "next" : {
           |            "href" : "/api/fizz/page/2"
           |          }
           |        }
           |      },
           |      {
           |        "entity" : {
           |          "module" : "buzz",
           |          "page" : 2
           |        },
           |        "links" : {
           |          "self" : {
           |            "href" : "/api/buzz/page/2"
           |          },
           |          "next" : {
           |            "href" : "/api/buzz/page/3"
           |          }
           |        }
           |      },
           |      {
           |        "entity" : {
           |          "module" : "bar",
           |          "page" : 3
           |        },
           |        "links" : {
           |          "self" : {
           |            "href" : "/api/bar/page/3"
           |          },
           |          "next" : {
           |            "href" : "/api/bar/page/4"
           |          }
           |        }
           |      }
           |    ]
           |  },
           |  "links" : {
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
