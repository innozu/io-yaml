package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.YamlIO
import com.innovenso.innozu.io.yaml.concepts.BusinessCapabilityYamlIO.withMap
import com.innovenso.innozu.io.yaml.concepts.EnterpriseYamlIO.YamlJavaData
import com.innovenso.innozu.io.yaml.properties.{
  ArchitectureVerdictYamlIO,
  DescriptionYamlIO,
  LinksYamlIO,
  SWOTYamlIO,
  TitleYamlIO
}
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.properties.{
  ArchitectureVerdict,
  CanConfigureTitle,
  HasArchitectureVerdict,
  HasDescription,
  HasLinks,
  HasSWOT,
  HasTitle
}
import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.{Key, SortKey}

import scala.jdk.CollectionConverters.{
  ListHasAsScala,
  MapHasAsScala,
  SeqHasAsJava
}

trait ModelComponentYamlIO[ModelComponentType <: ModelComponent]
    extends YamlIO {

  def KEY: String

  def write(modelComponents: List[ModelComponentType]): YamlJavaData =
    withMap { map =>
      debug(s"writing $modelComponents")
      map.put(KEY, modelComponents.map(write).asJava)
    }
  def write(modelComponent: ModelComponentType): YamlJavaData = withMap { map =>
    debug(s"writing $modelComponent")
    writeKeys(modelComponent, map)
    modelComponent match {
      case hasTitle: HasTitle => TitleYamlIO.write(hasTitle, map)
      case _                  =>
    }
    modelComponent match {
      case hasDescription: HasDescription =>
        DescriptionYamlIO.write(hasDescription, map)
      case _ =>
    }
    modelComponent match {
      case hasLinks: HasLinks => LinksYamlIO.write(hasLinks, map)
      case _                  =>
    }
    modelComponent match {
      case hasSwots: HasSWOT => SWOTYamlIO.write(hasSwots, map)
      case _                 =>
    }
    modelComponent match {
      case hasArchitectureVerdict: HasArchitectureVerdict =>
        ArchitectureVerdictYamlIO.write(hasArchitectureVerdict, map)
      case _ =>
    }
  }

  def read(data: YamlJavaData)(implicit
      ea: EnterpriseArchitecture
  ): List[ModelComponentType] =
    data.asScala
      .get(KEY)
      .map {
        case list: java.util.List[_] =>
          debug(s"reading $KEY")
          list.asScala
            .filter(_.isInstanceOf[YamlJavaData])
            .map(_.asInstanceOf[YamlJavaData])
            .flatMap(d => withKey(d, key => readOne(d, key)))
            .toList
        case _ =>
          debug(s"no $KEY available")
          Nil
      }
      .getOrElse(Nil)

  def readOne(data: YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): ModelComponentType

  def writeKeys(
      modelComponent: ModelComponent,
      data: YamlData
  ): Unit = {
    debug(s"writing model component properties: key and sortKey")
    data.put("key", modelComponent.key.value)
    modelComponent.sortKey.value.foreach(sk => data.put("sortKey", sk))
  }

  def readKey(data: YamlJavaData): Key =
    readString(data, "key").map(Key.fromString).getOrElse(Key())

  def withKey(
      data: YamlJavaData,
      operation: Key => ModelComponentType
  ): Option[ModelComponentType] = data.asScala match {
    case v if v.get("key").exists(kv => kv.isInstanceOf[String]) =>
      Some(operation.apply(readKey(data)))
    case _ => None
  }

  def readSortKey(data: YamlJavaData): SortKey = readString(data, "sortKey")
    .map(sk => SortKey(Some(sk)))
    .getOrElse(SortKey.next)

}