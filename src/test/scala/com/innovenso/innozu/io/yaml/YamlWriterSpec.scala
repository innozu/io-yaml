package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.concepts.properties.Title
import com.innovenso.townplanner.model.concepts.relationships.{
  One,
  OneOrMore,
  ZeroOrMore,
  ZeroOrOne
}
import com.innovenso.townplanner.model.concepts.{
  Actor,
  AggregateRoot,
  ArchitectureBuildingBlock,
  BusinessCapability,
  Command,
  Database,
  Enterprise,
  Entity,
  Event,
  Framework,
  ItPlatform,
  ItSystem,
  ItSystemIntegration,
  Language,
  Microservice,
  Organisation,
  Person,
  Platform,
  PlatformLayer,
  Projection,
  Query,
  Tag,
  Team,
  Technique,
  Tool,
  ValueObject
}
import fish.genius.logging.Loggable
import org.scalatest.flatspec.AnyFlatSpec

class YamlWriterSpec extends AnyFlatSpec with Loggable {
  it should "write YAML files" in new EnterpriseArchitectureContext {
    val enterprise = ea hasRandom Enterprise()
    val tag1 = ea hasRandom Tag()
    val tag2 = ea hasRandom Tag()
    val capability1 = ea describesRandom BusinessCapability() as { it =>
      it serves enterprise
      it isTagged tag1
    }
    val capability2 = ea describesRandom BusinessCapability() as { it =>
      it serves capability1
      it isTagged tag2
    }
    val capability3 = ea describesRandom BusinessCapability() as { it =>
      it serves capability1
    }

    val person1 = ea hasRandomActor Person()
    val actor1 = ea hasRandomActor Actor()
    val team1 = ea hasRandomActor Team()
    val organisation1 = ea hasRandomActor Organisation()

    val buildingBlock1 = ea describesRandom ArchitectureBuildingBlock() as {
      it =>
        it realizes capability2
    }
    val buildingBlock2 = ea describesRandom ArchitectureBuildingBlock() as {
      it =>
        it realizes capability3
    }

    val aggregateRoot1 = ea hasRandomDataObject AggregateRoot()
    val entity1 = ea describesRandomDataObject Entity() as { it =>
      it has (aggregateRoot1, OneOrMore, One, "")
    }
    val entity2 = ea describesRandomDataObject Entity() as { it =>
      it has (aggregateRoot1, OneOrMore, One, "")
    }
    val valueObject1 = ea describesRandomDataObject ValueObject() as { it =>
      it has (entity1, ZeroOrMore, ZeroOrOne, "")
    }
    val event1 = ea hasRandomDataObject Event()
    val command1 = ea hasRandomDataObject Command()
    val query1 = ea hasRandomDataObject Query()
    val projection1 = ea hasRandomDataObject Projection()

    val platform1 = ea hasRandom ItPlatform()

    val system1 = ea describesRandom ItSystem() as { it =>
      it isPartOf platform1
      it realizes buildingBlock1
      it owns (aggregateRoot1, "owns")
      it produces (event1, "produces")
    }
    val container1 = ea describesRandomContainer Microservice() as { it =>
      it isPartOf system1
    }
    val container2 = ea describesRandomContainer Database() as { it =>
      it isPartOf system1
      it isUsedBy container1
    }

    val system2 = ea hasRandom ItSystem()

    val integration1 =
      ea describes ItSystemIntegration() between system1 and system2 as { it =>
        it has Title("System Integration")
      }

    -->(s"integrations: ${townPlan.systemIntegrations}")

    val tool: Tool = ea hasRandomTech Tool()
    val technique: Technique = ea hasRandomTech Technique()
    val language: Language = ea hasRandomTech Language()
    val framework: Framework = ea hasRandomTech Framework()
    val platform: Platform = ea hasRandomTech Platform()

    val layer: PlatformLayer = ea hasRandom PlatformLayer()

    val outputDirectory = YamlWriter.write()
    assert(outputDirectory.nonEmpty)
    -->(s"output directory: ${outputDirectory.get}")
  }
}
