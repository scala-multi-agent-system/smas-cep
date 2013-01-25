package de.hsaugsburg.cep.agent

import de.hsaugsburg.smas.node.SmasNode
import com.espertech.esper.client._
import de.hsaugsburg.smas.services.messages.RegisterService
import org.apache.log4j.Logger
import java.io.IOException
import com.espertech.esper.client.deploy.DeploymentOptions
import com.espertech.esper.client.deploy.DeploymentResult
import com.espertech.esper.client.deploy.ParseException
import com.espertech.esper.client.deploy.DeploymentException

class EPANode extends SmasNode {

  private var handler: Option[EsperHandler] = None

  def onStart() {
    try {
      manager ! RegisterService("EPAService", me)

      handler = Some(new EsperHandler(EsperHandler.configFile, EsperHandler.moduleFile))

      val epl = handler.get.getStatement(EsperHandler.AllMoveEvents)
      epl addListener new LogAllEventsUpdateListener("MoveEvent")

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
