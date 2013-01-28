package de.hsaugsburg.cep.agent

import de.hsaugsburg.smas.node.SmasNode
import de.hsaugsburg.smas.services.messages.RegisterService

class EPANode extends SmasNode {

  private var handler: Option[EsperHandler] = None

  def onStart() {
    try {
      manager ! RegisterService("EPAService", me)

      handler = Some(new EsperHandler(EsperHandler.configFile, EsperHandler.moduleFile))

      handler.get.getStatement(EsperHandler.AllMoveEvents) addListener new LogAllEventsUpdateListener()
      handler.get.getStatement(EsperHandler.AllWorkEvents) addListener new LogAllEventsUpdateListener()
      handler.get.getStatement(EsperHandler.AllItemsChangedEvents) addListener new LogAllEventsUpdateListener()

      log.info("Event Processing Agent started")
    } catch {
      case e: Exception => log.error("Could not start EPA Node", e)
    }
  }

  def onStop() {
    handler match {
      case None => log.error("No Module was registered. Did the node start correctly?")
      case Some(id) => handler.get.undeploy()
    }
    log.info("Event Processing Agent stopped")
  }

}
