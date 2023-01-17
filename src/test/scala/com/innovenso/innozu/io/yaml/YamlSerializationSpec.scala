package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.concepts.Enterprise
import org.scalatest.flatspec.AnyFlatSpec

class YamlSerializationSpec extends AnyFlatSpec {
  it should "serialize enterprises" in new EnterpriseArchitectureContext {
    val enterprises: List[Enterprise] =
      (1 to samples.randomInt(10)).map(i => samples.enterprise).toList
    println(serialize(EnterpriseYamlIO.write(enterprises)))
  }
}
