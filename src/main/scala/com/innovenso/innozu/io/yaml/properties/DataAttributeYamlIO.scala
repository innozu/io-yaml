package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  DataAttribute,
  ExternalId,
  HasDataAttributes,
  HasExternalIds
}

import scala.jdk.CollectionConverters.SeqHasAsJava

object DataAttributeYamlIO
    extends YamlPropertiesIO[DataAttribute, HasDataAttributes] {
  override def write(
      hasDataAttributes: HasDataAttributes,
      data: YamlData
  ): Unit =
    if (hasDataAttributes.dataAttributes.nonEmpty) {
      debug(s"writing links ${hasDataAttributes.dataAttributes}")
      data.put(
        "attributes",
        hasDataAttributes.dataAttributes.flatMap(writeOne).asJava
      )
    }

  override def writeOne(dataAttribute: DataAttribute): Option[YamlJavaData] =
    Some(
      withMap { map =>
        debug(s"writing data attribute $dataAttribute")
        map.put("name", dataAttribute.name)
        dataAttribute.description.foreach(d => map.put("description", d))
        dataAttribute.dataType.foreach(t => map.put("type", t))
        map.put("required", dataAttribute.required)
        map.put("multiple", dataAttribute.multiple)
      }
    )

  override def readMany(data: YamlJavaData): List[DataAttribute] =
    readMaps(data, "attributes").flatMap(readOne)

  override def readOne(data: YamlJavaData): Option[DataAttribute] = for {
    name <- readString(data, "name")
    description <- Some(readString(data, "description"))
    dataType <- Some(readString(data, "type"))
    required <- readBoolean(data, "required").orElse(Some(false))
    multiple <- readBoolean(data, "multiple").orElse(Some(false))
  } yield DataAttribute(
    name = name,
    description = description,
    required = required,
    multiple = multiple,
    dataType = dataType
  )

}
