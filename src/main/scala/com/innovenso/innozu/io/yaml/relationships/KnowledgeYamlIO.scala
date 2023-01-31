package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{CanConsume, CanKnow, Consuming, Knowledge}
import com.innovenso.townplanner.model.meta.Key

object KnowledgeYamlIO extends YamlRelationshipIO[Knowledge, CanKnow] {
  override def KEY: String = "knows"

  override def theClass: Class[Knowledge] = classOf[Knowledge]

  override def readOne(
                        data: KnowledgeYamlIO.YamlJavaData,
                        source: CanKnow
                      ): Option[Knowledge] = readString(data, "target").map(targetKey =>
    Knowledge(
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
