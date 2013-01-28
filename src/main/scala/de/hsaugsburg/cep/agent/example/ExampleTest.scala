package de.hsaugsburg.cep.agent.example

import de.hsaugsburg.cep.model.SensorEvent
import de.hsaugsburg.cep.agent.{LogAllEventsUpdateListener, EsperHandler}
import de.hsaugsburg.cep.model.ChangeType
import org.apache.log4j.Logger

/**
 * Contains example test cases which test the three supported
 * events: MoveEvent, WorkEvent and ItemsChangedEvent.
 */
object ExampleTest extends App {

  private val log = Logger.getLogger("de.hsaugsburg.cep.agent.example.ExampleTest")

  private var numEvents = 0
  private val handler = new EsperHandler(EsperHandler.configFile, EsperHandler.moduleFile)
  private val sleepTime = 250

  handler.getStatement(EsperHandler.AllMoveEvents) addListener new LogAllEventsUpdateListener()
  handler.getStatement(EsperHandler.AllWorkEvents) addListener new LogAllEventsUpdateListener()
  handler.getStatement(EsperHandler.AllItemsChangedEvents) addListener new LogAllEventsUpdateListener()

  testWorkflow()

  def testWorkflow() {
    // add item
    sendItemsChangedEvent(ChangeType.Added)

    //move item to work machine
    Thread.sleep(sleepTime)
    sendMoveEvent("inSensor", "sensorDistr01")
    Thread.sleep(sleepTime)
    sendMoveEvent("sensorDistr01", "sensorM01Pos01")
    Thread.sleep(sleepTime)
    sendMoveEvent("sensorM01Pos01", "sensorM01Pos02")

    // begin work
    Thread.sleep(sleepTime)
    sendWorkEvent("sensorWorkM01")

    Thread.sleep(sleepTime)
    sendMoveEvent("sensorM01Pos02", "sensorM01Pos03")
    Thread.sleep(sleepTime)
    sendMoveEvent("sensorM01Pos03", "sensorOut01")
    Thread.sleep(sleepTime)
    // move to out sensor and removes the item
    sendMoveEvent("sensorOut01", "outSensor")
  }

  def testMoveEvent() {
    sendMoveEvent("sensor01", "sensor04")
    sendMoveEvent("sensor01", "sensor08")
  }

  def testWorkEvent() {
    sendWorkEvent("sensorWork01")
  }

  def testItemsChangedEvent() {
    sendItemsChangedEvent(ChangeType.Added)
  }

  def sendMoveEvent(sourceId: String, targetId: String) {
    log.info("Sending MoveEvent from '" + sourceId + "' to '" + targetId + "'")
    val sourceEvent = newSensorEvent(state = false, sourceId)
    EsperHandler.sendEvent(sourceEvent)

    Thread.sleep(500)

    val targetEvent = newSensorEvent(state = true, targetId)
    EsperHandler.sendEvent(targetEvent)
  }

  def sendWorkEvent(workSensor : String) {
    log.info("Sending WorkEvent for sensor '" + workSensor + "'")
    val workEvent = newSensorEvent(state = true, workSensor)
    EsperHandler.sendEvent(workEvent)
  }

  def sendItemsChangedEvent(changeType: ChangeType.ChangeType) {
    log.info("Sending ItemsChangedEvent of type '" + changeType + "'")
    val sensorId = changeType match {
      case ChangeType.Added => "inSensor"
      case ChangeType.Removed => "outSensor"
    }

    val itemsChangedEvent = newSensorEvent(state = true, sensorId)

    handler.sendEvent(itemsChangedEvent)
  }

  def newSensorEvent(state: Boolean, sensorId: String): SensorEvent = {
    numEvents += 1
    new SensorEvent("SensorEvent_" + numEvents, System.nanoTime(), false, sensorId)
  }
}
