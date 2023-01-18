package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.YamlIO
import com.innovenso.innozu.io.yaml.properties.{ArchitectureVerdictYamlIO, DescriptionYamlIO, LinksYamlIO, SWOTYamlIO, TitleYamlIO}
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.BusinessCapability
import com.innovenso.townplanner.model.meta.Key

object BusinessCapabilityYamlIO extends ModelComponentYamlIO[BusinessCapability] {
  val KEY = "capabilities"

  override def readOne(data: BusinessCapabilityYamlIO.YamlJavaData, key: Key)(implicit ea: EnterpriseArchitecture): BusinessCapability = ea describes BusinessCapability(key = key, sortKey = readSortKey(data)) as { it =>
    it has TitleYamlIO.read(data)
    DescriptionYamlIO.readMany(data).foreach(d => it has d)
    it should ArchitectureVerdictYamlIO.read(data)
    LinksYamlIO.readMany(data).foreach(l => it has l)
    SWOTYamlIO.readMany(data).foreach(s => it has s)
  }
}
