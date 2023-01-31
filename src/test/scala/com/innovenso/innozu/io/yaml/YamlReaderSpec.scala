package com.innovenso.innozu.io.yaml

import org.scalatest.flatspec.AnyFlatSpec

import java.io.File

class YamlReaderSpec extends AnyFlatSpec {
  it should "read YAML files into the town plan" in new EnterpriseArchitectureContext {
    YamlReader.add(new File("./yaml/"))
    YamlReader.load()
    ea.townPlan.relationships.foreach(r => println(r))
  }
}
