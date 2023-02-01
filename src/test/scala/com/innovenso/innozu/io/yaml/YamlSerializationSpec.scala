package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  ActorYamlIO,
  ArchitectureBuildingBlockYamlIO,
  BusinessCapabilityYamlIO,
  DataObjectYamlIO,
  EnterpriseYamlIO,
  ItContainerYamlIO,
  ItPlatformYamlIO,
  ItSystemIntegrationYamlIO,
  ItSystemYamlIO,
  OrganisationYamlIO,
  PersonYamlIO,
  PlatformLayerYamlIO,
  TeamYamlIO,
  TechnologyYamlIO
}
import com.innovenso.townplanner.model.concepts.properties.Title
import com.innovenso.townplanner.model.concepts.relationships.Accessing
import com.innovenso.townplanner.model.concepts.{
  Actor,
  ArchitectureBuildingBlock,
  BusinessCapability,
  DataObject,
  Database,
  Enterprise,
  Entity,
  Framework,
  ItContainer,
  ItPlatform,
  ItSystem,
  ItSystemIntegration,
  Language,
  Microservice,
  Organisation,
  Person,
  Platform,
  PlatformLayer,
  Tag,
  Team,
  Technique,
  Tool,
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

  it should "serialize systems" in new EnterpriseArchitectureContext {
    val layer: PlatformLayer = ea hasRandom PlatformLayer()
    val systems: List[ItSystem] =
      samples.times(
        10,
        i =>
          ea describesRandom ItSystem() as { it =>
            it isOn layer
          }
      )
    println(serialize(ItSystemYamlIO.write(systems)))
  }

  it should "serialize system integrations" in new EnterpriseArchitectureContext {
    val system1: ItSystem = ea hasRandom ItSystem()
    val system2: ItSystem = ea hasRandom ItSystem()
    println(townPlan)
    val integration: ItSystemIntegration =
      ea describes ItSystemIntegration() between system1 and system2 as { it =>
        it has Title("hello")
      }

    println(serialize(ItSystemIntegrationYamlIO.write(List(integration))))
  }

  it should "serialize IT Containers" in new EnterpriseArchitectureContext {
    val container1: ItContainer = ea hasRandomContainer Microservice()
    val container2: ItContainer = ea hasRandomContainer Database()
    println(townPlan)
    println(serialize(ItContainerYamlIO.write(List(container1, container2))))
  }

  it should "serialize Technologies" in new EnterpriseArchitectureContext {
    val tool: Tool = ea hasRandomTech Tool()
    val technique: Technique = ea hasRandomTech Technique()
    val language: Language = ea hasRandomTech Language()
    val framework: Framework = ea hasRandomTech Framework()
    val platform: Platform = ea hasRandomTech Platform()
    println(
      serialize(
        TechnologyYamlIO.write(
          List(tool, technique, language, framework, platform)
        )
      )
    )
  }

  it should "serialize platform layers" in new EnterpriseArchitectureContext {
    val layer: PlatformLayer = ea hasRandom PlatformLayer()
    println(serialize(PlatformLayerYamlIO.write(List(layer))))
  }

  it should "serialize Accessing relationships" in new EnterpriseArchitectureContext {
    val dataObject: DataObject = ea hasRandomDataObject Entity()
    val system1: ItSystem = ea describesRandom ItSystem() as { it =>
      it accesses (dataObject, "accesses")
    }
    println(townPlan)
    println(
      serialize(
        ItSystemYamlIO.write(List(system1))
      )
    )
  }
}
