package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  ArchitectureBuildingBlockYamlIO,
  BusinessCapabilityYamlIO,
  EnterpriseYamlIO
}
import com.innovenso.townplanner.model.concepts.{
  ArchitectureBuildingBlock,
  BusinessCapability,
  Enterprise,
  Tag
}
import com.innovenso.townplanner.model.samples
import org.scalatest.flatspec.AnyFlatSpec

class YamlSerializationSpec extends AnyFlatSpec {
  it should "serialize enterprises" in new EnterpriseArchitectureContext {
    val enterprises: List[Enterprise] =
      (1 to samples.randomInt(10)).map(i => ea hasRandom Enterprise()).toList
    println(serialize(EnterpriseYamlIO.write(enterprises)))
  }

  it should "serialize business capabilities" in new EnterpriseArchitectureContext {
    val tags: List[Tag] =
      (1 to samples.randomInt(5)).map(i => ea hasRandom Tag()).toList
    val capabilities: List[BusinessCapability] =
      (1 to samples.randomInt(10))
        .map(i =>
          ea describesRandom BusinessCapability() as { it =>
            tags.foreach(it.isTagged)
          }
        )
        .toList
    println(serialize(BusinessCapabilityYamlIO.write(capabilities)))
  }

  it should "serialize architecture building blocks" in new EnterpriseArchitectureContext {
    val buildingBlocks: List[ArchitectureBuildingBlock] =
      samples.times(10, i => ea hasRandom ArchitectureBuildingBlock())
    println(serialize(ArchitectureBuildingBlockYamlIO.write(buildingBlocks)))
  }
}
