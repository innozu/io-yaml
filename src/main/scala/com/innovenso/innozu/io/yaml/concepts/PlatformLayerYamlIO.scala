package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{PlatformLayer, Tag}
import com.innovenso.townplanner.model.meta.{Color, Key}

object PlatformLayerYamlIO extends ModelComponentYamlIO[PlatformLayer] {
  val KEY = "platformLayers"

  override def readOne(data: PlatformLayerYamlIO.YamlJavaData, key: Key)(
      implicit ea: EnterpriseArchitecture
  ): PlatformLayer =
    ea describes PlatformLayer(
      key = key,
      sortKey = readSortKey(data),
      color = readColor(data),
      order = readOrder(data)
    ) as { it =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
    }

  override def writeExtraProperties(
      modelComponent: PlatformLayer,
      data: PlatformLayerYamlIO.YamlData
  ): Unit = {
    data.put("color", modelComponent.color.hex)
    data.put("order", modelComponent.order)
  }

  def readColor(data: YamlJavaData): Color = readString(data, "color")
    .map(hex => Color.apply(hex)(hex))
    .getOrElse(Color.random)

  def readOrder(data: YamlJavaData): Int = readInt(data, "order").getOrElse(0)
}
