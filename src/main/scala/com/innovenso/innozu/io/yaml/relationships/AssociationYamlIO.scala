package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.innozu.io.yaml.properties.TitleYamlIO
import com.innovenso.townplanner.model.concepts.relationships.{Accessing, Association, CanAccess, CanBeAssociated}
import com.innovenso.townplanner.model.meta.Key

object AssociationYamlIO extends YamlRelationshipIO[Association, CanBeAssociated] {
  override def KEY: String = "association"

  override def theClass: Class[Association] = classOf[Association]

  override def readOne(
                        data: AssociationYamlIO.YamlJavaData,
                        source: CanBeAssociated
                      ): Option[Association] = readString(data, "target").map(targetKey =>
    Association(
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
