package net.liftmodules.scalate

import net.liftweb.http.LiftRules
import net.liftweb.util.NamedPF
import net.liftweb.http.Req
import net.liftweb.http.GetRequest

/**
 * The pure scalate engine, which technically supports .scaml or .ssp files. No Lift processing happens
 * on these files.
 */
object PureScaml {
  def init = {
    val renderer = new ScalateView
    LiftRules.dispatch.prepend(NamedPF("Scalate Dispatch") {
      case Req(path, ext, GetRequest) if (renderer.canRender(path, ext)) => renderer.render(path, ext)
    })
  }
}