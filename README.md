# Scalate Lift Module

This module provides integration with Scalate in two ways: Lifted Scaml (in
2.0+), and Pure Scaml.

See Also: [Sample project using Pure/Lifted Scaml](https://github.com/awkay/scalate-demo).

---

**Note:** The module package changed from `net.liftweb.scalate` to `net.liftmodules.scalate` in May 2012.  Please consider this when referencing documentation written before that date.

---

# Synopsis

In ``Boot.scala`` (You may enable either mode, or both):

    LifedScaml.init // Turn on Lifted Scaml support. Templates end in .l.scaml
    PureScaml.init // Turn on Pure Scaml support. Templates end in just .scaml

In .l.scaml files:

    %div(class="lift:Demo1.currentTime")
      %p The time is
        %span.time 3:00am

In .scaml files:

    %div
      %p The time is
        %span.time 
          = java.util.Date()

**The file suffix determines the processing mode**. The following
suffixes have the following meanings:

- .scaml - Pure Scaml (no Lift pipeline)
- .l.scaml - Lifted Scaml (``scaml (once) -> HTML -> Lift``)
- .html - Regular Lift processing (template content is  HTML5/XHTML)

It is possible to turn the .l.scaml/.scaml modes on separately, and it is also
possible to redefine the suffix that triggers the given method of processing.

# Description

## Lifted Scaml (.l.scaml files)

If enabled, files with this suffix are used purely as a templates for Lift. The
rendering pipeline looks like this:

    HTTP request --> There is an .l.scaml file --> Lift externalTemplateResolver
    --> Scaml to HTML/XHTML (cached in production) --> Lift 

This means that you are using scaml to make it cleaner to write your semantic
HTML. You can use this with both of Lift's primary template engines 
(designer-friendly templates or XHTML), so you must ensure that your scaml 
generates valid input to Lift.

**IMPORTANT**: In development mode, scalate will run the new template every
time, so that:

    %p 
      The time is
      %b
        = java.util.Date().toString()

will generate a page with the correct time on every load. However, this
is unlikely to be what you want in 
production mode, since that means that two rendering pipelines would be 
running for every page load. So, in production mode, it will cache the scalate
result for performance, and you will see the time that the page first loaded.

### The Pros:

- You can generate clean semantic HTML with scaml
- You can use side-effect-free Scala in your template
- You get to use Lift for the heavy lifting
- In production mode, it runs with normal efficiency
- In development mode, you still get edit/save/reload behavior

### The Cons:

- You cannot embed scala code in the template that has/requires side-effects,
  since it will only run once when in production mode. 

## Pure Scalate (MVC-style)

In this mode (which until recently was the only mode), .scaml files are
processed by Scalate **ONLY** (Scalate generates the LiftResponse via
early dispatch). Thus, **the entire Lift pipeline is bypassed**, and it looks
like this:

    HTTP request --> .scaml file --> Scalate --> Response

There are some cases where this can be desirable, especially on pages where you
don't need AJAX, Comet, Wiring, or any of the other Lift goodies.

### The Pros:

- You can use all Scalate features (including side-effect driven scala) in your
  scaml files.
- The rendering pipeline is shorter, and thus possibly easier on the CPU

### The Cons:

- You get **none** of the features that originally attracted you to Lift in 
  the first place. No wiring, no AJAX handling, no Comet.

The following example project uses this mode, and is at:

* [Example project using Scalate](https://github.com/lift/lift/tree/master/examples/helloscalate).

---

# Notes for module developers

* The [Jenkins build](https://liftmodules.ci.cloudbees.com/job/scalate/) is triggered on a push to master.



