package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanTransport,
  CanTrigger,
  Transporting,
  Trigger
}
import com.innovenso.townplanner.model.meta.Key

object TransportingYamlIO
    extends YamlRelationshipIO[Transporting, CanTransport] {
  override def KEY: String = "transports"

  override def theClass: Class[Transporting] = classOf[Transporting]

  override def readOne(
      data: TransportingYamlIO.YamlJavaData,
      source: CanTransport
  ): Option[Transporting] = readString(data, "target").map(targetKey =>
    Transporting(
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
