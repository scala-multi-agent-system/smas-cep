package de.hsaugsburg.cep.agent.example

import de.hsaugsburg.smas.startup.XmlSystemBuilder
import de.hsaugsburg.cep.model.ItemMovedEvent
import de.hsaugsburg.cep.model.ItemMovedEvent
import de.hsaugsburg.cep.model.SensorEvent
import de.hsaugsburg.cep.model.SensorEvent
import de.hsaugsburg.cep.agent.{LogAllEventsUpdateListener, EsperHandler}
import de.hsaugsburg.cep.model.ChangeType

/**
 * Contains example test cases which test the three supported
 * events: MoveEvent, WorkEvent and ItemsChangedEvent.
 */
object ExampleTest extends App {

  private var numEvents = 0
  private val handler = new EsperHandler(EsperHandler.configFile, EsperHandler.moduleFile)

  handler.getStatement(EsperHandler.AllMoveEvents) addListener new LogAllEventsUpdateListener("MoveEvent")
  handler.getStatement(EsperHandler.AllWorkEvents) addListener new LogAllEventsUpdateListener("WorkEvent")
  handler.getStatement(EsperHandler.AllItemsChangedEvents) addListener new LogAllEventsUpdateListener("ItemsChangedEvent")

  testItemsChangedEvent()

  def testMoveEvent() {
    sendMoveEvent("sensor01", "sensor04")
  }

  def testWorkEvent() {
    sendWorkEvent()
  }

  def testItemsChangedEvent() {
    sendItemsChangedEvent(ChangeType.Added)
  }

  def sendMoveEvent(sourceId: String, targetId: String) {
    val sourceEvent = newSensorEvent(state = false, sourceId)
    EsperHandler.sendEvent(sourceEvent)

    Thread.sleep(500)

    val targetEvent = newSensorEvent(state = true, targetId)
    EsperHandler.sendEvent(targetEvent)
  }

  def sendWorkEvent() {
    EsperHandler.sendEvent(newSensorEvent(state = true, "sensorWork01"))
  }

  def sendItemsChangedEvent(changeType: ChangeType.ChangeType) {
    val sensorId = changeType match {
      case ChangeType.Added => "inSensor"
      case ChangeType.Removed => "outSensor"
    }

    val itemsChangedEvent = newSensorEvent(state = true, sensorId)
    EsperHandler.sendEvent(itemsChangedEvent)
  }

  def newSensorEvent(state: Boolean, sensorId: String): SensorEvent = {
    numEvents += 1
    new SensorEvent("SensorEvent_" + numEvents, System.nanoTime(), false, sensorId)
  }
}
