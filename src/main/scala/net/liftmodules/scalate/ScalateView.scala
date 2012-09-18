/*
 * Copyright 2010-2011 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.liftmodules
package scalate

import xml.NodeSeq

import net.liftweb._
import common._
import util._
import http._


/**
 * A {@link LiftView} which uses a <a href="http://scalate.fusesource.org/">Scalate</a>
 * template engine to resolve a URI and render it as markup
 */
class ScalateView(engine: LiftTemplateEngine = new LiftTemplateEngine()) extends LiftView with Logger {

  /**
   * Registers this view with Lift's dispatcher
   */
  def register: Unit = {
    val scalateView: ScalateView = this

    /**
     * Registers viewDispatch to render templates using scalate.
     */
    LiftRules.viewDispatch.prepend(NamedPF("Scalate View") {
      case path if (canRender(path, "scaml")) =>
        debug("scalate viewDispatch Path: [" + path.mkString("/") + "]")
        Left(() => Full(engine.layoutAsNodes(createUri(path, "scaml"))))
      case path if (canRender(path, "jade")) =>
        debug("scalate viewDispatch Path: [" + path.mkString("/") + "]")
        Left(() => Full(engine.layoutAsNodes(createUri(path, "jade"))))
      case path if (canRender(path, "ssp")) =>
        debug("scalate viewDispatch Path: [" + path.mkString("/") + "]")
        Left(() => Full(engine.layoutAsNodes(createUri(path, "ssp"))))
    })
  }

  //should never get called
  def dispatch: PartialFunction[String, () => Box[NodeSeq]] = {
    case _ =>
      () => Empty
  }

  def canRender(path: List[String], ext: String): Boolean = {
    debug("=== attempting to find: " + path + " ext: '" + ext + "'")

    if (ext == "") {
      canLoad(createUri(path, "scaml")) || canLoad(createUri(path, "jade")) || canLoad(createUri(path, "ssp"))
    }
    else {
      val uri = createUri(path, ext)
      (uri.endsWith(".scaml") || uri.endsWith(".jade") || uri.endsWith(".ssp")) && canLoad(uri)
    }
  }

  protected def createUri(path: List[String], ext: String): String = path.mkString("/") +
          (if (ext.length > 0) "." + ext else "")

  protected def canLoad(v: String): Boolean = {
    engine.canLoad(v)
  }

}
