package com.innovenso.innozu.io.yaml

import fish.genius.logging.Loggable

import scala.collection.mutable
import scala.jdk.CollectionConverters.{
  ListHasAsScala,
  MapHasAsScala,
  MutableMapHasAsJava
}

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

  def readStrings(data: YamlJavaData, key: String): List[String] =
    data.asScala match {
      case v if v.get(key).exists(kv => kv.isInstanceOf[java.util.List[_]]) =>
        val theList = v(key).asInstanceOf[java.util.List[_]]
        debug(s"reading list  of strings with key $key: $theList")
        theList.asScala
          .filter(_.isInstanceOf[String])
          .map(_.asInstanceOf[String])
          .toList
      case _ =>
        debug(s"no entry found with key $key")
        Nil
    }

  def readMaps(data: YamlJavaData, key: String): List[YamlJavaData] =
    data.asScala match {
      case v if v.get(key).exists(kv => kv.isInstanceOf[java.util.List[_]]) =>
        val theList = v(key).asInstanceOf[java.util.List[_]]
        debug(s"reading list of maps with key $key: $theList")
        theList.asScala
          .filter(_.isInstanceOf[java.util.Map[_, _]])
          .map(_.asInstanceOf[YamlJavaData])
          .toList
      case _ =>
        debug(s"no entry found with key $key")
        Nil
    }

  def readMap(data: YamlJavaData, key: String): Option[YamlJavaData] =
    data.asScala match {
      case v if v.get(key).exists(kv => kv.isInstanceOf[java.util.Map[_, _]]) =>
        val theMap = v(key).asInstanceOf[YamlJavaData]
        debug(s"reading map with key $key: $theMap")
        Some(theMap)
      case _ =>
        debug(s"no entry found with key $key")
        None
    }

}
