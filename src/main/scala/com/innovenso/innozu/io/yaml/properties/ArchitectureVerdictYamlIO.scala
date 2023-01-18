package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  ArchitectureVerdict,
  BeEliminated,
  BeInvestedIn,
  BeMigrated,
  BeTolerated,
  HasArchitectureVerdict,
  HasTitle,
  Title,
  UnknownArchitectureVerdict
}

object ArchitectureVerdictYamlIO
    extends YamlPropertiesIO[ArchitectureVerdict, HasArchitectureVerdict] {
  override def write(
      hasArchitectureVerdict: HasArchitectureVerdict,
      data: YamlData
  ): Unit = {
    debug(
      s"writing architecture verdict ${hasArchitectureVerdict.architectureVerdict.name}"
    )
    if (!hasArchitectureVerdict.isUnknownArchitectureVerdict) {
      data.put(
        hasArchitectureVerdict.architectureVerdict.name.toLowerCase,
        hasArchitectureVerdict.architectureVerdict.description
      )
    }
  }

  def read(data: YamlJavaData): ArchitectureVerdict =
    readOne(data).getOrElse(UnknownArchitectureVerdict(""))

  override def readOne(
      data: ArchitectureVerdictYamlIO.YamlJavaData
  ): Option[ArchitectureVerdict] =
    readString(data, BeTolerated().name.toLowerCase)
      .map(BeTolerated)
      .orElse(
        readString(data, BeInvestedIn().name.toLowerCase).map(BeInvestedIn)
      )
      .orElse(readString(data, BeMigrated().name.toLowerCase).map(BeMigrated))
      .orElse(
        readString(data, BeEliminated().name.toLowerCase).map(BeEliminated)
      )
}
