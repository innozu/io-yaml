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
  TagYamlIO,
  TeamYamlIO
}
import com.innovenso.innozu.io.yaml.concepts.TagYamlIO.YamlJavaData
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{
  Actor,
  AggregateRoot,
  Command,
  Entity,
  Event,
  Organisation,
  Person,
  Projection,
  Query,
  Team,
  ValueObject
}
import fish.genius.logging.Loggable
import org.yaml.snakeyaml.{DumperOptions, Yaml}

import java.io.{File, PrintWriter}
import java.nio.file.Path
import scala.annotation.unused
import scala.util.{Failure, Success, Try}

object YamlWriter extends Loggable {
  val options = new DumperOptions()
  options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
  val yaml: Yaml = new Yaml(options)

  def write()(implicit ea: EnterpriseArchitecture): Option[File] =
    inDirectory(Path.of("./yaml/")) { directory =>
      writeYaml(directory, "enterprises.yaml")(
        EnterpriseYamlIO.write(ea.townPlan.enterprises)
      )
      writeYaml(directory, "tags.yaml")(TagYamlIO.write(ea.townPlan.tags))
      writeYaml(directory, "business-capabilities.yaml")(
        BusinessCapabilityYamlIO.write(ea.townPlan.businessCapabilities)
      )
      inDirectory(new File(directory, "actors").toPath) { actorDirectory =>
        writeYaml(actorDirectory, "actors.yaml")(
          ActorYamlIO.write(ea.townPlan.components(classOf[Actor]))
        )
        writeYaml(actorDirectory, "teams.yaml")(
          TeamYamlIO.write(ea.townPlan.components(classOf[Team]))
        )
        writeYaml(actorDirectory, "organisations.yaml")(
          OrganisationYamlIO.write(
            ea.townPlan.components(classOf[Organisation])
          )
        )
        writeYaml(actorDirectory, "people.yaml")(
          PersonYamlIO.write(
            ea.townPlan.components(classOf[Person])
          )
        )
      }
      writeYaml(directory, "building-blocks.yml")(
        ArchitectureBuildingBlockYamlIO.write(
          ea.townPlan.architectureBuildingBlocks
        )
      )
      inDirectory(new File(directory, "data").toPath) { dataDirectory =>
        writeYaml(dataDirectory, "entities.yaml")(
          DataObjectYamlIO.write(ea.townPlan.components(classOf[Entity]))
        )
        writeYaml(dataDirectory, "aggregate-roots.yaml")(
          DataObjectYamlIO.write(ea.townPlan.components(classOf[AggregateRoot]))
        )
        writeYaml(dataDirectory, "value-objects.yaml")(
          DataObjectYamlIO.write(ea.townPlan.components(classOf[ValueObject]))
        )
        writeYaml(dataDirectory, "events.yaml")(
          DataObjectYamlIO.write(ea.townPlan.components(classOf[Event]))
        )
        writeYaml(dataDirectory, "commands.yaml")(
          DataObjectYamlIO.write(ea.townPlan.components(classOf[Command]))
        )
        writeYaml(dataDirectory, "queries.yaml")(
          DataObjectYamlIO.write(ea.townPlan.components(classOf[Query]))
        )
        writeYaml(dataDirectory, "projections.yaml")(
          DataObjectYamlIO.write(ea.townPlan.components(classOf[Projection]))
        )
      }
      writeYaml(directory, "platforms.yaml")(
        ItPlatformYamlIO.write(ea.townPlan.platforms)
      )
      inDirectory(new File(directory, "systems").toPath) { systemDirectory =>
        ea.townPlan.systems.foreach(itSystem => {
          val yml =
            s"""${serializeYaml(
                ItSystemYamlIO.write(ea.townPlan.system(itSystem.key).toList)
              )}
              |${serializeYaml(
                ItContainerYamlIO.write(ea.townPlan.containers(itSystem))
              )}
              |""".stripMargin
          writeString(systemDirectory, s"${itSystem.key}.yaml")(yml)
        })
      }
      writeYaml(directory, "integrations.yaml")(
        ItSystemIntegrationYamlIO.write(ea.townPlan.systemIntegrations)
      )
    }

  private def inDirectory(
      targetPath: Path
  )(operation: File => Any): Option[File] =
    Try {
      val targetDirectory = targetPath.toFile
      debug(s"creating target directory ${targetDirectory.getAbsolutePath}")
      targetDirectory.mkdirs()
      operation.apply(targetDirectory)
      targetDirectory
    } match {
      case Success(value) => Some(value)
      case Failure(cause) =>
        exception(cause)
        None
    }

  private def serializeYaml(data: YamlJavaData) = yaml.dump(data)

  private def writeYaml(targetDirectory: File, filename: String)(
      data: YamlJavaData
  ): Option[File] = writeString(targetDirectory, filename)(serializeYaml(data))
  private def writeString(targetDirectory: File, filename: String)(
      yml: String
  ): Option[File] = Try {
    val targetFile = new File(targetDirectory, filename)
    val printWriter = new PrintWriter(targetFile)
    printWriter.write(yml)
    printWriter.flush()
    targetFile
  } match {
    case Success(value) => Some(value)
    case Failure(cause) =>
      exception(cause)
      None
  }

}
