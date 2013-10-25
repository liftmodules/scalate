name := "scalate"

organization := "net.liftmodules"

version := "1.4-SNAPSHOT"

liftVersion <<= liftVersion ?? "2.6-SNAPSHOT"

liftEdition <<= liftVersion apply { _.substring(0,3) }

moduleName <<= (name, liftEdition) { (n, e) =>  n + "_" + e }

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation")

crossScalaVersions := Seq("2.10.0", "2.9.2", "2.9.1-1", "2.9.1")

resolvers += "CB Central Mirror" at "http://repo.cloudbees.com/content/groups/public"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

libraryDependencies <++= liftVersion { v =>
  "net.liftweb" %% "lift-webkit" % v % "provided" ::
  Nil
}

libraryDependencies <++= scalaVersion { sv =>
  "javax.servlet" % "servlet-api" % "2.5" % "provided" ::
  (sv match {
	 case "2.9.2" | "2.9.1" | "2.9.1-1" => "org.specs2" %% "specs2" % "1.12.3" % "test"
	 case "2.10.0" => "org.specs2" %% "specs2" % "1.13" % "test"
      })  ::
   (sv match {
      case "2.10.0"  => "org.fusesource.scalate" %% "scalate-core" % "1.6.1"
      case _ => "org.fusesource.scalate" % "scalate-core" % "1.5.3"
      })  ::
   (sv match {
      case "2.10.0"  => "org.fusesource.scalamd" %% "scalamd"  % "1.6"
      case _ => "org.fusesource.scalamd" % "scalamd"  % "1.5"
      })  ::
  Nil
}


publishTo <<= version { _.endsWith("SNAPSHOT") match {
 	case true  => Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
 	case false => Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
  }
 }


// For local deployment:

credentials += Credentials( file("sonatype.credentials") )

// For the build server:

credentials += Credentials( file("/private/liftmodules/sonatype.credentials") )

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }


pomExtra := (
	<url>https://github.com/liftmodules/scalate</url>
	<licenses>
		<license>
	      <name>Apache 2.0 License</name>
	      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
	      <distribution>repo</distribution>
	    </license>
	 </licenses>
	 <scm>
	    <url>git@github.com:liftmodules/scalate.git</url>
	    <connection>scm:git:git@github.com:liftmodules/scalate.git</connection>
	 </scm>
	 <developers>
	    <developer>
	      <id>liftmodules</id>
	      <name>Lift Team</name>
	      <url>http://www.liftmodules.net</url>
	 	</developer>
	 </developers>
 )

