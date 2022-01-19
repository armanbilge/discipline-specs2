ThisBuild / tlBaseVersion := "1.4"

ThisBuild / developers := List(
  tlGitHubDev("larsrh", "Lars Hupel"),
  tlGitHubDev("travisbrown", "Travis Brown"),
  tlGitHubDev("ChristopherDavenport", "Christopher Davenport"),
  tlGitHubDev("djspiewak", "Daniel Spiewak"),
  Developer("vasilmkd", "Vasil Vasilev", "vasil@vasilev.io", url("https://github.com/vasilmkd"))
)

val Scala213 = "2.13.8"

ThisBuild / crossScalaVersions := Seq("3.0.2", "2.12.15", Scala213)

ThisBuild / licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / startYear := Some(2019)
ThisBuild / tlSiteApiUrl := Some(
  url("https://www.javadoc.io/doc/org.typelevel/discipline-specs2_2.13"))

val disciplineV = "1.4.0"
val specs2V = "4.13.2"
val macrotaskExecutorV = "1.0.0"

lazy val root = tlCrossRootProject.aggregate(core)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    name := "discipline-specs2",
    libraryDependencies += "org.typelevel" %%% "discipline-core" % disciplineV,
    Compile / doc / sources := {
      val old = (Compile / doc / sources).value
      if (tlIsScala3.value) Seq() else old
    }
  )
  .jvmSettings(
    libraryDependencies += {
      if (tlIsScala3.value)
        ("org.specs2" %%% "specs2-scalacheck" % specs2V)
          .cross(CrossVersion.for3Use2_13)
          .exclude("org.scalacheck", "scalacheck_2.13")
      else
        "org.specs2" %%% "specs2-scalacheck" % specs2V
    }
  )
  .jsSettings(
    libraryDependencies += {
      if (tlIsScala3.value)
        ("org.specs2" %%% "specs2-scalacheck" % specs2V)
          .cross(CrossVersion.for3Use2_13)
          .exclude("org.scalacheck", "scalacheck_sjs1_2.13")
          .exclude("org.scala-js", "scala-js-macrotask-executor_sjs1_2.13")
      else
        "org.specs2" %%% "specs2-scalacheck" % specs2V
    },
    libraryDependencies += "org.scala-js" %%% "scala-js-macrotask-executor" % macrotaskExecutorV
  )

lazy val docs = project.in(file("site")).enablePlugins(TypelevelSitePlugin).dependsOn(core.jvm)
