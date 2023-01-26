package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  ActorYamlIO,
  ArchitectureBuildingBlockYamlIO,
  BusinessCapabilityYamlIO,
  DataObjectYamlIO,
  EnterpriseYamlIO,
  ItPlatformYamlIO,
  OrganisationYamlIO,
  PersonYamlIO,
  TeamYamlIO
}
import com.innovenso.townplanner.model.concepts.{
  Actor,
  ArchitectureBuildingBlock,
  BusinessCapability,
  Enterprise,
  Entity,
  ItPlatform,
  Organisation,
  Person,
  Tag,
  Team,
  ValueObject
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

  it should "serialize business actors" in new EnterpriseArchitectureContext {
    val people: List[Person] = samples.times(5, i => ea hasRandomActor Person())
    val teams: List[Team] = samples.times(5, i => ea hasRandomActor Team())
    val organisations: List[Organisation] =
      samples.times(5, i => ea hasRandomActor Organisation())
    val actors: List[Actor] = samples.times(5, i => ea hasRandomActor Actor())
    println(serialize(PersonYamlIO.write(people)))
    println(serialize(TeamYamlIO.write(teams)))
    println(serialize(OrganisationYamlIO.write(organisations)))
    println(serialize(ActorYamlIO.write(actors)))
  }

  it should "serialize data objects" in new EnterpriseArchitectureContext {
    val entity = ea hasRandomDataObject Entity()
    val valueObject = ea hasRandomDataObject ValueObject()
    val samples = List(entity, valueObject)
    println(serialize(DataObjectYamlIO.write(samples)))
  }

  it should "serialize platforms" in new EnterpriseArchitectureContext {
    val platforms: List[ItPlatform] =
      samples.times(10, i => ea hasRandom ItPlatform())
    println(serialize(ItPlatformYamlIO.write(platforms)))
  }
}
