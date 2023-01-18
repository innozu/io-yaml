package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{Description, HasDescription}

import scala.jdk.CollectionConverters.SeqHasAsJava

object DescriptionYamlIO extends YamlPropertiesIO[Description, HasDescription] {
  override def write(
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

  override def readMany(data: YamlJavaData): List[Description] =
    readStrings(data, "description").map(s => Description(s))
}
