package com.innovenso.innozu.io.yaml.concepts

import ModelComponentYamlIO.{readSortKey, withKey}
import com.innovenso.innozu.io.yaml.properties.{
  DescriptionYamlIO,
  LinksYamlIO,
  SWOTYamlIO,
  TitleYamlIO
}
import com.innovenso.innozu.io.yaml.YamlIO
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.Enterprise

import scala.jdk.CollectionConverters.{
  ListHasAsScala,
  MapHasAsScala,
  SeqHasAsJava
}

object EnterpriseYamlIO extends YamlIO {
  private val ENTERPRISES = "enterprises"
  def write(enterprise: Enterprise): YamlJavaData =
    withMap { map =>
      debug(s"writing enterprise $enterprise")
      ModelComponentYamlIO.write(enterprise, map)
      TitleYamlIO.write(enterprise, map)
      DescriptionYamlIO.write(enterprise, map)
      LinksYamlIO.write(enterprise, map)
      SWOTYamlIO.write(enterprise, map)
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
      ) as { it =>
        it has TitleYamlIO.read(data)
        DescriptionYamlIO.readMany(data).foreach(d => it has d)
        LinksYamlIO.readMany(data).foreach(l => it has l)
        SWOTYamlIO.readMany(data).foreach(s => it has s)
      }
    }
  )
}
