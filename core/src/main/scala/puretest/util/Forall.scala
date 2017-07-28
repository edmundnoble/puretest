package puretest.util

trait ForallInt { f =>
  type T1[F[_]]

  def apply[F[_]](fa: F[:?:]): T1[F] = make[F](fa)

  def make[F[_]](fa: F[:?:]): T1[F]

  def run1[F[_], A](tf: T1[F]): F[A]

  def mapK[F[_], G[_]](fa: T1[F])(fg: F ~> G): T1[G]

  def lift[F[_], G[_]](fa: T1[F]): T1[λ[α => F[G[α]]]]

  def toFunctionK[F[_], G[_]](tf: T1[F]): G ~> F
}

class ForallImpl extends ForallInt {
  final type T1[F[_]] = F[:?:]

  final def make[F[_]](fa: F[:?:]): T1[F] = fa

  final def run1[F[_], A](tf: T1[F]): F[A] = tf.asInstanceOf[F[A]]

  final def mapK[F[_], G[_]](tf: T1[F])(fg: F ~> G): T1[G] = fg.apply(tf)

  final def lift[F[_], G[_]](fa: T1[F]): T1[λ[α => F[G[α]]]] = fa.asInstanceOf[F[G[:?:]]]

  final def toFunctionK[F[_], G[_]](tf: T1[F]): G ~> F = _ => run1[F, :?:](tf)
}

object ForallSyntax {
  final class Forall1Syntax[F[_]](val f: Forall[F]) extends AnyVal {
    def run[A]: F[A]                        = Forall.run1[F, A](f)
    def mapK[G[_]](fg: F ~> G): Forall[G]   = Forall.mapK[F, G](f)(fg)
    def lift[G[_]]: Forall[λ[α => F[G[α]]]] = Forall.lift[F, G](f)
    def toFunctionK[G[_]]:  G ~> F          = Forall.toFunctionK[F, G](f)
  }
}
trait ForallSyntax {
  import ForallSyntax._
  implicit final def toForall1Syntax[F[_]](fa: ∀[F]): Forall1Syntax[F] = new Forall1Syntax[F](fa)
}
