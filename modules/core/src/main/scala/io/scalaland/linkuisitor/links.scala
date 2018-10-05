package io.scalaland.linkuisitor

sealed trait LinkDetails extends Product with Serializable

@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
final case class FlatLinkDetails(
    href: String,
    method: Option[HttpMethod] = None,
    templated: Option[Boolean] = None
) extends LinkDetails

final case class GroupedLinkDetails(
    group: List[FlatLinkDetails]
) extends LinkDetails
