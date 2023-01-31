package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanBeStakeholder,
  CanTrigger,
  Stakeholder,
  Trigger
}
import com.innovenso.townplanner.model.meta.Key

object TriggeringYamlIO extends YamlRelationshipIO[Trigger, CanTrigger] {
  override def KEY: String = "triggers"

  override def theClass: Class[Trigger] = classOf[Trigger]

  override def readOne(
      data: TriggeringYamlIO.YamlJavaData,
      source: CanTrigger
  ): Option[Trigger] = readString(data, "target").map(targetKey =>
    Trigger(
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
