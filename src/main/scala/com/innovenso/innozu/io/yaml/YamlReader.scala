package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.TagYamlIO.YamlJavaData
import com.innovenso.innozu.io.yaml.concepts.{
  ActorYamlIO,
  ArchitectureBuildingBlockYamlIO,
  BusinessCapabilityYamlIO,
  DataObjectYamlIO,
  EnterpriseYamlIO,
  ItContainerYamlIO,
  ItPlatformYamlIO,
  ItSystemIntegrationYamlIO,
  ItSystemYamlIO,
  OrganisationYamlIO,
  PersonYamlIO,
  TeamYamlIO
}
import com.innovenso.innozu.io.yaml.relationships.RelationshipBuffer
import com.innovenso.townplanner.model.EnterpriseArchitecture
import fish.genius.logging.Loggable

import java.io.{File, FileInputStream}

object YamlReader extends Loggable with HasSnakeYaml {
  private var sources: List[File] = Nil

  def add(yamlFile: File): Unit = yamlFile match {
    case d: File if d.isDirectory => addFiles(d.listFiles().toList)
    case f: File if f.canRead     => sources = f :: sources
    case _                        =>
  }

  private def addFiles(yamlFiles: List[File]): Unit =
    yamlFiles.foreach(f => add(f))

  def load()(implicit ea: EnterpriseArchitecture): Unit = {
    sources.foreach(loadFile)
    RelationshipBuffer.save()
    sources = Nil
  }

  private def loadFile(
      file: File
  )(implicit ea: EnterpriseArchitecture): Unit = {
    info(s"loading YAML file ${file.getAbsolutePath}")
    val inputStream = new FileInputStream(file)
    val data: YamlJavaData = yaml.load(inputStream)
    EnterpriseYamlIO.read(data)
    BusinessCapabilityYamlIO.read(data)
    ActorYamlIO.read(data)
    PersonYamlIO.read(data)
    TeamYamlIO.read(data)
    OrganisationYamlIO.read(data)
    ArchitectureBuildingBlockYamlIO.read(data)
    DataObjectYamlIO.read(data)
    ItPlatformYamlIO.read(data)
    ItSystemYamlIO.read(data)
    ItContainerYamlIO.read(data)
    ItSystemIntegrationYamlIO.read(data)
  }
}
