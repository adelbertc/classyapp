package classyapp

import monocle.Prism
import monocle.macros.{GenIso, GenPrism}

sealed abstract class AppError

object AppError {
  final case class AppDbError(dbError: DbError) extends AppError
  final case class AppNetError(networkError: NetworkError) extends AppError

  implicit val appErrorAsDbError: AsDbError[AppError] =
    new AsDbError[AppError] {
      val dbError: Prism[AppError, DbError] =
        GenPrism[AppError, AppError.AppDbError].
        composeIso(GenIso[AppError.AppDbError, DbError])
    }

  implicit val appErrorAsNetworkError: AsNetworkError[AppError] =
    new AsNetworkError[AppError] {
      val networkError: Prism[AppError, NetworkError] =
        GenPrism[AppError, AppError.AppNetError].
        composeIso(GenIso[AppError.AppNetError, NetworkError])
    }

}
