case class Test()

case class TestSuite (tests: List[Test])

case class TestRoot(suites: List[TestSuite])
