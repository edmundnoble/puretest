package puretest

import puretest.util.Instance

trait Assertion[A, S] {
  def assert(a: A): Option[S]
}

object Assertion {
  def assert[A, S](a: A)(implicit ev: Assertion[A, S]): Instance[Assertion[?, S]] =
    Instance.capture[Assertion[?, S], A](a)(ev)
}

