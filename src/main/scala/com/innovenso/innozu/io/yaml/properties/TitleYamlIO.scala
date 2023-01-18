package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{HasTitle, Title}

object TitleYamlIO extends YamlPropertiesIO[Title, HasTitle] {
  override def write(
      hasTitle: HasTitle,
      data: YamlData
  ): Unit = {
    debug(s"writing title ${hasTitle.title.value}")
    data.put("title", hasTitle.title.value)
  }

  def read(data: YamlJavaData): Title = readOne(data).getOrElse(Title(""))

  override def readOne(data: TitleYamlIO.YamlJavaData): Option[Title] =
    readString(data, "title").map(t => Title(t))
}
