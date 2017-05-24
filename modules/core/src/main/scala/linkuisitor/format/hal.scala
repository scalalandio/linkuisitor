package linkuisitor.format

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import linkuisitor.{ FlatLinkDetails, GroupedLinkDetails, LinkDetails, WithHateoas }

trait hal {

  implicit val linkDetailsEncoder: Encoder[LinkDetails] = {
    case ld: FlatLinkDetails    => ld.asJson
    case ld: GroupedLinkDetails => ld.group.asJson
  }

  implicit def linkedEncoder[T: Encoder]: Encoder[WithHateoas[T]] = (linked: WithHateoas[T]) =>
    Encoder[T].apply(linked.entity).withObject(_.add("_links", linked.links.asJson).asJson)
}

object hal extends hal
