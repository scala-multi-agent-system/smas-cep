package de.hsaugsburg.cep.agent

import de.hsaugsburg.smas.plugin.base.SmasPlugin
import com.espertech.esper.client.EPServiceProviderManager
import de.hsaugsburg.smas.services.RequestForService
import de.hsaugsburg.smas.naming.AddressBookEntry
import de.hsaugsburg.cep.model.SensorEvent
import org.apache.log4j.Logger

/**
 * Initializes und manages the Esper cep engine.
 */
class EPAPlugin extends SmasPlugin {

  def onStop: Boolean = { true }

  def onStart: Boolean = {
    true
    //    TODO change name to real name of service, currently there is none
    /*def eventSources = node ?? RequestForService("EventSourceService")
    eventSources match {
      case List(entry) =>
        //    TODO how to register?
        //    entry ! Subscribe("EventSourceService")
        "registered"
        true
      case List(entry, _*) =>
        log.warn("Found more than one event source service. There is no mechanism to decide which is the correct one.")
        true
      case Nil =>
    	log.error("No event source services have been found.")
    	false
    }*/
  }

  def handleSensorEvent(event: SensorEvent) {
    val serviceProvider = EPServiceProviderManager.getProvider(EPANode.serviceProviderID)
    serviceProvider.getEPRuntime.sendEvent(event)
  }

  //  TODO add method to register/unregister EPLs at runtime

}

object EPAPlugin {
  private def log = Logger.getLogger(classOf[EPAPlugin])
}