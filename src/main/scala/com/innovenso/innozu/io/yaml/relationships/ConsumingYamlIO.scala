package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  Accessing,
  CanAccess,
  CanConsume,
  Consuming
}
import com.innovenso.townplanner.model.meta.Key

object ConsumingYamlIO extends YamlRelationshipIO[Consuming, CanConsume] {
  override def KEY: String = "consumes"
  override def theClass: Class[Consuming] = classOf[Consuming]

  override def readOne(
      data: ConsumingYamlIO.YamlJavaData,
      source: CanConsume
  ): Option[Consuming] = readString(data, "target").map(targetKey =>
    Consuming(
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
