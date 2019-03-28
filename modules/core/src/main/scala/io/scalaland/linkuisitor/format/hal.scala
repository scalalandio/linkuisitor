package io.scalaland.linkuisitor.format

import io.circe._
import io.circe.syntax._
import io.scalaland.linkuisitor.{ FlatLinkDetails, GroupedLinkDetails, LinkDetails, WithHateoas }

trait hal {

  implicit val fldEncoder: Encoder[FlatLinkDetails] = fld => Json.obj(
    Seq("href" -> fld.href.asJson) ++
    fld.method.toList.map(m => "method" -> m.asJson) ++
    fld.templated.toList.map(t => "templated" -> t.asJson): _*
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
        .withObject(obj => {
          if (linked.links.isEmpty) obj
          else obj.add("_links", linked.links.asJson)
        }.asJson)
}

object hal extends hal
