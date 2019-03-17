val Http4sVersion = "0.20.0-M6"
val Specs2Version = "4.1.0"
val LogbackVersion = "1.2.3"
val PureConfigVersion = "0.10.2"

//Compiler plugins
val KindProjectorVersion = "0.9.6"
val BetterMonadicForVersion = "0.2.4"

lazy val root = (project in file("."))
  .settings(
    organization := "io.abp",
    name := "finance-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig" % PureConfigVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    ),
    addCompilerPlugin(
      "org.spire-math" %% "kind-projector" % KindProjectorVersion
    ),
    addCompilerPlugin(
      "com.olegpy" %% "better-monadic-for" % BetterMonadicForVersion
    )
  )
