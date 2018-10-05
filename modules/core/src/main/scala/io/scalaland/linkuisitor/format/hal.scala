package io.scalaland.linkuisitor.format

import io.circe._
import io.circe.syntax._
import io.scalaland.linkuisitor.{ FlatLinkDetails, GroupedLinkDetails, LinkDetails, WithHateoas }

trait hal {

  implicit val fldEncoder: Encoder[FlatLinkDetails] = fld => Json.obj(
    "href" -> fld.href.asJson,
    "method" -> fld.method.asJson,
    "templated" -> fld.templated.asJson
  )
  implicit val gldEncoder: Encoder[GroupedLinkDetails] = Encoder[List[FlatLinkDetails]].contramap(_.group)
  implicit val ldEncoder: Encoder[LinkDetails] = {
    case fld: FlatLinkDetails => fld.asJson
    case gld: GroupedLinkDetails => gld.asJson
  }

  implicit def linkedEncoder[T: Encoder]: Encoder[WithHateoas[T]] =
    (linked: WithHateoas[T]) =>
      Encoder[T]
        .apply(linked.entity)
        .withObject(_.add("_links", linked.links.asJson).asJson)
}

object hal extends hal
