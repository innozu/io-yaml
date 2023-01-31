package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{CanConsume, CanDeliver, Consuming, Delivery}
import com.innovenso.townplanner.model.meta.Key

object DeliveryYamlIO extends YamlRelationshipIO[Delivery, CanDeliver] {
  override def KEY: String = "delivers"

  override def theClass: Class[Delivery] = classOf[Delivery]

  override def readOne(
                        data: DeliveryYamlIO.YamlJavaData,
                        source: CanDeliver
                      ): Option[Delivery] = readString(data, "target").map(targetKey =>
    Delivery(
      key =
        readString(data, "key").map(v => Key.fromString(v)).getOrElse(Key()),
      source = source.key,
      target = Key.fromString(targetKey),
      properties = Map(
        Key.fromString("title") -> TitleYamlIO.read(data)
      )
    )
  )
}
