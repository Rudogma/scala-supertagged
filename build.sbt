import sbt._

import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

releaseCrossBuild := true
publishMavenStyle := true
pomIncludeRepository := (_ => false)
releasePublishArtifactsAction := PgpKeys.publishSigned.value

parallelExecution in ThisBuild := false

lazy val defaultSettings =
  Project.defaultSettings ++
    Compiler.defaultSettings ++
    Publish.defaultSettings ++
    Tests.defaultSettings ++
    Console.defaultSettings

lazy val supertagged = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(defaultSettings: _*)
  .jsSettings(
    crossScalaVersions := Versions.ScalaCross,
    parallelExecution in Test := false
  )

lazy val root = project.in(file("."))
  .settings(defaultSettings: _*)
  .settings(
    name := "supertagged",
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )
  .aggregate(supertaggedJVM, supertaggedJS)

lazy val supertaggedJVM = supertagged.jvm
lazy val supertaggedJS = supertagged.js
lazy val supertaggedNative = supertagged.native.settings(Project.moduleNativeSettings  : _*)
