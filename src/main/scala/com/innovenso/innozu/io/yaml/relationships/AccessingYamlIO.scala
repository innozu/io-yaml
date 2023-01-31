package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.DataAttributeYamlIO.{
  readMaps,
  readOne
}
import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.properties.{
  HasLinks,
  Link,
  Title
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Accessing,
  CanAccess
}
import com.innovenso.townplanner.model.meta.Key

import scala.jdk.CollectionConverters.SeqHasAsJava

object AccessingYamlIO extends YamlRelationshipIO[Accessing, CanAccess] {
  override def KEY: String = "accesses"
  override def theClass: Class[Accessing] = classOf[Accessing]

  override def readOne(
      data: AccessingYamlIO.YamlJavaData,
      source: CanAccess
  ): Option[Accessing] = readString(data, "target").map(targetKey =>
    Accessing(
      key =
        readString(data, "key").map(v => Key.fromString(v)).getOrElse(Key()),
      source = source.key,
      target = Key.fromString(targetKey),
      properties = Map(
        Key.fromString("title") -> TitleYamlIO.read(data)
      )
    )
  )
}
