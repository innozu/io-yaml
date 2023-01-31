package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanBeStakeholder,
  CanServe,
  Serving,
  Stakeholder
}
import com.innovenso.townplanner.model.meta.Key

object StakeholderYamlIO
    extends YamlRelationshipIO[Stakeholder, CanBeStakeholder] {
  override def KEY: String = "stakeholder"

  override def theClass: Class[Stakeholder] = classOf[Stakeholder]

  override def readOne(
      data: StakeholderYamlIO.YamlJavaData,
      source: CanBeStakeholder
  ): Option[Stakeholder] = readString(data, "target").map(targetKey =>
    Stakeholder(
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
