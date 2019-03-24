val ChimneyVersion = "0.3.1"
val CirceFs2Version = "0.11.0"
val CirceVersion = "0.11.1"
val CirisVersion = "0.12.1"
val DoobieVersion = "0.6.0"
val Http4sVersion = "0.20.0-M6"
val LogbackVersion = "1.2.3"
val PureConfigVersion = "0.10.2"
val RefinedVersion = "0.9.4"
val Specs2Version = "4.1.0"

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
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.github.pureconfig" %% "pureconfig" % PureConfigVersion,
      "eu.timepit" %% "refined" % RefinedVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-fs2" % CirceFs2Version,
      "io.circe" %% "circe-generic-extras" % CirceVersion,
      "io.scalaland" %% "chimney" % ChimneyVersion,
      "is.cir" %% "ciris-cats" % CirisVersion,
      "is.cir" %% "ciris-cats-effect" % CirisVersion,
      "is.cir" %% "ciris-core" % CirisVersion,
      "is.cir" %% "ciris-refined" % CirisVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-refined" % DoobieVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test"
    ),
    addCompilerPlugin(
      "org.spire-math" %% "kind-projector" % KindProjectorVersion
    ),
    addCompilerPlugin(
      "com.olegpy" %% "better-monadic-for" % BetterMonadicForVersion
    )
  )
