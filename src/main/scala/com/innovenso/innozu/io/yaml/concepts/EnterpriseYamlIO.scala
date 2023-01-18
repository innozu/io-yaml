package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties.{
  DescriptionYamlIO,
  LinksYamlIO,
  SWOTYamlIO,
  TitleYamlIO
}
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.meta.Key

import scala.jdk.CollectionConverters.{
  ListHasAsScala,
  MapHasAsScala,
  SeqHasAsJava
}

object EnterpriseYamlIO extends ModelComponentYamlIO[Enterprise] {
  override val KEY = "enterprises"

  override def readOne(data: YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): Enterprise = {
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

}
