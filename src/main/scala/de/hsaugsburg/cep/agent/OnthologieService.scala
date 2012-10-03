package de.hsaugsburg.cep.agent

import de.hsaugsburg.cep.model.ChangeType

trait OnthologieService {
	def isNeighbour(sensorId: String, otherSensorId: String): Boolean
	def isWorkerMachine(sensorId: String): Boolean
	//TODO change method name
	def isSensor(sensorId: String, changeType: ChangeType): Boolean
}