package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  HasLinks,
  HasSecurityMeasures,
  Link,
  SecurityMeasure
}

import scala.jdk.CollectionConverters.SeqHasAsJava

object SecurityMeasuresYamlIO
    extends YamlPropertiesIO[SecurityMeasure, HasSecurityMeasures] {
  override def write(
      hasSecurityMeasures: HasSecurityMeasures,
      data: YamlData
  ): Unit = if (hasSecurityMeasures.securityMeasures.nonEmpty) {
    debug(s"writing security measures ${hasSecurityMeasures.securityMeasures}")
    data.put(
      "securityMeasures",
      hasSecurityMeasures.securityMeasures.flatMap(writeOne).asJava
    )
  }

  override def writeOne(
      securityMeasure: SecurityMeasure
  ): Option[YamlJavaData] = Some(
    withMap { map =>
      debug(s"writing security measure $securityMeasure")
      map.put("type", securityMeasure.name)
      map.put("description", securityMeasure.description)
    }
  )

  override def readMany(data: YamlJavaData): List[SecurityMeasure] =
    readMaps(data, "securityMeasures").flatMap(readOne)

  override def readOne(data: YamlJavaData): Option[SecurityMeasure] = for {
    measureType <- readString(data, "type")
    description <- readString(data, "description").orElse(Some(""))
  } yield SecurityMeasure.fromString(measureType, Some(description))

}
