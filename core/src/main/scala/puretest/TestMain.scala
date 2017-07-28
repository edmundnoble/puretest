package puretest

import cats.effect.Effect
import cats.mtl.ApplicativeAsk

abstract class TestMain[F[_]: Effect, S] {
  val testRoot: TestRoot[S]

  final def main(args: Array[String]): Unit = {
  }

}
