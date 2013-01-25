package de.hsaugsburg.cep.agent

import com.espertech.esper.client.UpdateListener
import com.espertech.esper.client.EventBean
import org.apache.log4j.Logger

class LogAllEventsUpdateListener(val eventType: String) extends UpdateListener {

  private val log = Logger.getLogger(classOf[LogAllEventsUpdateListener])

  def update(newEvents: Array[EventBean], oldEvents: Array[EventBean]) {
    val event = newEvents(0)
    val logText: String = "New " + eventType + ": " + event.get("eventId")
    log.info(logText)
    System.out.println(logText)
  }
}