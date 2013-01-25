package de.hsaugsburg.cep.agent

import com.espertech.esper.client.EPServiceProvider
import com.espertech.esper.client.deploy.DeploymentException
import com.espertech.esper.client.EPException
import com.espertech.esper.client.deploy.DeploymentOptions
import java.io.IOException
import com.espertech.esper.client.EPServiceProviderManager
import com.espertech.esper.client.deploy.DeploymentResult
import com.espertech.esper.client.Configuration
import com.espertech.esper.client.deploy.ParseException
import com.espertech.esper.client.ConfigurationException
import de.hsaugsburg.cep.model.SensorEvent

/**
 * Interface to the esper cep enigne.
 */
class EsperHandler(val configFile: String, val moduleFile: String) {

  private val serviceProvider = configureServiceProvider(configFile)
  private val deploymentResult = loadModule(moduleFile, serviceProvider)

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
    EPServiceProviderManager.getProvider(EsperHandler.serviceProviderID, config)
  }

  def getStatement(name: String) = {
    serviceProvider.getEPAdministrator.getStatement(name)
  }

  def undeploy() {
    val serviceProvider = EPServiceProviderManager.getProvider(EsperHandler.serviceProviderID)
    val deployAdmin = serviceProvider.getEPAdministrator.getDeploymentAdmin
    deployAdmin.undeployRemove(deploymentResult.getDeploymentId)
  }
}

object EsperHandler {
  val serviceProviderID = "EPAServiceProvider"

  val configFile = "esper.cfg.xml"
  val moduleFile = "EPAModule.epl"

  val DetectMoveEvent = "DetectMoveEvent"
  val DetectItemsChangedEvent = "DetectItemsChangedEvent"
  val DetectWorkEvent = "DetectWorkEvent"

  val AllSensorEvents = "AllSensorEvents"
  val AllMoveEvents = "AllMoveEvents"
  val AllWorkEvents = "AllWorkEvents"
  val AllItemsChangedEvents = "AllItemsChangedEvent"

  def sendEvent(event: SensorEvent) {
    val serviceProvider = EPServiceProviderManager.getProvider(EsperHandler.serviceProviderID)
    serviceProvider.getEPRuntime.sendEvent(event)
  }
}