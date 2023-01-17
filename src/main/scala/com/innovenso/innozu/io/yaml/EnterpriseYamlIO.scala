package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.meta.Key
import org.yaml.snakeyaml.Yaml

import scala.collection.mutable
import scala.collection.mutable.LinkedHashMap
import scala.jdk.CollectionConverters.{
  ListHasAsScala,
  MapHasAsScala,
  MutableMapHasAsJava,
  SeqHasAsJava
}
import ModelComponentYamlIO._

object EnterpriseYamlIO extends YamlIO {
  private val ENTERPRISES = "enterprises"
  def write(enterprise: Enterprise): YamlJavaData =
    withMap { map =>
      debug(s"writing enterprise $enterprise")
      ModelComponentYamlIO.write(enterprise, map)
      TitleYamlIO.write(enterprise, map)
      DescriptionYamlIO.write(enterprise, map)
    }

  def write(enterprises: List[Enterprise]): YamlJavaData =
    withMap { map =>
      debug(s"writing enterprises $enterprises")
      map.put(ENTERPRISES, enterprises.map(write).asJava)
    }

  def read(data: YamlJavaData)(implicit ea: EnterpriseArchitecture): Unit =
    data.asScala.get(ENTERPRISES).foreach {
      case list: java.util.List[_] =>
        debug(s"reading enterprises")
        list.asScala
          .filter(_.isInstanceOf[YamlJavaData])
          .map(_.asInstanceOf[YamlJavaData])
          .foreach(readOne)
      case _ =>
        debug(s"no enterprises available")
        ()
    }

  def readOne(data: YamlJavaData)(implicit
      ea: EnterpriseArchitecture
  ): Unit = withKey(
    data,
    { key =>
      debug(s"creating enterprise $key")
      ea describes Enterprise(
        key = key,
        sortKey = readSortKey(data)
      ) as { it => }
    }
  )
}
