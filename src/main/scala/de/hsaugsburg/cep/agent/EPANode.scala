package de.hsaugsburg.cep.agent

import de.hsaugsburg.smas.node.SmasNode
import com.espertech.esper.client._
import de.hsaugsburg.smas.services.messages.RegisterService
import org.apache.log4j.Logger
import de.hsaugsburg.cep.model.SensorEvent
import java.io.File
import java.io.IOException
import com.espertech.esper.client.deploy.Module
import com.espertech.esper.client.deploy.DeploymentOptions
import com.espertech.esper.client.deploy.DeploymentResult
import com.espertech.esper.client.deploy.ParseException
import com.espertech.esper.client.deploy.DeploymentException

class EPANode extends SmasNode {

  private var moduleId: Option[String] = None

  def onStart(): Unit = {
    try {
      manager ! RegisterService("EPAService", me)

      val testfile = new File(EPANode.configFile)
      println(testfile.getCanonicalPath() + " : " + testfile.exists())
      val serviceProvider = configureServiceProvider(EPANode.configFile)

      val result = loadModule(EPANode.moduleFile, serviceProvider)
      moduleId = Some(result.getDeploymentId())

      val epl = serviceProvider.getEPAdministrator.getStatement(EPANode.AllMoveEvents)
      epl addListener new LogUpdateListener()

      log.info("Event Processing Agent started")
    } catch {
      case e: Exception => log.error("Could not start EPA Node", e)
    }
  }

  def onStop(): Unit = {
    moduleId match {
      case None => log.error("No Module was registered. Did the node start correctly?")
      case Some(id) =>
        val serviceProvider = EPServiceProviderManager.getProvider(EPANode.serviceProviderID)
        val deployAdmin = serviceProvider.getEPAdministrator.getDeploymentAdmin
        deployAdmin.undeployRemove(id)
    }
    log.info("Event Processing Agent stopped")
  }

  @throws(classOf[IOException])
  @throws(classOf[DeploymentException])
  @throws(classOf[ParseException])
  private def loadModule(fileName: String, serviceProvider: EPServiceProvider): DeploymentResult = {
    val deployAdmin = serviceProvider.getEPAdministrator.getDeploymentAdmin
    val module = deployAdmin.read(fileName)
    val options = new DeploymentOptions
    deployAdmin.deploy(module, options)
  }

  @throws(classOf[EPException])
  @throws(classOf[ConfigurationException])
  private def configureServiceProvider(configFile: String): EPServiceProvider = {
    val config = new Configuration()
    config.configure(configFile)
    EPServiceProviderManager.getProvider(EPANode.serviceProviderID, config)
  }

}

object EPANode {
  private val configFile = "esper.cfg.xml"
  private val moduleFile = "EPAModule.epl"

  private val DetectMoveEvent = "DetectMoveEvent"
  private val DetectItemsChangedEvent = "DetectItemsChangedEvent"
  private val DetectWorkEvent = "DetectWorkEvent"
  
  private val AllSensorEvents = "AllSensorEvents"
  private val AllMoveEvents = "AllMoveEvents"
    
  val serviceProviderID = "EPAServiceProvider"
}

class LogUpdateListener extends UpdateListener {

  private val log = Logger.getLogger(classOf[LogUpdateListener])

  def update(newEvents: Array[EventBean], oldEvents: Array[EventBean]) {
    val event = newEvents(0)
    log.info("New MoveEvent: " + event.get("eventId"))
  }
}
