package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  BusinessCapabilityYamlIO,
  EnterpriseYamlIO
}
import com.innovenso.townplanner.model.concepts.{BusinessCapability, Enterprise}
import org.scalatest.flatspec.AnyFlatSpec

class YamlSerializationSpec extends AnyFlatSpec {
  it should "serialize enterprises" in new EnterpriseArchitectureContext {
    val enterprises: List[Enterprise] =
      (1 to samples.randomInt(10)).map(i => samples.enterprise).toList
    println(serialize(EnterpriseYamlIO.write(enterprises)))
  }

  it should "serialize business capabilities" in new EnterpriseArchitectureContext {
    val capabilities: List[BusinessCapability] =
      (1 to samples.randomInt(10)).map(i => samples.capability()).toList
    println(serialize(BusinessCapabilityYamlIO.write(capabilities)))
  }
}
