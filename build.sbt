name := "import-user-log-sample"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.11.12"
organization := "sparksample"
scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature")
updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.7",
  "org.apache.spark" %% "spark-sql" % "2.4.7",
  "org.apache.spark" %% "spark-streaming" % "2.4.7",
  "org.apache.kudu"  %% "kudu-spark2" % "1.13.0",
)

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "mailcap" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".jnilib" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".so" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}


