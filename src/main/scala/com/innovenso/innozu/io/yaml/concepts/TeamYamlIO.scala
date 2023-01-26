package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{Person, Team}
import com.innovenso.townplanner.model.meta.Key

object TeamYamlIO extends ModelComponentYamlIO[Team] {
  val KEY = "teams"

  override def readOne(data: TeamYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): Team =
    ea describes Team(key = key, sortKey = readSortKey(data)) as { it =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
      LinksYamlIO.readMany(data).foreach(l => it has l)
      SWOTYamlIO.readMany(data).foreach(s => it has s)
      ExternalIdYamlIO
        .readMany(data)
        .foreach(i => it isIdentifiedAs i.id on i.externalSystemName)
    }
}
