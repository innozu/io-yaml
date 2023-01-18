package com.innovenso.innozu.io.yaml.properties

import com.innovenso.innozu.io.yaml.YamlIO
import com.innovenso.townplanner.model.concepts.properties.{
  HasProperties,
  Property
}

import java.util

trait YamlPropertiesIO[
    PropertyType <: Property,
    PropertyContainerType <: HasProperties
] extends YamlIO {
  def write(propertyContainer: PropertyContainerType, data: YamlData): Unit

  def writeOne(property: PropertyType): Option[YamlJavaData] = None
  def readOne(data: YamlJavaData): Option[PropertyType] = None
  def readMany(data: YamlJavaData): List[PropertyType] = Nil
}
