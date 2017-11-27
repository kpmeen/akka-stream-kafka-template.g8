logLevel := Level.Warn

resolvers ++= Seq(
  Resolver.bintrayIvyRepo("sbt", "sbt-plugin-releases"),
  Resolver.typesafeRepo("releases"),
  // Remove below resolver once the following issues has been resolved:
  // https://issues.jboss.org/projects/JBINTER/issues/JBINTER-21
  "JBoss" at "https://repository.jboss.org/"
)

// Dependency handling
addSbtPlugin("io.get-coursier"  %% "sbt-coursier" % "1.0.0-RC13")
addSbtPlugin("com.timushev.sbt" %% "sbt-updates"  % "0.3.3")

// Style formatting
addSbtPlugin("com.geirsson"   %% "sbt-scalafmt"          % "1.3.0")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// Test coverage
addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "1.5.1")

// Packaging and release
addSbtPlugin("com.typesafe.sbt"  %% "sbt-native-packager" % "1.3.1")
