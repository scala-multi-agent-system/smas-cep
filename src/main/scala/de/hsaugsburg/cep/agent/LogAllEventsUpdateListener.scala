package de.hsaugsburg.cep.agent

import com.espertech.esper.client.UpdateListener
import com.espertech.esper.client.EventBean
import org.apache.log4j.Logger
import de.hsaugsburg.cep.model.{ItemMovedEvent, WorkEvent, ItemsChangedEvent}

class LogAllEventsUpdateListener extends UpdateListener {

  private val log = Logger.getLogger(classOf[LogAllEventsUpdateListener])

  def update(newEvents: Array[EventBean], oldEvents: Array[EventBean]) {
    val numOfEvents = newEvents.length
    numOfEvents match {
      case 1 => handleSingleEvent(newEvents(0))
      case _ => log.info("Illegal State: No new events. (What did cause the update?)")
    }
  }

  def handleSingleEvent(eventBean: EventBean) {
    eventBean.getUnderlying match {
      case event: ItemsChangedEvent =>
        log.info("Detected ItemsChangedEvent: item " + event.changeType)
      case event: WorkEvent =>
        log.info("Detected WorkEvent: " + event.work + " work at machine '" + event.workerId + "'")
      case event: ItemMovedEvent =>
        log.info("Detected MoveEvent: item '" + event.itemId + "' moved from '" + event.sourceId + "' to '" + event.targetId + "'")
      case _ => log.info("Unhandled event type '" + eventBean.getEventType.getUnderlyingType.getCanonicalName + "'")
    }
  }
}