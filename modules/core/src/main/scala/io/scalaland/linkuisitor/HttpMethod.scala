package io.scalaland.linkuisitor

import io.circe.Encoder

sealed trait HttpMethod extends Product with Serializable {
  val name: String; override def toString: String = name
}
object HttpMethod {
  case object GET extends HttpMethod { val name = "GET" }
  case object POST extends HttpMethod { val name = "POST" }
  case object PUT extends HttpMethod { val name = "PUT" }
  case object DELETE extends HttpMethod { val name = "DELETE" }
  case object PATCH extends HttpMethod { val name = "PATCH" }
  case object HEAD extends HttpMethod { val name = "HEAD" }
  case object OPTIONS extends HttpMethod { val name = "OPTIONS" }
  val values: Set[HttpMethod] =
    Set[HttpMethod](GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS)

  implicit val encoder: Encoder[HttpMethod] = Encoder[String].contramap(_.name)
}
