package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  ExternalId,
  HasExternalIds,
  HasLinks,
  Link
}

import scala.jdk.CollectionConverters.SeqHasAsJava

object ExternalIdYamlIO extends YamlPropertiesIO[ExternalId, HasExternalIds] {
  override def write(hasExternalIds: HasExternalIds, data: YamlData): Unit =
    if (hasExternalIds.externalIds.nonEmpty) {
      debug(s"writing links ${hasExternalIds.externalIds}")
      data.put(
        "externalIds",
        hasExternalIds.externalIds.flatMap(writeOne).asJava
      )
    }

  override def writeOne(externalId: ExternalId): Option[YamlJavaData] = Some(
    withMap { map =>
      debug(s"writing external ID $externalId")
      map.put("system", externalId.externalSystemName)
      map.put("id", externalId.id)
    }
  )

  override def readMany(data: YamlJavaData): List[ExternalId] =
    readMaps(data, "externalIds").flatMap(readOne)

  override def readOne(data: YamlJavaData): Option[ExternalId] = for {
    externalSystem <- readString(data, "system")
    id <- readString(data, "id")
  } yield ExternalId(id, externalSystem)

}
