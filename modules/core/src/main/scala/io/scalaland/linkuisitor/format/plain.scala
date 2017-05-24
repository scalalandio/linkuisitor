package io.scalaland.linkuisitor.format

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.scalaland.linkuisitor._

trait plain {

  private case class PlainLink(rel: String, href: String, method: Option[String])

  implicit def linkedEncoder[T: Encoder]: Encoder[WithHateoas[T]] = (linked: WithHateoas[T]) => Json.obj(
    "entity" -> Encoder[T].apply(linked.entity),
    "links" -> flattenLinks(linked.links).asJson
  )

  private def flattenLinks(links: Links): Seq[PlainLink] = links.toSeq.flatMap {
    case (rel, FlatLinkDetails(href, method, _)) => Seq(PlainLink(rel, href, method map (_.toString)))
    case (rel, GroupedLinkDetails(group)) => group map {
      case FlatLinkDetails(href, method, _) =>
        PlainLink(rel, href, method map (_.toString))
    }
  }
}

object plain extends plain
