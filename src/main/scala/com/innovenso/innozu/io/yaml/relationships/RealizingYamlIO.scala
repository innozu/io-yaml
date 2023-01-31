package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanProduce,
  CanRealize,
  Producing,
  Realization
}
import com.innovenso.townplanner.model.meta.Key

object RealizingYamlIO extends YamlRelationshipIO[Realization, CanRealize] {
  override def KEY: String = "realizes"

  override def theClass: Class[Realization] = classOf[Realization]

  override def readOne(
      data: RealizingYamlIO.YamlJavaData,
      source: CanRealize
  ): Option[Realization] = readString(data, "target").map(targetKey =>
    Realization(
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
