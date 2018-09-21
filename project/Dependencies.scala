import sbt._

object dependencies {
  object cats {
    val version = "1.4.0"
    val org = "org.typelevel"

    val core = org %% "cats-core" % version
    val testkit = org %% "cats-testkit" % version
  }

  val droste = "io.higherkindness" %% "droste-core" % "0.5.0"
}
