package io.scalaland.linkuisitor

class WithHateoas[T: LinkProvider](val entity: T) {

  lazy val links = LinkProvider[T].provideFor(entity)
}

object WithHateoas {

  def apply[T: LinkProvider](entity: T): WithHateoas[T] = new WithHateoas(entity)
}
