package classyapp

import monocle.Lens
import monocle.macros.GenLens

sealed abstract class Port

sealed abstract class Ssl

final case class NetworkConfig(port: Port, ssl: Ssl)

object NetworkConfig {
  implicit val networkConfigHasNetworkConfig: HasNetworkConfig[NetworkConfig] =
    new HasNetworkConfig[NetworkConfig] {
      val netConfig: Lens[NetworkConfig, NetworkConfig] = Lens.id
    }
}

trait HasNetworkConfig[A] {
  def netConfig: Lens[A, NetworkConfig]

  val netPort: Lens[A, Port] = netConfig.composeLens(GenLens[NetworkConfig](_.port))
  val netSsl: Lens[A, Ssl] = netConfig.composeLens(GenLens[NetworkConfig](_.ssl))
}
