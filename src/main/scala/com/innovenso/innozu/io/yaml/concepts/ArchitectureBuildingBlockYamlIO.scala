package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties.{
  ArchitectureVerdictYamlIO,
  CriticalityYamlIO,
  DescriptionYamlIO,
  ExternalIdYamlIO,
  LinksYamlIO,
  SWOTYamlIO,
  TagPropertyYamlIO,
  TitleYamlIO
}
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.ArchitectureBuildingBlock
import com.innovenso.townplanner.model.meta.Key

object ArchitectureBuildingBlockYamlIO
    extends ModelComponentYamlIO[ArchitectureBuildingBlock] {
  val KEY = "buildingBlocks"

  override def readOne(
      data: ArchitectureBuildingBlockYamlIO.YamlJavaData,
      key: Key
  )(implicit
      ea: EnterpriseArchitecture
  ): ArchitectureBuildingBlock =
    ea describes ArchitectureBuildingBlock(
      key = key,
      sortKey = readSortKey(data)
    ) as { it =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
      it should ArchitectureVerdictYamlIO.read(data)
      it ratesImpactAs CriticalityYamlIO.read(data)
      LinksYamlIO.readMany(data).foreach(l => it has l)
      SWOTYamlIO.readMany(data).foreach(s => it has s)
      ExternalIdYamlIO
        .readMany(data)
        .foreach(i => it isIdentifiedAs i.id on i.externalSystemName)
    }

}
