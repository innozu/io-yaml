package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanConsume,
  CanInfluence,
  Consuming,
  Influence
}
import com.innovenso.townplanner.model.meta.Key

object InfluencingYamlIO extends YamlRelationshipIO[Influence, CanInfluence] {
  override def KEY: String = "influences"

  override def theClass: Class[Influence] = classOf[Influence]

  override def readOne(
      data: InfluencingYamlIO.YamlJavaData,
      source: CanInfluence
  ): Option[Influence] = readString(data, "target").map(targetKey =>
    Influence(
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
