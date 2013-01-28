package de.hsaugsburg.cep.agent

import de.hsaugsburg.cep.model.ChangeType
import de.hsaugsburg.cep.model.Work

/**
 * This interface provides methods to query information from the ontology of
 * the industrial plant. These methods are necessary for the Esper statements
 * to function properly.
 * <p>
 * Depending on the latency caused by querying the ontology, the result of
 * the query should be cached to reduce the load on the ontology server.
 * The ontology of a plant should not be changing during runtime, except for
 * modular plant parts. These should be considered when implementing
 * caching functionality.
 */
trait OntologyService {

  /**
   * Checks if both sensors specified by the ids are neighbours. Items of the plant
   * can move between neighbouring sensors.
   *
   * @param sensorId the id of a sensor
   * @param otherSensorId the id of a sensor
   * @return true if both specified sensors are neighbours, otherwise false
   */
  def isNeighbour(sensorId: String, otherSensorId: String): Boolean

  /**
   * @param sensorId the id of a sensor
   * @return true if the specified sensor is the sensor of a machine
   */
  def isMachineSensor(sensorId: String): Boolean

  /**
   * @param sensorId the id of a sensor
   * @return the id of the machine the specified sensor belongs to
   */
  def getMachineId(sensorId: String): String

  /**
   * Returns the work type triggered by the specified sensor. The lower
   * sensor of a machine triggers the type Work.Begin, while the upper
   * sensor triggers the type Work.End.
   *
   * @param sensorId the id of a sensor
   * @return the work type triggered by the specified sensor
   */
  def getMachineWorkType(sensorId: String): Work.Work

  /**
   * @param sensorId the id of a sensor
   * @return true if the specified sensor is the sensor of the entry or
   *         exit of the plant, otherwise false
   */
  def isChangeSensor(sensorId: String): Boolean

  /**
   * The sensor of the entry of the plant should trigger ChangeType.Added,
   * while the sensor of the output triggers ChangeType.Removed.
   *
   * @param sensorId the id of a sensor
   * @return the type of change triggered by the specified sensor
   */
  def getChangeSensorType(sensorId: String): ChangeType.ChangeType
}