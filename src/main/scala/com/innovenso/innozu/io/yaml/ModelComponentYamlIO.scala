package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.concepts.properties.HasTitle
import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.{Key, SortKey}

import scala.jdk.CollectionConverters.MapHasAsScala

object ModelComponentYamlIO extends YamlIO {
  def write(
      modelComponent: ModelComponent,
      data: YamlData
  ): Unit = {
    debug(s"writing model component properties: key and sortKey")
    data.put("key", modelComponent.key.value)
    modelComponent.sortKey.value.foreach(sk => data.put("sortKey", sk))
  }

  def readKey(data: YamlJavaData): Key =
    readString(data, "key").map(Key.fromString).getOrElse(Key())

  def withKey(
      data: YamlJavaData,
      operation: Key => Unit
  ): Unit = data.asScala match {
    case v if v.get("key").exists(kv => kv.isInstanceOf[String]) =>
      operation.apply(readKey(data))
    case _ => ()
  }

  def readSortKey(data: YamlJavaData): SortKey = readString(data, "sortKey")
    .map(sk => SortKey(Some(sk)))
    .getOrElse(SortKey.next)
}
