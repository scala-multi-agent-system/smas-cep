package de.hsaugsburg.cep.agent

import de.hsaugsburg.smas.startup.XmlSystemBuilder
import java.io.File

object Main extends App {
  XmlSystemBuilder.runOverXmlFileAndBuildSystem("/epaconfig.xml")
}