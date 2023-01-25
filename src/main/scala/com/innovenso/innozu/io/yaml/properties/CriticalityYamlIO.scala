package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  Criticality,
  ExternalId,
  HasCriticality,
  HasExternalIds,
  UnknownCriticality
}

import scala.jdk.CollectionConverters.SeqHasAsJava

object CriticalityYamlIO extends YamlPropertiesIO[Criticality, HasCriticality] {
  override def write(hasCriticality: HasCriticality, data: YamlData): Unit =
    if (!hasCriticality.isUnknownCriticality) {
      debug(s"writing criticality ${hasCriticality.criticality}")
      data.put(
        "criticality",
        writeOne(hasCriticality.criticality).get
      )
    }

  override def writeOne(criticality: Criticality): Option[YamlJavaData] = Some(
    withMap { map =>
      debug(s"writing criticality $criticality")
      map.put("level", criticality.name)
      map.put("consequences", criticality.consequences)
    }
  )

  def read(data: YamlJavaData): Criticality =
    readMap(data, "criticality")
      .flatMap(readOne)
      .getOrElse(UnknownCriticality())
  override def readOne(data: YamlJavaData): Option[Criticality] = Some(
    Criticality.fromString(
      readString(data, "level").getOrElse(""),
      readString(data, "consequences")
    )
  )

}
