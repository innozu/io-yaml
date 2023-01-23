package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  ExternalId,
  HasExternalIds,
  HasTagProperties,
  TagProperty
}
import com.innovenso.townplanner.model.meta.Key

import scala.jdk.CollectionConverters.SeqHasAsJava

object TagPropertyYamlIO
    extends YamlPropertiesIO[TagProperty, HasTagProperties] {
  override def write(hasTagProperties: HasTagProperties, data: YamlData): Unit =
    if (hasTagProperties.tags.nonEmpty) {
      debug(s"writing tag properties ${hasTagProperties.tags}")
      data.put(
        "tags",
        hasTagProperties.tags.map(_.tagKey.value).asJava
      )
    }

  override def readMany(data: YamlJavaData): List[TagProperty] =
    readStrings(data, "tags").map(tagKey =>
      new TagProperty(tagKey = Key.fromString(tagKey))
    )

}
