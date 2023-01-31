package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanRealize,
  CanServe,
  Realization,
  Serving
}
import com.innovenso.townplanner.model.meta.Key

object ServingYamlIO extends YamlRelationshipIO[Serving, CanServe] {
  override def KEY: String = "serves"

  override def theClass: Class[Serving] = classOf[Serving]

  override def readOne(
      data: ServingYamlIO.YamlJavaData,
      source: CanServe
  ): Option[Serving] = readString(data, "target").map(targetKey =>
    Serving(
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
