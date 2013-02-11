/**
 * Lifted Scaml mode
 *
 * Author: Tony Kay <tony.kay@gmail.com>
 */
package net.liftmodules.scalate

import net.liftweb.http.LiftRules
import java.util.Locale
import net.liftweb.common.Full
import net.liftweb.common.Box
import scala.xml.NodeSeq
import java.io.InputStream
import net.liftweb.http.S
import net.liftweb.common.Loggable
import net.liftweb.common.Empty
import java.io.StringReader
import java.io.InputStreamReader
import java.io.ByteArrayInputStream
import net.liftweb.util.NoCache

/**
 * LiftedScaml template mode.
 *
 * This mode allows you to write files in scaml which are then pre-processed into Lift templates.
 *
 * **IMPORTANT**: In development mode, the scaml will be run every time; however, in production mode
 * the scaml will only every be processed once, so it must be side-effect free or it will not behave
 * as expected.
 *
 * TODO: Verify proper template caching.
 */
object LiftedScaml {
  /**
   * Initialize Lifted Scaml mode. Requires an extra suffix on files as a marker.
   *
   * This installs Scalate as a template resolver into the regular Lift pipeline.
   *
   * Templates must be named xxx.l.scaml
   */
  def init = {
    val suffix = "l.scaml"
    val resolver = new ScamlTemplateLoader(suffix)
    LiftRules.externalTemplateResolver.default.set(resolver.scalateTemplateLoader _)
  }
}

class ScamlTemplateLoader(suffix: String) extends Loggable {
  val renderer = new LiftTemplateEngine

  protected def createUri(path: List[String]): String = path.mkString("/") + "." + suffix

  protected def canLoad(v: String): Boolean = v.endsWith(".l.scaml") && renderer.canLoad(v)

  def canRender(path: List[String]): Boolean = canLoad(createUri(path))

  def scalateTemplateLoader: PartialFunction[(Locale, List[String]), Box[NodeSeq]] = {
    // TODO: Add locale suffix support
    case (locale, path) if (canRender(path)) => {
      val uri: String = createUri(path)
      val lrCache = LiftRules.templateCache
      val cache = if (lrCache.isDefined) lrCache.openOrThrowException("Internal Cache Error") else NoCache
      val key = (locale, path)
      val cachedTemplate = cache.get(key)
      if (cachedTemplate.isDefined) {
        cachedTemplate
      } else {
        logger.debug("Loading template at " + uri)
        val rawTemplate = renderer.layout(uri)
        val is = new ByteArrayInputStream(rawTemplate.getBytes("UTF-8"));
        val parserFunction: InputStream => Box[NodeSeq] = S.htmlProperties.htmlParser
        val template = parserFunction(is)
        template.map(t => cache(key) = t)
        template
      }
    }
  }
}