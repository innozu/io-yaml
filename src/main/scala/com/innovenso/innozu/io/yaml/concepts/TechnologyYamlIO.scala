package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.meta.{Key, SortKey}

object TechnologyYamlIO extends ModelComponentYamlIO[Technology] {
  val KEY = "technology"

  override def readOne(data: TechnologyYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): Technology = {
    val body = { it: TechnologyRadarConfigurer[_ <: Technology] =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
      it should ArchitectureVerdictYamlIO.read(data)
      LinksYamlIO.readMany(data).foreach(l => it has l)
      SWOTYamlIO.readMany(data).foreach(s => it has s)
    }

    val tech: Option[Technology] =
      readString(data, "type").flatMap(typeString =>
        Technology.fromString(typeString, key)
      )
    tech.map {
      case t: Tool      => ea describes t as { it => body.apply(it) }
      case p: Platform  => ea describes p as { it => body.apply(it) }
      case t: Technique => ea describes t as { it => body.apply(it) }
      case l: Language  => ea describes l as { it => body.apply(it) }
      case f: Framework => ea describes f as { it => body.apply(it) }
      case _            => ea describes Framework() as { it => body.apply(it) }
    }.get
  }

  override def writeExtraProperties(
      modelComponent: Technology,
      data: TechnologyYamlIO.YamlData
  ): Unit = {
    data.put(
      "type",
      modelComponent match {
        case _: Tool      => "tool"
        case _: Technique => "technique"
        case _: Platform  => "platform"
        case _: Language  => "language"
        case _: Framework => "framework"
      }
    )
  }
}
