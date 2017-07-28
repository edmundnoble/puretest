package puretest

final case class Test[A](name: String, run: A)

final case class TestSuite[T](tests: List[T])

final case class TestRoot[S](suites: List[S])

