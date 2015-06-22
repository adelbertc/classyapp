package classyapp

import monocle.Lens
import monocle.macros.GenLens

final case class AppConfig(appDbConfig: DbConfig, appNetConfig: NetworkConfig)

object AppConfig {
  implicit val appConfigHasDbConfig: HasDbConfig[AppConfig] =
    new HasDbConfig[AppConfig] {
      val dbConfig: Lens[AppConfig, DbConfig] = GenLens[AppConfig](_.appDbConfig)
    }

  implicit val appConfigHasNetworkConfig: HasNetworkConfig[AppConfig] =
    new HasNetworkConfig[AppConfig] {
      val netConfig: Lens[AppConfig, NetworkConfig] = GenLens[AppConfig](_.appNetConfig)
    }
}
