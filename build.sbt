enablePlugins(GatlingPlugin)

name := "GatlingPerf2018"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.0",
  "io.gatling"            % "gatling-test-framework"    % "2.3.0",
  "mysql"                 % "mysql-connector-java"      % "6.0.6",
  "org.postgresql"        % "postgresql"                % "42.2.2",
  //json4s
  "org.json4s"            % "json4s-jackson_2.12"       % "3.5.3",
  "org.json4s"            %% "json4s-ext"               % "3.5.3"
)