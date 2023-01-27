package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.PlatformLayer
import com.innovenso.townplanner.model.concepts.properties.{
  HasPlatformLayerProperties,
  HasTagProperties,
  PlatformLayerProperty,
  TagProperty
}
import com.innovenso.townplanner.model.meta.Key

import scala.jdk.CollectionConverters.SeqHasAsJava

object PlatformLayerPropertyYamlIO
    extends YamlPropertiesIO[
      PlatformLayerProperty,
      HasPlatformLayerProperties
    ] {
  override def write(
      hasPlatformLayerProperties: HasPlatformLayerProperties,
      data: YamlData
  ): Unit =
    if (hasPlatformLayerProperties.platformLayer.nonEmpty) {
      debug(
        s"writing platformLayer ${hasPlatformLayerProperties.platformLayer}"
      )
      data.put(
        "layer",
        hasPlatformLayerProperties.platformLayer
          .map(p => p.platformLayerKey.value)
          .getOrElse("")
      )
    }

  override def readOne(
      data: PlatformLayerPropertyYamlIO.YamlJavaData
  ): Option[PlatformLayerProperty] = readString(data, "layer")
    .map(s => Key.fromString(s))
    .map(k => PlatformLayerProperty(platformLayerKey = k))
}
