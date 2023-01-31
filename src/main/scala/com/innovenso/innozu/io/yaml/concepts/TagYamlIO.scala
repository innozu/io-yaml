package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{BusinessCapability, Tag}
import com.innovenso.townplanner.model.meta.{Color, Key}

object TagYamlIO extends ModelComponentYamlIO[Tag] {
  val KEY = "tags"

  override def readOne(data: TagYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): Tag =
    ea describes Tag(
      key = key,
      sortKey = readSortKey(data),
      color = readColor(data)
    ) as { it =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
    }

  override def writeExtraProperties(
      modelComponent: Tag,
      data: TagYamlIO.YamlData
  ): Unit = data.put("color", modelComponent.color.hex)

  def readColor(data: YamlJavaData): Color = readString(data, "color")
    .map(hex => Color.apply(hex)(hex))
    .getOrElse(Color.random)
}
