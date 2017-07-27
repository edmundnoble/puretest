import sbt.{Def, _}
import Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {

  //noinspection TypeAnnotation
  object Versions {
    val scalaCheck = "1.13.4"
    val macroParadise = "2.1.0"
    val kindProjector = "0.9.3"
    val simulacrum = "0.10.0"
    val machinist = "0.6.1"
    val cats = "0.9.0"
    val shapeless = "2.3.2"
  }

  val acyclic = Def.settings(
    libraryDependencies += "com.lihaoyi" %% "acyclic" % "0.1.7" % "provided",
    autoCompilerPlugins := true,
    addCompilerPlugin("com.lihaoyi" %% "acyclic" % "0.1.7"),
    scalacOptions += "-P:acyclic:force"
  )

  val scalaCheck: Seq[Setting[_]] = Def.settings(libraryDependencies ++= Seq(
    "org.scalacheck" %%% "scalacheck" % Versions.scalaCheck
  ))

  val compilerPlugins: Seq[Setting[_]] = Def.settings(libraryDependencies ++= Seq(
    compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.patch),
    compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
  ))

  val catsBundle: Seq[Setting[_]] = Def.settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats" % Versions.cats
  ))

  val catsCore: Seq[Setting[_]] = Def.settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core" % Versions.cats
  ))

  val shapeless: Seq[Setting[_]] = Def.settings(libraryDependencies ++= Seq(
    "com.chuusai" %%% "shapeless" % Versions.shapeless
  ))

  val simulacrumAndMachinist: Seq[Setting[_]] = Def.settings(libraryDependencies ++= Seq(
    "com.github.mpilquist" %%% "simulacrum" % Versions.simulacrum,
    "org.typelevel" %%% "machinist" % Versions.machinist
  ))

  val sonatypeResolvers: Seq[Setting[_]] = Def.settings(resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ))

  val bintrayResolvers: Seq[Setting[_]] = Def.settings(
    resolvers += "bintray/non" at "http://dl.bintray.com/non/maven"
  )

}
