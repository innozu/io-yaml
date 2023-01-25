package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}
import org.yaml.snakeyaml.{DumperOptions, Yaml}

import scala.collection.mutable
import scala.jdk.CollectionConverters.MutableMapHasAsJava

trait EnterpriseArchitectureContext {
  implicit val ea: EnterpriseArchitecture = EnterpriseArchitecture()
  val options = new DumperOptions()
  options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
  val yaml: Yaml = new Yaml(options)

  def exists[ModelComponentType <: ModelComponent](
      modelComponent: ModelComponentType
  ): Boolean = {
    ea.townPlan.has(modelComponent)
  }

  def townPlan: TownPlan = ea.townPlan

  def serialize(data: java.util.Map[String, Any]): String =
    yaml.dump(data)
}
