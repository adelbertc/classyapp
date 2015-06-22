name := "classyapp"

scalaVersion := "2.10.5"

licenses += ("BSD 2-Clause", url("http://opensource.org/licenses/BSD-2-Clause"))

resolvers += "bintray/non" at "http://dl.bintray.com/non/maven"

val monocleVersion = "1.1.1"

val scalazVersion = "7.1.3"

libraryDependencies ++= List(
  compilerPlugin("org.spire-math" %% "kind-projector" % "0.6.0"),

  "com.github.julien-truffaut"  %% "monocle-core"               % monocleVersion,
  "com.github.julien-truffaut"  %% "monocle-macro"              % monocleVersion,
  "org.scalaz"                  %% "scalaz-core"                % scalazVersion,
  "org.scalaz"                  %% "scalaz-effect"              % scalazVersion
)

scalacOptions ++= List(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)
