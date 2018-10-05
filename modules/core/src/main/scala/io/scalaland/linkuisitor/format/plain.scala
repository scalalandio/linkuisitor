package io.scalaland.linkuisitor.format

import io.circe._
import io.circe.syntax._
import io.scalaland.linkuisitor._

trait plain {

  private case class PlainLink(rel: String,
                               href: String,
                               method: Option[HttpMethod])
  private object PlainLink {

    implicit val encoder: Encoder[PlainLink] = fld => Json.obj(
      "rel" -> fld.rel.asJson,
      "href" -> fld.href.asJson,
      "method" -> fld.method.asJson
    )
  }

  implicit def linkedEncoder[T: Encoder]: Encoder[WithHateoas[T]] =
    (linked: WithHateoas[T]) =>
      Json.obj(
        "entity" -> Encoder[T].apply(linked.entity),
        "links" -> flattenLinks(linked.links).asJson
    )

  private def flattenLinks(links: Links): List[PlainLink] = links.toList.flatMap {
    case (rel, FlatLinkDetails(href, method, _)) =>
      List(PlainLink(rel, href, method))
    case (rel, GroupedLinkDetails(group)) =>
      group map {
        case FlatLinkDetails(href, method, _) =>
          PlainLink(rel, href, method)
      }
  }
}

object plain extends plain
