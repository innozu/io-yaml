package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{BusinessCapability, ItPlatform}
import com.innovenso.townplanner.model.meta.Key

object ItPlatformYamlIO extends ModelComponentYamlIO[ItPlatform] {
  val KEY = "platforms"

  override def readOne(data: ItPlatformYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): ItPlatform =
    ea describes ItPlatform(key = key, sortKey = readSortKey(data)) as { it =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
      it should ArchitectureVerdictYamlIO.read(data)
      LinksYamlIO.readMany(data).foreach(l => it has l)
      it ratesImpactAs CriticalityYamlIO.read(data)
      SWOTYamlIO.readMany(data).foreach(s => it has s)
      ExternalIdYamlIO
        .readMany(data)
        .foreach(i => it isIdentifiedAs i.id on i.externalSystemName)
      FatherTimeYamlIO
        .readMany(data)
        .foreach(fatherTime => it has fatherTime on fatherTime.date)
      ResilienceMeasureYamlIO.readMany(data).foreach(r => it provides r)
    }
}
