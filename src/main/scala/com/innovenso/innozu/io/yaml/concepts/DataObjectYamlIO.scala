package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.properties._
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{
  AggregateRoot,
  BusinessCapability,
  Command,
  DataObject,
  DataObjectConfigurer,
  Entity,
  Event,
  Projection,
  Query,
  ValueObject
}
import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.{Key, SortKey}

object DataObjectYamlIO extends ModelComponentYamlIO[DataObject] {
  val KEY = "data"

  override def readOne(data: DataObjectYamlIO.YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): DataObject = {
    val body = { it: DataObjectConfigurer[_ <: DataObject] =>
      it has TitleYamlIO.read(data)
      DescriptionYamlIO.readMany(data).foreach(d => it has d)
      LinksYamlIO.readMany(data).foreach(l => it has l)
      ExternalIdYamlIO
        .readMany(data)
        .foreach(i => it isIdentifiedAs i.id on i.externalSystemName)
      FatherTimeYamlIO
        .readMany(data)
        .foreach(fatherTime => it has fatherTime on fatherTime.date)
      DataAttributeYamlIO
        .readMany(data)
        .foreach(dataAttribute => it has dataAttribute)
      it has DataClassificationYamlIO.read(data)
    }

    val sortKey: SortKey = readSortKey(data)
    val dataObject: Option[DataObject] =
      readString(data, "type").flatMap(typeString =>
        DataObject.fromString(typeString, key, sortKey)
      )
    dataObject.map {
      case e: Entity         => ea describes e as { it => body.apply(it) }
      case v: ValueObject    => ea describes v as { it => body.apply(it) }
      case ar: AggregateRoot => ea describes ar as { it => body.apply(it) }
      case c: Command        => ea describes c as { it => body.apply(it) }
      case e: Event          => ea describes e as { it => body.apply(it) }
      case q: Query          => ea describes q as { it => body.apply(it) }
      case p: Projection     => ea describes p as { it => body.apply(it) }
      case _ => ea describes (Entity(key, sortKey)) as { it => body.apply(it) }
    }.get
  }

  override def writeExtraProperties(
      modelComponent: DataObject,
      data: DataObjectYamlIO.YamlData
  ): Unit = {
    data.put("type", modelComponent.dataObjectType)
  }
}
