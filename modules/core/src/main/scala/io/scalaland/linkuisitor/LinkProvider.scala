package io.scalaland.linkuisitor

trait LinkProvider[T] {

  def provideFor(entity: T): Links
}

object LinkProvider {

  def apply[T: LinkProvider]: LinkProvider[T] = implicitly[LinkProvider[T]]
}
