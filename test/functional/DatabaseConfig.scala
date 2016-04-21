package functional

import play.api.db.{Database, Databases}

import scala.util.Properties

/**
  * Documentation Here
  *
  * @author Deny Prasetyo.
  */
object DatabaseConfig {
  val url = Properties.envOrElse("PLAY_PRODUCT_DB_TEST_URL", "jdbc:postgresql://localhost:15432/product-test")
  val username = Properties.envOrElse("PLAY_PRODUCT_DB_TEST_USERNAME", "root")
  val password = Properties.envOrElse("PLAY_PRODUCT_DB_TEST_PASSWORD", "localhost")

  val database = Databases(
    driver = "org.postgresql.Driver",
    url = url,
    name = "Product-Test",
    config = Map(
      "username" -> username,
      "password" -> password,
      "logStatements" -> true
    )
  )

  def withDatabase[T](block: Database => T): T = {
    Databases.withDatabase(
      driver = "org.postgresql.Driver",
      url = url,
      name = "Product-Test",
      config = Map(
        "username" -> username,
        "password" -> password,
        "logStatements" -> true
      )
    )(block)
  }
}
