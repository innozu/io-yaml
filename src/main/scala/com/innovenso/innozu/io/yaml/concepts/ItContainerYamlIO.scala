package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.meta.{Key, SortKey}

object ItContainerYamlIO extends ModelComponentYamlIO[ItContainer] {
  val KEY = "containers"

  override def readOne(data: ItContainerYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): ItContainer = {
    val body = { it: ItContainerConfigurer[_ <: ItContainer] =>
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

    val sortKey: SortKey = readSortKey(data)
    val container: Option[ItContainer] =
      readString(data, "type").flatMap(typeString =>
        ItContainer.fromString(typeString, key, sortKey)
      )
    container.map {
      case m: Microservice => ea describes m as { it => body.apply(it) }
      case d: Database => 
    }.get
  }

  override def writeExtraProperties(
      modelComponent: ItContainer,
      data: ItContainerYamlIO.YamlData
  ): Unit = {
    data.put("type", modelComponent.containerType)
  }
}
