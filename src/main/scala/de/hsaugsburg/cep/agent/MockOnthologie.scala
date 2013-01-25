package de.hsaugsburg.cep.agent

import org.apache.log4j.Logger
import de.hsaugsburg.cep.model.ChangeType
import de.hsaugsburg.cep.model.Work

object MockOnthologie extends OntologyService {

  private val log = Logger.getLogger(MockOnthologie.getClass())

  def isNeighbour(sensorId: String, otherSensorId: String): Boolean = {
    val result = getNeighbours(sensorId).contains(otherSensorId)
    log.debug("isNeighbour '" + sensorId + "' and '" + otherSensorId + "': " + result)
    result
  }

  private def getNeighbours(sensorId: String): List[String] = {
    sensorId match {
      case "sensor01" => List("sensor04", "sensor08")
      case "sensor02" => List("sensor03", "sensor05", "sensor06")
      case _ => List.empty[String]
    }
  }
	def isMachineSensor(sensorId: String): Boolean = {
	  false
	}
	
	def getMachineId(sensorId: String): String = {
	  "machine01"
	}
	def getMachineWorkType(sensorId: String): Work.Work = {
	  Work.Begin
	}
	
	def isChangeSensor(sensorId: String): Boolean = {
	  false
	}
	
	def getChangeSensorType(sensorId: String): ChangeType.ChangeType = {
	  ChangeType.Added
	}

}