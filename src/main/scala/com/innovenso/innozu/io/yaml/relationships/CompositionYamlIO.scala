package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  Association,
  CanBeAssociated,
  CanBeComposedOf,
  Composition
}
import com.innovenso.townplanner.model.meta.Key

object CompositionYamlIO
    extends YamlRelationshipIO[Composition, CanBeComposedOf] {
  override def KEY: String = "composition"

  override def theClass: Class[Composition] = classOf[Composition]

  override def readOne(
      data: CompositionYamlIO.YamlJavaData,
      source: CanBeComposedOf
  ): Option[Composition] = readString(data, "target").map(targetKey =>
    Composition(
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
