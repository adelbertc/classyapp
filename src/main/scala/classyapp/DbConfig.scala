package classyapp

import monocle.Lens
import monocle.macros.GenLens

sealed abstract class DbConnection

sealed abstract class Schema

final case class DbConfig(dbConn: DbConnection, schema: Schema)

object DbConfig {
  implicit val dbConfigHasDbConfig: HasDbConfig[DbConfig] =
    new HasDbConfig[DbConfig] {
      val dbConfig: Lens[DbConfig, DbConfig] = Lens.id
    }
}

trait HasDbConfig[A] {
  def dbConfig: Lens[A, DbConfig]

  val dbConn: Lens[A, DbConnection] = dbConfig.composeLens(GenLens[DbConfig](_.dbConn))
  val dbSchema: Lens[A, Schema] = dbConfig.composeLens(GenLens[DbConfig](_.schema))
}
