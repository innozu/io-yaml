package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanConsume,
  CanImplement,
  Consuming,
  Implementation
}
import com.innovenso.townplanner.model.meta.Key

object ImplementationYamlIO
    extends YamlRelationshipIO[Implementation, CanImplement] {
  override def KEY: String = "implements"

  override def theClass: Class[Implementation] = classOf[Implementation]

  override def readOne(
      data: ImplementationYamlIO.YamlJavaData,
      source: CanImplement
  ): Option[Implementation] = readString(data, "target").map(targetKey =>
    Implementation(
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
