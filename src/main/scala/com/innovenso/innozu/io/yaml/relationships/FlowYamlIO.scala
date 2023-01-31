package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanBeFlowSource,
  CanConsume,
  Consuming,
  Flow
}
import com.innovenso.townplanner.model.meta.Key

object FlowYamlIO extends YamlRelationshipIO[Flow, CanBeFlowSource] {
  override def KEY: String = "uses"

  override def theClass: Class[Flow] = classOf[Flow]

  override def readOne(
      data: FlowYamlIO.YamlJavaData,
      source: CanBeFlowSource
  ): Option[Flow] = readString(data, "target").map(targetKey =>
    Flow(
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
