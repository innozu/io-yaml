package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  ExternalId,
  FatherTime,
  HasExternalIds,
  HasFatherTime
}
import com.innovenso.townplanner.model.meta.Day

import scala.jdk.CollectionConverters.SeqHasAsJava

object FatherTimeYamlIO extends YamlPropertiesIO[FatherTime, HasFatherTime] {
  override def write(hasFatherTime: HasFatherTime, data: YamlData): Unit =
    if (hasFatherTime.lifeEvents.nonEmpty) {
      debug(s"writing father time ${hasFatherTime.lifeEvents}")
      data.put(
        "fatherTime",
        hasFatherTime.lifeEvents.flatMap(writeOne).asJava
      )
    } else {
      debug(s"no father time found!")
    }

  override def writeOne(fatherTime: FatherTime): Option[YamlJavaData] = Some(
    withMap { map =>
      debug(s"writing life event $fatherTime")
      map.put("date", fatherTime.date.toString())
      map.put("event", fatherTime.name)
      map.put("description", fatherTime.description)
    }
  )

  override def readMany(data: YamlJavaData): List[FatherTime] =
    readMaps(data, "fatherTime").flatMap(readOne)

  override def readOne(data: YamlJavaData): Option[FatherTime] =
    for {
      dateString <- readString(data, "date")
      date <- Some(Day.fromString(dateString))
      name <- readString(data, "event")
      description <- readString(data, "description").orElse(Some(""))
    } yield FatherTime.fromString(name, date, description)

}
