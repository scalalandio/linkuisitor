package linkuisitor.second

import com.typesafe.scalalogging.Logger
import pureconfig.loadConfig

object Second {

  val config = loadConfig[SecondConfig]("first").getOrElse(SecondConfig("undefined"))

  val logger = Logger(getClass)

  def main(args: Array[String]): Unit = logger.info(s"Run first at version: ${config.version}")
}
