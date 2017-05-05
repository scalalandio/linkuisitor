package linkuisitor.format

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import linkuisitor.{ FlatLinkDetails, GroupedLinkDetails, LinkDetails, WithHateoas }

package object plain {

  implicit val linkDetailsEncoder: Encoder[LinkDetails] = {
    case ld: FlatLinkDetails    => ld.asJson
    case ld: GroupedLinkDetails => ld.group.asJson
  }

  implicit def linkedEncoder[T: Encoder]: Encoder[WithHateoas[T]] = (linked: WithHateoas[T]) => Json.obj(
    "entity" -> Encoder[T].apply(linked.entity),
    "links" -> linked.links.asJson
  )
}
