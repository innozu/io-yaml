package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  BusinessCapabilityYamlIO,
  EnterpriseYamlIO
}
import com.innovenso.innozu.io.yaml.properties.TitleYamlIO.YamlJavaData
import com.innovenso.townplanner.model.concepts.Enterprise
import org.scalatest.flatspec.AnyFlatSpec

class YamlDeserializationSpec extends AnyFlatSpec {
  it should "read enterprises from YAML" in new EnterpriseArchitectureContext {
    val yml: String =
      """enterprises:
        |- key: enterprise_1
        |  sortKey: '0001'
        |  title: Enterprise One
        |  description:
        |  - this is Enterprise One
        |- key: enterprise_2
        |  sortKey: '0002'
        |  title: Enterprise Two
        |  description:
        |  - this is Enterprise Two
        |  - it has twice as many descriptions as Enterprise One
        |  links:
        |  - type: Website
        |    url: https://innovenso.com
        |    title: Innovenso Website
        |  - type: API Documentation
        |    url: https://innozu.cloud
        |    title: Innozu API Documentation
        |  swot:
        |    strengths:
        |    - a first strength
        |    - a second strength
        |    opportunities:
        |    - the only opportunity
        |""".stripMargin

    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    EnterpriseYamlIO.read(data)
    println(townPlan)
  }

  it should "read business capabilities from YAML" in new EnterpriseArchitectureContext {
    val yml: String =
      """capabilities:
        |- key: capability_1
        |  title: Business Capability 1
        |  description:
        |  - This is a business capability
        |- key: capability_2
        |  title: Business Capability 2
        |  tolerate: This capability should be tolerated
        |  externalIds:
        |    - id: '123'
        |      system: Sparx Enterprise Architect
        |    - id: '456'
        |      system: Confluence
        |""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    BusinessCapabilityYamlIO.read(data)
    println(townPlan)
  }
}
