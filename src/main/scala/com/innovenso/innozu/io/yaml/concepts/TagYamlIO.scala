package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{BusinessCapability, Tag}
import com.innovenso.townplanner.model.meta.Key

object TagYamlIO extends ModelComponentYamlIO[Tag] {
  val KEY = "tags"

  override def readOne(data: TagYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): Tag =
    ea describes Tag(key = key, sortKey = readSortKey(data)) as { it =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
    }
}
