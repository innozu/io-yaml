package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.YamlIO
import com.innovenso.innozu.io.yaml.properties.{
  DescriptionYamlIO,
  FatherTimeYamlIO,
  TitleYamlIO
}
import com.innovenso.innozu.io.yaml.relationships.AccessingYamlIO.{
  KEY,
  readMaps,
  readOne
}
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.properties.{
  HasDescription,
  HasFatherTime,
  HasProperties,
  HasTitle,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanBeRelationshipSource,
  Relationship
}

import scala.jdk.CollectionConverters.SeqHasAsJava

trait YamlRelationshipIO[
    RelationshipType <: Relationship,
    RelationshipSourceType <: CanBeRelationshipSource
] extends YamlIO {

  def KEY: String
  def theClass: Class[RelationshipType]

  def write(propertyContainer: RelationshipSourceType, data: YamlData)(implicit
      ea: EnterpriseArchitecture
  ): Unit = {
    val relationships = ea.townPlan
      .relationshipsWithSource(propertyContainer)
      .filter(theClass.isInstance)
      .map(theClass.cast)
    if (relationships.nonEmpty) {
      debug(s"writing relationships ${relationships}")
      data.put(KEY, relationships.flatMap(writeOne).asJava)
    }
  }

  def writeOne(relationship: RelationshipType)(implicit
      ea: EnterpriseArchitecture
  ): Option[YamlJavaData] = Some(withMap { map =>
    debug(s"writing $relationship")
    writeKeys(relationship, map)
    writeExtraProperties(relationship, map)
    TitleYamlIO.write(relationship, map)
    DescriptionYamlIO.write(relationship, map)
    FatherTimeYamlIO.write(relationship, map)
  })

  def writeKeys(
      modelComponent: RelationshipType,
      data: YamlData
  ): Unit = {
    debug(s"writing relationship properties: key")
    data.put("key", modelComponent.key.value)
    data.put("target", modelComponent.target.value)
  }

  def writeExtraProperties(
      modelComponent: RelationshipType,
      data: YamlData
  ): Unit = {}
  def readOne(
      data: YamlJavaData,
      source: RelationshipSourceType
  ): Option[RelationshipType] = None
  def readMany(
      data: YamlJavaData,
      source: RelationshipSourceType
  ): Unit =
    readMaps(data, KEY).flatMap(m => readOne(m, source)).foreach(RelationshipBuffer.add)
}
