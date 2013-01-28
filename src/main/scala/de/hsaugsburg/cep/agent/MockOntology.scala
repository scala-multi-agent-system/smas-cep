package de.hsaugsburg.cep.agent

import org.apache.log4j.Logger
import de.hsaugsburg.cep.model.ChangeType
import de.hsaugsburg.cep.model.Work

object MockOntology extends OntologyService {

  private val log = Logger.getLogger(MockOntology.getClass)

  def isNeighbour(sensorId: String, otherSensorId: String): Boolean = {
    val result = getNeighbours(sensorId).contains(otherSensorId)
    log.debug("isNeighbour '" + sensorId + "' and '" + otherSensorId + "': " + result)
    result
  }

  private def getNeighbours(sensorId: String): List[String] = {
    sensorId match {
      case "inSensor" => List("sensorDistr01")
      case "sensorDistr01" => List("sensorM01Pos01")
      case "sensorM01Pos01" => List("sensorM01Pos02")
      case "sensorM01Pos02" => List("sensorM01Pos03")
      case "sensorM01Pos03" => List("sensorOut01")
      case "sensorOut01" => List("outSensor")
      case "sensor01" => List("sensor04", "sensor08")
      case "sensor02" => List("sensor03", "sensor05", "sensor06")
      case _ => List.empty[String]
    }
  }

  def isMachineSensor(sensorId: String): Boolean = {
    sensorId.startsWith("sensorWork")
  }

  def getMachineId(sensorId: String): String = {
    "machine01"
  }

  def getMachineWorkType(sensorId: String): Work.Work = {
    Work.Begin
  }

  def isChangeSensor(sensorId: String): Boolean = {
    sensorId match {
      case "inSensor" => true
      case "outSensor" => true
      case _ => false
    }
  }

  def getChangeSensorType(sensorId: String): ChangeType.ChangeType = {
    sensorId match {
      case "inSensor" => ChangeType.Added
      case "outSensor" => ChangeType.Removed
      case _ => throw new IllegalStateException("isChangeSensor must return true for the specified sensorId")
    }
  }

}