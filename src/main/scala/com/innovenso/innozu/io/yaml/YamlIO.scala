package com.innovenso.innozu.io.yaml

import fish.genius.logging.Loggable

import scala.collection.mutable
import scala.jdk.CollectionConverters.{MapHasAsScala, MutableMapHasAsJava}

trait YamlIO extends Loggable {
  type YamlData = mutable.LinkedHashMap[String, Any]
  type YamlJavaData = java.util.Map[String, Any]

  def withMap(
      operation: YamlData => Unit
  ): YamlJavaData = {
    val map = new YamlData()
    operation.apply(map)
    map.asJava
  }

  def readString(data: YamlJavaData, key: String): Option[String] =
    data.asScala match {
      case v if v.get(key).exists(kv => kv.isInstanceOf[String]) =>
        val theString = v(key).asInstanceOf[String]
        debug(s"reading string with key $key: $theString")
        Some(theString)
      case _ =>
        debug(s"no entry found with key $key")
        None
    }
}
