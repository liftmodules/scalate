Scalate Lift Module
==================

[![Join the chat at https://gitter.im/liftmodules/scalate](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/liftmodules/scalate?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This module provides integration with Scalate.

To include this module in your Lift project, update your `libraryDependencies` in `build.sbt` to include:

*Lift 2.6* for Scala 2.11:

    "net.liftmodules" %% "scalate_2.6" % "1.4-SNAPSHOT"

*Lift 2.6.x* for Scala 2.9 and 2.10:

    "net.liftmodules" %% "scalate_2.6" % "1.3"

*Lift 2.5.x* for Scala 2.9 and 2.10:

    "net.liftmodules" %% "scalate_2.5" % "1.3"

*Lift 3.0.x* for Scala 2.10:

    "net.liftmodules" %% "scalate_3.0" % "1.3-SNAPSHOT"

Documentation
=============

* [Using Scalate with Lift](http://scalate.fusesource.org/documentation/lift.html).

* [Example project using Scalate](https://github.com/lift/lift/tree/master/examples/helloscalate).

**Note:** The module package changed from `net.liftweb.scalate` to `net.liftmodules.scalate` in May 2012.  Please consider this when referencing documentation written before that date.


Notes for module developers
===========================

* The [Jenkins build](https://liftmodules.ci.cloudbees.com/job/scalate/) is triggered on a push to master.



