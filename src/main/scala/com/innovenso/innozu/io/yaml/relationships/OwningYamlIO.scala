package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanConsume,
  CanOwn,
  Consuming,
  Owning
}
import com.innovenso.townplanner.model.meta.Key

object OwningYamlIO extends YamlRelationshipIO[Owning, CanOwn] {
  override def KEY: String = "owns"

  override def theClass: Class[Owning] = classOf[Owning]

  override def readOne(
      data: OwningYamlIO.YamlJavaData,
      source: CanOwn
  ): Option[Owning] = readString(data, "target").map(targetKey =>
    Owning(
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
