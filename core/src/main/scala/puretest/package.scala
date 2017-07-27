package object puretest {
  val Exists: ExistsInt = new ExistsImpl
  val ∃ : ExistsInt = Exists
  val Forall: ForallInt = new ForallImpl
  val ∀ : ForallInt = Forall
  val Unknown: UnknownInt = new UnknownImpl

  type Forall[F[_]] = Forall.T1[F]
  type ∀[F[_]] = Forall[F]
  type Exists[F[_]] = Exists.T1[F]
  type ∃[F[_]] = Exists[F]
  type ~>[A[_], B[_]] = FunctionK[A, B]

  type Unk = Unknown.T
  type Unk1 = Unknown.T1
  type UnkK[A] = Unknown.K[A]
  type :?: = Unk
  type :??:[A] = UnkK[A]
}
