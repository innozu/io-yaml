package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{
  CanConsume,
  CanHaveDataRelationship,
  Cardinality,
  Consuming,
  DataRelationship
}
import com.innovenso.townplanner.model.meta.Key

object DataRelationshipYamlIO
    extends YamlRelationshipIO[DataRelationship, CanHaveDataRelationship] {
  override def KEY: String = "relatedTo"

  override def theClass: Class[DataRelationship] = classOf[DataRelationship]

  override def writeExtraProperties(
      modelComponent: DataRelationship,
      data: DataRelationshipYamlIO.YamlData
  ): Unit = {
    data.put("left", modelComponent.left.value)
    data.put("right", modelComponent.right.value)
  }

  override def readOne(
      data: DataRelationshipYamlIO.YamlJavaData,
      source: CanHaveDataRelationship
  ): Option[DataRelationship] = readString(data, "target").map(targetKey =>
    DataRelationship(
      key =
        readString(data, "key").map(v => Key.fromString(v)).getOrElse(Key()),
      source = source.key,
      target = Key.fromString(targetKey),
      left = Cardinality.fromString(readString(data, "left").getOrElse("")),
      right = Cardinality.fromString(readString(data, "right").getOrElse("")),
      properties = Map(
        Key.fromString("title") -> TitleYamlIO.read(data)
      )
    )
  )
}
