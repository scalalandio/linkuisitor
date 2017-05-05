package linkuisitor

class Linked[T: LinkProvider](val entity: T) {

  lazy val links = LinkProvider[T].provideFor(entity)
}

object Linked {

  def apply[T: LinkProvider](entity: T): Linked[T] = new Linked(entity)
}
