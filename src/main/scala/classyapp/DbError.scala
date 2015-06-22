package classyapp

import monocle.{Iso, Prism}
import monocle.macros.{GenIso, GenPrism}

sealed abstract class DbError

object DbError {
  final case class QueryError(error: String) extends DbError
  final case object InvalidConnection extends DbError

  implicit val dbErrorAsDbError: AsDbError[DbError] =
    new AsDbError[DbError] {
      val dbError: Prism[DbError, DbError] = Prism.id
    }
}

trait AsDbError[A] {
  def dbError: Prism[A, DbError]

  val queryError: Prism[A, String] =
    dbError.
    composePrism(GenPrism[DbError, DbError.QueryError]).
    composeIso(GenIso[DbError.QueryError, String])

  val invalidConnection: Prism[A, Unit] =
    dbError.
    composePrism(GenPrism[DbError, DbError.InvalidConnection.type]).
    composeIso(Iso[DbError.InvalidConnection.type, Unit](Function.const(()))(Function.const(DbError.InvalidConnection)))
}
