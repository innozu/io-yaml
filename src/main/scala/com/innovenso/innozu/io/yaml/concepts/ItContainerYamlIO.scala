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
      APIYamlIO.readOne(data).foreach(it.has)
    }

    val sortKey: SortKey = readSortKey(data)
    val container: Option[ItContainer] =
      readString(data, "type").flatMap(typeString =>
        ItContainer.fromString(typeString, key, sortKey)
      )
    container.map {
      case m: Microservice => ea describes m as { it => body.apply(it) }
      case d: Database     => ea describes d as { it => body.apply(it) }
      case s: Service      => ea describes s as { it => body.apply(it) }
      case f: Function     => ea describes f as { it => body.apply(it) }
      case f: Filesystem   => ea describes f as { it => body.apply(it) }
      case q: Queue        => ea describes q as { it => body.apply(it) }
      case t: Topic        => ea describes t as { it => body.apply(it) }
      case e: EventStream  => ea describes e as { it => body.apply(it) }
      case g: Gateway      => ea describes g as { it => body.apply(it) }
      case p: Proxy        => ea describes p as { it => body.apply(it) }
      case f: Firewall     => ea describes f as { it => body.apply(it) }
      case c: Cache        => ea describes c as { it => body.apply(it) }
      case w: WebUI        => ea describes w as { it => body.apply(it) }
      case m: MobileUI     => ea describes m as { it => body.apply(it) }
      case d: DesktopUI    => ea describes d as { it => body.apply(it) }
      case w: WatchUI      => ea describes w as { it => body.apply(it) }
      case t: TerminalUI   => ea describes t as { it => body.apply(it) }
      case s: SmartTVUI    => ea describes s as { it => body.apply(it) }
      case b: Batch        => ea describes b as { it => body.apply(it) }
    }.get
  }

  override def writeExtraProperties(
      modelComponent: ItContainer,
      data: ItContainerYamlIO.YamlData
  ): Unit = {
    data.put("type", modelComponent.containerType)
  }
}
