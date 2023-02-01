package com.innovenso.innozu.io.yaml.concepts

import com.innovenso.innozu.io.yaml.YamlIO
import com.innovenso.innozu.io.yaml.concepts.BusinessCapabilityYamlIO.withMap
import com.innovenso.innozu.io.yaml.concepts.EnterpriseYamlIO.YamlJavaData
import com.innovenso.innozu.io.yaml.properties.{
  APIYamlIO,
  ArchitectureVerdictYamlIO,
  CriticalityYamlIO,
  DataAttributeYamlIO,
  DataClassificationYamlIO,
  DescriptionYamlIO,
  ExternalIdYamlIO,
  FatherTimeYamlIO,
  LinksYamlIO,
  PlatformLayerPropertyYamlIO,
  ResilienceMeasureYamlIO,
  SWOTYamlIO,
  TagPropertyYamlIO,
  TitleYamlIO
}
import com.innovenso.innozu.io.yaml.relationships.{
  AccessingYamlIO,
  AssociationYamlIO,
  CompositionYamlIO,
  ConsumingYamlIO,
  DataRelationshipYamlIO,
  DeliveryYamlIO,
  FlowYamlIO,
  ImplementationYamlIO,
  InfluencingYamlIO,
  KnowledgeYamlIO,
  OwningYamlIO,
  ProcessingYamlIO,
  ProducingYamlIO,
  RealizingYamlIO,
  ServingYamlIO,
  StakeholderYamlIO,
  TransportingYamlIO,
  TriggeringYamlIO
}
import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.properties.{
  ArchitectureVerdict,
  CanConfigureTitle,
  HasAPI,
  HasArchitectureVerdict,
  HasCriticality,
  HasDataAttributes,
  HasDataClassification,
  HasDescription,
  HasExternalIds,
  HasFatherTime,
  HasLinks,
  HasPlatformLayerProperties,
  HasResilienceMeasures,
  HasSWOT,
  HasTagProperties,
  HasTitle
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAccess,
  CanBeAssociated,
  CanBeComposedOf,
  CanBeFlowSource,
  CanBeStakeholder,
  CanConsume,
  CanDeliver,
  CanHaveDataRelationship,
  CanImplement,
  CanInfluence,
  CanKnow,
  CanOwn,
  CanProcess,
  CanProduce,
  CanRealize,
  CanServe,
  CanTransport,
  CanTrigger,
  DataRelationship
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

  def write(modelComponents: List[ModelComponentType])(implicit
      ea: EnterpriseArchitecture
  ): YamlJavaData =
    withMap { map =>
      debug(s"writing $modelComponents")
      map.put(KEY, modelComponents.map(write).asJava)
    }
  def write(modelComponent: ModelComponentType)(implicit
      ea: EnterpriseArchitecture
  ): YamlJavaData = withMap { map =>
    debug(s"writing $modelComponent")
    writeKeys(modelComponent, map)
    writeExtraProperties(modelComponent, map)
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
    modelComponent match {
      case hasCriticality: HasCriticality =>
        CriticalityYamlIO.write(hasCriticality, map)
      case _ =>
    }
    modelComponent match {
      case hasExternalIds: HasExternalIds =>
        ExternalIdYamlIO.write(hasExternalIds, map)
      case _ =>
    }
    modelComponent match {
      case hasTagProperties: HasTagProperties =>
        TagPropertyYamlIO.write(hasTagProperties, map)
      case _ =>
    }
    modelComponent match {
      case hasFatherTime: HasFatherTime =>
        FatherTimeYamlIO.write(hasFatherTime, map)
      case _ =>
    }
    modelComponent match {
      case hasDataAttributes: HasDataAttributes =>
        DataAttributeYamlIO.write(hasDataAttributes, map)
      case _ =>
    }
    modelComponent match {
      case hasDataClassification: HasDataClassification =>
        DataClassificationYamlIO.write(hasDataClassification, map)
      case _ =>
    }
    modelComponent match {
      case hasResilienceMeasures: HasResilienceMeasures =>
        ResilienceMeasureYamlIO.write(hasResilienceMeasures, map)
      case _ =>
    }
    modelComponent match {
      case hasPlatformLayerProperties: HasPlatformLayerProperties =>
        PlatformLayerPropertyYamlIO.write(hasPlatformLayerProperties, map)
      case _ =>
    }
    modelComponent match {
      case hasAPI: HasAPI => APIYamlIO.write(hasAPI, map)
      case _              =>
    }
    modelComponent match {
      case canAccess: CanAccess => AccessingYamlIO.write(canAccess, map)
      case _                    =>
    }
    modelComponent match {
      case canBeAssociated: CanBeAssociated =>
        AssociationYamlIO.write(canBeAssociated, map)
      case _ =>
    }
    modelComponent match {
      case canBeComposedOf: CanBeComposedOf =>
        CompositionYamlIO.write(canBeComposedOf, map)
      case _ =>
    }
    modelComponent match {
      case canConsume: CanConsume =>
        ConsumingYamlIO.write(canConsume, map)
      case _ =>
    }
    modelComponent match {
      case canHaveDataRelationship: CanHaveDataRelationship =>
        DataRelationshipYamlIO.write(canHaveDataRelationship, map)
      case _ =>
    }
    modelComponent match {
      case canDeliver: CanDeliver =>
        DeliveryYamlIO.write(canDeliver, map)
      case _ =>
    }
    modelComponent match {
      case canBeFlowSource: CanBeFlowSource =>
        FlowYamlIO.write(canBeFlowSource, map)
      case _ =>
    }
    modelComponent match {
      case s: CanImplement =>
        ImplementationYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanInfluence =>
        InfluencingYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanKnow =>
        KnowledgeYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanOwn =>
        OwningYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanProcess =>
        ProcessingYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanProduce =>
        ProducingYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanRealize =>
        RealizingYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanServe =>
        ServingYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanBeStakeholder =>
        StakeholderYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanTransport =>
        TransportingYamlIO.write(s, map)
      case _ =>
    }
    modelComponent match {
      case s: CanTrigger =>
        TriggeringYamlIO.write(s, map)
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
            .flatMap(d => withKey(d, key => readOneWithRelationships(d, key)))
            .toList
        case _ =>
          debug(s"no $KEY available")
          Nil
      }
      .getOrElse(Nil)

  def readOne(data: YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): ModelComponentType

  private def readOneWithRelationships(data: YamlJavaData, key: Key)(implicit
      ea: EnterpriseArchitecture
  ): ModelComponentType = {
    val modelComponent: ModelComponentType = readOne(data, key)
    modelComponent match {
      case c: CanAccess =>
        AccessingYamlIO
          .readMany(data, c)
      case _ =>
    }
    modelComponent match {
      case a: CanBeAssociated => AssociationYamlIO.readMany(data, a)
      case _                  =>
    }
    modelComponent match {
      case c: CanBeComposedOf => CompositionYamlIO.readMany(data, c)
      case _                  =>
    }
    modelComponent match {
      case c: CanConsume => ConsumingYamlIO.readMany(data, c)
      case _             =>
    }
    modelComponent match {
      case d: CanHaveDataRelationship =>
        DataRelationshipYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanDeliver =>
        DeliveryYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanBeFlowSource =>
        FlowYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanImplement =>
        ImplementationYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanInfluence =>
        InfluencingYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanKnow =>
        KnowledgeYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanOwn =>
        OwningYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanProcess =>
        ProcessingYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanProduce =>
        ProducingYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanRealize =>
        RealizingYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanServe =>
        ServingYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanBeStakeholder =>
        StakeholderYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanTrigger =>
        TriggeringYamlIO.readMany(data, d)
      case _ =>
    }
    modelComponent match {
      case d: CanTransport =>
        TransportingYamlIO.readMany(data, d)
      case _ =>
    }

    modelComponent
  }

  def writeKeys(
      modelComponent: ModelComponent,
      data: YamlData
  ): Unit = {
    debug(s"writing model component properties: key and sortKey")
    data.put("key", modelComponent.key.value)
  }

  def writeExtraProperties(
      modelComponent: ModelComponentType,
      data: YamlData
  ): Unit = {}

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
