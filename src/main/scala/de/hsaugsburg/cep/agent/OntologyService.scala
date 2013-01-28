package de.hsaugsburg.cep.agent

import de.hsaugsburg.cep.model.ChangeType
import de.hsaugsburg.cep.model.Work

trait OntologyService {
  def isNeighbour(sensorId: String, otherSensorId: String): Boolean

  def isMachineSensor(sensorId: String): Boolean

  def getMachineId(sensorId: String): String

  def getMachineWorkType(sensorId: String): Work.Work

  def isChangeSensor(sensorId: String): Boolean

  def getChangeSensorType(sensorId: String): ChangeType.ChangeType
}