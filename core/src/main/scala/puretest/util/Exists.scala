package puretest.util

trait ExistsInt {
  type T1[F[_]] <: Any {type T}

  final def apply[F[_], A](fa: F[A]): T1[F] = wrap[F, A](fa)

  def wrap[F[_], A](fa: F[A]): T1[F]

  def unwrap[F[_]](fa: T1[F]): F[fa.T]

  def unwrapE[F[_]](tf: T1[F]): F[T1] forSome {type T1}

  def wrapE[F[_]](tf: F[T1] forSome {type T1}): T1[F]

  def mapK[F[_], G[_]](tf: T1[F])(fg: F ~> G): T1[G]

  def fromInstance[F[_]](instance: Instance[F]): T1[λ[X => (X, F[X])]]
}

final class ExistsImpl extends ExistsInt {
  type L1[F[_], A] = F[A] {type T = A}

  type T1[F[_]] = L1[F, _]

  def wrap[F[_], A](fa: F[A]): T1[F] = fa.asInstanceOf[T1[F]]

  def unwrap[F[_]](fa: T1[F]): F[fa.T] = fa.asInstanceOf[F[fa.T]]

  def unwrapE[F[_]](tf: T1[F]): F[T1] forSome {type T1} = unwrap[F](tf)

  def wrapE[F[_]](tf: F[T1] forSome {type T1}): T1[F] = tf.asInstanceOf[T1[F]]

  def mapK[F[_], G[_]](tf: T1[F])(fg: F ~> G): T1[G] = {
    fg.apply(tf.asInstanceOf[F[:?:]]).asInstanceOf[T1[G]]
  }

  def fromInstance[F[_]](instance: Instance[F]): T1[λ[X => (X, F[X])]] = {
    apply[λ[X => (X, F[X])], instance.Type]((instance.first, instance.second))
  }
}

object ExistsSyntax {

  implicit final class Exists1Syntax[F[_], A](val fa: Exists[F] {type T = A}) extends AnyVal {
    def value: F[A] = Exists.unwrap[F](fa)

    def mapK[G[_]](fg: F ~> G): Exists[G] = Exists.mapK[F, G](fa)(fg)

    def toScala: F[A] forSome {type A} = Exists.unwrap[F](fa)
  }

}

trait ExistsSyntax {

  import ExistsSyntax._

  implicit final def toExists1Syntax[F[_], A]
  (fa: Exists[F] {type T = A}): Exists1Syntax[F, A] = {
    new Exists1Syntax[F, A](fa)
  }
}
