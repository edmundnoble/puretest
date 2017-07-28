// project info

organization in ThisBuild := "puretest"

// aliases

addCommandAlias("buildJVM", "puretestJVM/test")

addCommandAlias("validateJVM", ";scalastyle;scalafmt::test;test:scalafmt::test;buildJVM;mimaReportBinaryIssues;makeMicrosite")

addCommandAlias("validateJS", ";puretestJS/compile;testsJS/test;js/test")

addCommandAlias("validate", ";clean;validateJS;validateJVM")

// projects

val core = crossProject.crossType(CrossType.Pure)
  .settings(moduleName := "puretest-core", name := "puretest core")
  .settings(Settings.coreSettings:_*)
  .settings(Settings.includeGeneratedSrc)
  .settings(Dependencies.sourcecode)
  .settings(Dependencies.pprint)
  .settings(Dependencies.catsCore)
  .settings(Dependencies.catsEffect:_*)
  .settings(Dependencies.scalaCheck)
  .configureCross(Coverage.disableScoverage210Jvm)
  .configureCross(Coverage.disableScoverage210Js)
  .jsSettings(Settings.commonJsSettings:_*)
  .jvmSettings(Settings.commonJvmSettings:_*)

val coreJVM = core.jvm
val coreJS = core.js

val docs = project
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(moduleName := "puretest-docs")
  .settings(Settings.coreSettings)
  .settings(Publishing.noPublishSettings)
  .settings(ghpages.settings)
  .settings(Docs.docSettings)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
  .settings(Settings.commonJvmSettings)
  .dependsOn(coreJVM)

val laws = crossProject.crossType(CrossType.Pure)
  .dependsOn(core)
  .settings(moduleName := "puretest-laws", name := "puretest laws")
  .settings(Settings.coreSettings:_*)
  .settings(Dependencies.catsBundle:_*)
  .settings(Dependencies.catsEffect:_*)
  .configureCross(Coverage.disableScoverage210Jvm)
  .jsSettings(Settings.commonJsSettings:_*)
  .jvmSettings(Settings.commonJvmSettings:_*)
  .jsSettings(coverageEnabled := false)

val lawsJVM = laws.jvm
val lawsJS = laws.js

val tests = crossProject.crossType(CrossType.Pure)
  .dependsOn(core, laws)
  .settings(moduleName := "puretest-tests")
  .settings(Settings.coreSettings:_*)
  .settings(Dependencies.catsBundle:_*)
  .settings(Dependencies.catsEffect:_*)
  .settings(Dependencies.scalaCheck:_*)
  .settings(Publishing.noPublishSettings:_*)
  .jsSettings(Settings.commonJsSettings:_*)
  .jvmSettings(Settings.commonJvmSettings:_*)

val testsJVM = tests.jvm
val testsJS = tests.js

// puretest-js is JS-only
val js = project
  .dependsOn(coreJS, testsJS % "test-internal -> test")
  .settings(moduleName := "puretest-js")
  .settings(Settings.coreSettings:_*)
  .settings(Settings.commonJsSettings:_*)
  .configure(Coverage.disableScoverage210Js)
  .enablePlugins(ScalaJSPlugin)

// puretest-jvm is JVM-only
val jvm = project
  .dependsOn(coreJVM, testsJVM % "test-internal -> test")
  .settings(moduleName := "puretest-jvm")
  .settings(Settings.coreSettings:_*)
  .settings(Settings.commonJvmSettings:_*)

val puretestJVM = project.in(file(".puretestJVM"))
  .settings(moduleName := "puretest")
  .settings(Settings.coreSettings)
  .settings(Settings.commonJvmSettings)
  .aggregate(coreJVM, lawsJVM, testsJVM, jvm, docs)
  .dependsOn(coreJVM, lawsJVM, testsJVM % "test-internal -> test", jvm)

val puretestJS = project.in(file(".puretestJS"))
  .settings(moduleName := "puretest")
  .settings(Settings.coreSettings)
  .settings(Settings.commonJsSettings)
  .aggregate(coreJS, lawsJS, testsJS, js)
  .dependsOn(coreJS, lawsJS, testsJS % "test-internal -> test", js)
  .enablePlugins(ScalaJSPlugin)

val puretest = project.in(file("."))
  .settings(moduleName := "root")
  .settings(Settings.coreSettings)
  .settings(Publishing.noPublishSettings)
  .aggregate(puretestJVM, puretestJS)
  .dependsOn(puretestJVM, puretestJS, testsJVM % "test-internal -> test")

