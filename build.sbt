name := "cats-for-fun"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.12"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",   // source files are in UTF-8
  "-deprecation",         // warn about use of deprecated APIs
  "-unchecked",           // warn about unchecked type parameters
  "-feature",             // warn about misused language features
  "-language:higherKinds",// allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint",               // enable handy linter warnings
//  "-Xfatal-warnings"     // turn compiler warnings into errors
  "-Ypartial-unification" // allow the compiler to unify type constructors of different arities
)


libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.9.2",
  "org.typelevel" %% "cats-core" % "1.0.0-MF",
  "com.twitter" %% "scrooge-core" % "17.11.0" exclude("com.twitter", "libthrift"),
  "com.twitter" %% "finagle-thrift" % "17.11.0" exclude("com.twitter", "libthrift")
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")