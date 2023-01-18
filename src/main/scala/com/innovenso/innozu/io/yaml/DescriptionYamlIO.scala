package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.concepts.properties.{
  Description,
  HasDescription,
  HasTitle
}

import scala.jdk.CollectionConverters.SeqHasAsJava

object DescriptionYamlIO extends YamlIO {
  def write(
      hasDescription: HasDescription,
      data: YamlData
  ): Unit = {
    if (hasDescription.descriptions.nonEmpty) {
      debug(s"writing descriptions ${hasDescription.descriptions}")
      data.put(
        "description",
        hasDescription.descriptions.map(_.value).toList.asJava
      )
    }
  }

  def read(data: YamlJavaData): List[Description] =
    readStrings(data, "description").map(s => Description(s))
}
