package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanProcess,
  CanProduce,
  Processing,
  Producing
}
import com.innovenso.townplanner.model.meta.Key

object ProducingYamlIO extends YamlRelationshipIO[Producing, CanProduce] {
  override def KEY: String = "produces"

  override def theClass: Class[Producing] = classOf[Producing]

  override def readOne(
      data: ProducingYamlIO.YamlJavaData,
      source: CanProduce
  ): Option[Producing] = readString(data, "target").map(targetKey =>
    Producing(
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
