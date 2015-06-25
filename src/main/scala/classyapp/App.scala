package classyapp

import scalaz.{DisjunctionT, Kleisli, MonadError, MonadReader}
import scalaz.effect.{IO, LiftIO, MonadIO}
import scalaz.syntax.bind._

sealed abstract class MyData

object App extends Instances {
  def loadFromDb[M[_, _, _], E, R](implicit
                                   ME: MonadError[M[?, R, ?], E],
                                   MR: MonadReader[M[E, ?, ?], R],
                                   E: AsDbError[E],
                                   R: HasDbConfig[R],
                                   M: MonadIO[M[E, R, ?]]): M[E, R, MyData] = ???

  def sendOverNet[M[_, _, _], E, R](data: MyData)(implicit
                                                  ME: MonadError[M[?, R, ?], E],
                                                  MR: MonadReader[M[E, ?, ?], R],
                                                  E: AsNetworkError[E],
                                                  R: HasNetworkConfig[R],
                                                  M: MonadIO[M[E, R, ?]]): M[E, R, Unit] = ???

  def loadAndSend[M[_, _, _], E, R](implicit
                                    ME: MonadError[M[?, R, ?], E],
                                    MR: MonadReader[M[E, ?, ?], R],
                                    E0: AsDbError[E],
                                    E1: AsNetworkError[E],
                                    R0: HasDbConfig[R],
                                    R1: HasNetworkConfig[R],
                                    M: MonadIO[M[E, R, ?]]): M[E, R, Unit] =
    M.bind(loadFromDb[M, E, R])(sendOverNet[M, E, R])

  val app = loadAndSend[App, AppError, AppConfig]
}

sealed abstract class Instances {
  type DisjunctionIO[E, A] = DisjunctionT[IO, E, A]
  type App[E, R, A] = Kleisli[DisjunctionIO[E, ?], R, A]

  def app[E, R, A](f: R => DisjunctionIO[E, A]): App[E, R, A] =
    Kleisli.kleisli[DisjunctionIO[E, ?], R, A](f)

  implicit def monadError[E, R]: MonadError[App[?, R, ?], E] =
    new MonadError[App[?, R, ?], E] {
      def bind[A, B](fa: App[E, R, A])(f: A => App[E, R, B]): App[E, R, B] =
        fa.flatMap(f)

      def point[A](a: => A): App[E, R, A] =
        app(Function.const(DisjunctionT.right(IO(a))))

      def handleError[A](fa: App[E, R, A])(f: E => App[E, R, A]): App[E, R, A] =
        app { r =>
          DisjunctionT.eitherTMonadError[IO, E].handleError(fa.run(r))(e => f(e).run(r))
        }

      def raiseError[A](e: E): App[E, R, A] =
        app(Function.const(DisjunctionT.left(IO(e))))
    }

  implicit def monadReader[E, R]: MonadReader[App[E, ?, ?], R] = Kleisli.kleisliMonadReader[DisjunctionIO[E, ?], R]

  implicit def monadIO[E, R]: MonadIO[App[E, R, ?]] = MonadIO.kleisliMonadIO[DisjunctionIO[E, ?], R]
}
