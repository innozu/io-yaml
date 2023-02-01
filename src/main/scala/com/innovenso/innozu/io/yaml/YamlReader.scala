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
  PlatformLayerYamlIO,
  TeamYamlIO,
  TechnologyYamlIO
}
import com.innovenso.innozu.io.yaml.relationships.RelationshipBuffer
import com.innovenso.innozu.io.yaml.util.FileUtils
import com.innovenso.townplanner.model.EnterpriseArchitecture
import fish.genius.logging.Loggable

import java.io.{File, FileInputStream}
import java.nio.file.Files

object YamlReader extends Loggable with HasSnakeYaml {
  private var sources: List[File] = Nil

  def add(yamlFile: File): Unit = yamlFile match {
    case d: File if d.isDirectory => addFiles(d.listFiles().toList)
    case f: File if f.canRead     => sources = f :: sources
    case _                        =>
  }

  def fromGithub(
      organisation: String,
      repository: String,
      branch: String,
      file: String,
      accessToken: String
  ): Unit = {
    val url =
      s"https://raw.githubusercontent.com/$organisation/$repository/$branch/$file"
    val response = requests.get(
      url,
      headers = Map(
        "Authorization" -> s"token $accessToken",
        "Accept" -> "application/vnd.github.v3.raw"
      )
    )
    val ymlText = response.text()
    val ymlFile = Files.createTempFile("yml", "read")
    FileUtils.writeStringToFile(ymlText, ymlFile.toFile).foreach(f => add(f))
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
    TechnologyYamlIO.read(data)
    ActorYamlIO.read(data)
    PersonYamlIO.read(data)
    TeamYamlIO.read(data)
    OrganisationYamlIO.read(data)
    ArchitectureBuildingBlockYamlIO.read(data)
    DataObjectYamlIO.read(data)
    ItPlatformYamlIO.read(data)
    PlatformLayerYamlIO.read(data)
    ItSystemYamlIO.read(data)
    ItContainerYamlIO.read(data)
    ItSystemIntegrationYamlIO.read(data)
  }

  def listSources(): Unit = sources.foreach(s => println(s.getAbsolutePath))
}
