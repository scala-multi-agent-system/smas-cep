package de.hsaugsburg.cep.agent

import de.hsaugsburg.smas.startup.XmlSystemBuilder

object Main extends App {
  XmlSystemBuilder.runOverXmlFileAndBuildSystem("/epaconfig.xml")
}