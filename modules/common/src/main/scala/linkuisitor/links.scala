package linkuisitor

sealed trait LinkDetails

@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
final case class FlatLinkDetails(
  href:      String,
  method:    Option[HttpMethod] = None,
  templated: Option[Boolean]    = None
) extends LinkDetails

final case class GroupedLinkDetails(
  group: Seq[FlatLinkDetails]
) extends LinkDetails
