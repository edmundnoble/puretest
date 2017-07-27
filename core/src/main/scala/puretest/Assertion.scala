package puretest

import cats.data.Reader
import fansi.Str
import pprint.PPrinter

trait Assertion[A, S] {
  def assert(a: A): Option[S]
}

object Assertion {

  trait ContextI {
    type F[_]

    def assertion[A: Assertion[?, String]]: Assertion[F[A], String]
  }

  val ContextImpl: ContextI = new ContextI {
    type F[A] = A

    def assertion[A](implicit under: Assertion[A, String]): Assertion[A, String] =
      (a: A) => under.assert(a).map { error =>
        ???
      }
  }
  type Context[A] = ContextImpl.F[A]

  implicit def contextAssert[A: Assertion[?, String]]: Assertion[Context[A], String] = ContextImpl.assertion[A]

  def context(error: String, tag: String)
             (implicit line: sourcecode.Line, enclosing: sourcecode.Enclosing): Reader[PPrinter, Str] = {
    Reader { pprinter: PPrinter =>
      def joinSeq[T](seq: Seq[T], sep: T): Seq[T] = {
        seq.flatMap(x => Seq(x, sep)).dropRight(1)
      }

      val enclosingWithoutAnonfuns =
        enclosing.value.split(' ').filter(_ != "$anonfun").mkString(" ")
      val coloredEnclosing = joinSeq(
        enclosingWithoutAnonfuns.split('.').map(x =>
          joinSeq(x.split('#').map(fansi.Color.Magenta(_)), fansi.Str("#"))
        ),
        Seq(fansi.Str("."))
      ).flatten

      val tagStrs =
        if (tag.isEmpty) Seq()
        else Seq(fansi.Color.Cyan(tag), fansi.Str(" "))

      val prefix = coloredEnclosing ++ Seq(
        fansi.Str(":"),
        fansi.Color.Green(line.value.toString),
        fansi.Str(": ")
      ) ++ tagStrs
      fansi.Str.join(prefix ++
        pprinter.tokenize(error, pprinter.defaultWidth, pprinter.defaultHeight, pprinter.defaultIndent).toSeq: _*
      )
    }
  }
}