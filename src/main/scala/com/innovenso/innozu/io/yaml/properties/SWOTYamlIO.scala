package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{HasSWOT, SWOT}

import scala.jdk.CollectionConverters.SeqHasAsJava

object SWOTYamlIO extends YamlPropertiesIO[SWOT, HasSWOT] {
  override def write(
      propertyContainer: HasSWOT,
      data: SWOTYamlIO.YamlData
  ): Unit = if (propertyContainer.swots.nonEmpty) {
    debug(s"writing links ${propertyContainer.swots}")
    data.put(
      "swot",
      withMap { map =>
        if (propertyContainer.strengths.nonEmpty)
          map.put(
            "strengths",
            propertyContainer.strengths.map(_.description).asJava
          )
        if (propertyContainer.weaknesses.nonEmpty)
          map.put(
            "weaknesses",
            propertyContainer.weaknesses.map(_.description).asJava
          )
        if (propertyContainer.opportunities.nonEmpty)
          map.put(
            "opportunities",
            propertyContainer.opportunities.map(_.description).asJava
          )
        if (propertyContainer.threats.nonEmpty)
          map.put(
            "threats",
            propertyContainer.threats.map(_.description).asJava
          )
      }
    )
  }

  override def readMany(data: SWOTYamlIO.YamlJavaData): List[SWOT] =
    readMap(data, "swot")
      .map(d => {
        val strengths =
          readStrings(d, "strengths").map(SWOT.fromString("strength", _))
        val weaknesses =
          readStrings(d, "weaknesses").map(SWOT.fromString("weakness", _))
        val opportunities =
          readStrings(d, "opportunities").map(SWOT.fromString("opportunity", _))
        val threats =
          readStrings(d, "threats").map(SWOT.fromString("threat", _))
        strengths ::: weaknesses ::: opportunities ::: threats
      })
      .getOrElse(Nil)
}
