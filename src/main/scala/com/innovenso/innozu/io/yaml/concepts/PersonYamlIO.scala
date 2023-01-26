package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{Actor, Person}
import com.innovenso.townplanner.model.meta.Key

object PersonYamlIO extends ModelComponentYamlIO[Person] {
  val KEY = "people"

  override def readOne(data: PersonYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): Person =
    ea describes Person(key = key, sortKey = readSortKey(data)) as { it =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
      LinksYamlIO.readMany(data).foreach(l => it has l)
      SWOTYamlIO.readMany(data).foreach(s => it has s)
      ExternalIdYamlIO
        .readMany(data)
        .foreach(i => it isIdentifiedAs i.id on i.externalSystemName)
    }
}
