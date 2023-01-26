package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  Description,
  HasDescription,
  HasResilienceMeasures,
  ResilienceMeasure
}

import scala.jdk.CollectionConverters.SeqHasAsJava

object ResilienceMeasureYamlIO
    extends YamlPropertiesIO[ResilienceMeasure, HasResilienceMeasures] {
  override def write(
      hasResilienceMeasures: HasResilienceMeasures,
      data: YamlData
  ): Unit = {
    if (hasResilienceMeasures.resilienceMeasures.nonEmpty) {
      debug(
        s"writing resilience measures ${hasResilienceMeasures.resilienceMeasures}"
      )
      data.put(
        "resilience",
        hasResilienceMeasures.resilienceMeasures
          .map(_.description)
          .toList
          .asJava
      )
    }
  }

  override def readMany(data: YamlJavaData): List[ResilienceMeasure] =
    readStrings(data, "resilience").map(s => ResilienceMeasure(s))
}
