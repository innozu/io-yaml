package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.concepts.properties.{HasTitle, Title}

import scala.collection.mutable

object TitleYamlIO extends YamlIO {
  def write(
      hasTitle: HasTitle,
      data: YamlData
  ): Unit = {
    debug(s"writing title ${hasTitle.title.value}")
    data.put("title", hasTitle.title.value)
  }
}
