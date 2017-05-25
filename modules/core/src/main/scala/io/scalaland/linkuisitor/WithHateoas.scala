package io.scalaland.linkuisitor

final case class WithHateoas[T: LinkProvider](entity: T) {

  lazy val links = LinkProvider[T].provideFor(entity)
}

object WithHateoas {

  def wrap[T: LinkProvider]: T => WithHateoas[T] = WithHateoas.apply
}
