package classyapp

import monocle.{Iso, Prism}
import monocle.macros.{GenIso, GenPrism}

sealed abstract class NetworkError

object NetworkError {
  final case class Timeout(timeout: Int) extends NetworkError
  final case object ServerOnFire extends NetworkError

  implicit val networkErrorAsNetworkError: AsNetworkError[NetworkError] =
    new AsNetworkError[NetworkError] {
      val networkError: Prism[NetworkError, NetworkError] = Prism.id
    }
}

trait AsNetworkError[A] {
  def networkError: Prism[A, NetworkError]

  val timeout: Prism[A, Int] =
    networkError.
    composePrism(GenPrism[NetworkError, NetworkError.Timeout]).
    composeIso(GenIso[NetworkError.Timeout, Int])

  val invalidConnection: Prism[A, Unit] =
    networkError.
    composePrism(GenPrism[NetworkError, NetworkError.ServerOnFire.type]).
    composeIso(Iso[NetworkError.ServerOnFire.type, Unit](Function.const(()))(Function.const(NetworkError.ServerOnFire)))
}
