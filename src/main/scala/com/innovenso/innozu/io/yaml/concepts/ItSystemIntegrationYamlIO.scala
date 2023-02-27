package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{ItSystem, ItSystemIntegration}
import com.innovenso.townplanner.model.meta.Key

object ItSystemIntegrationYamlIO
    extends ModelComponentYamlIO[ItSystemIntegration] {
  val KEY = "integrations"

  override def writeExtraProperties(
      modelComponent: ItSystemIntegration,
      data: ItSystemIntegrationYamlIO.YamlData
  ): Unit = {
    data.put("system1", modelComponent.source.value)
    data.put("system2", modelComponent.target.value)
    data.put("dataObject", modelComponent.dataObject.value)
  }

  override def readOne(data: ItSystemIntegrationYamlIO.YamlJavaData, key: Key)(
      implicit ea: EnterpriseArchitecture
  ): ItSystemIntegration = readIntegration(data, key).getOrElse(
    ItSystemIntegration(source = Key(), target = Key())
  )

  def readIntegration(data: ItSystemIntegrationYamlIO.YamlJavaData, key: Key)(
      implicit ea: EnterpriseArchitecture
  ): Option[ItSystemIntegration] = for {
    system1Key <- readString(data, "system1").map(v => Key.fromString(v))
    system2Key <- readString(data, "system2").map(v => Key.fromString(v))
    dataObjectKey <- readString(data, "dataObject").map(v => Key.fromString(v))
    integration <- Some(
      ea describes ItSystemIntegration(
        key = key,
        source = system1Key,
        target = system2Key,
        dataObject = dataObjectKey,
        sortKey = readSortKey(data)
      ) as { it =>
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
        SecurityMeasuresYamlIO.readMany(data).foreach(s => it provides s)
        ThroughPutYamlIO.readVolume(data).foreach(it.has)
        ThroughPutYamlIO.readFrequency(data).foreach(it.has)
      }
    )
  } yield integration

}
