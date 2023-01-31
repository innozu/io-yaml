package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanOwn,
  CanProcess,
  Owning,
  Processing
}
import com.innovenso.townplanner.model.meta.Key

object ProcessingYamlIO extends YamlRelationshipIO[Processing, CanProcess] {
  override def KEY: String = "processes"

  override def theClass: Class[Processing] = classOf[Processing]

  override def readOne(
      data: ProcessingYamlIO.YamlJavaData,
      source: CanProcess
  ): Option[Processing] = readString(data, "target").map(targetKey =>
    Processing(
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
