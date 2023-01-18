package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.TitleYamlIO.YamlJavaData
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
        |""".stripMargin

    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    EnterpriseYamlIO.read(data)
    println(townPlan)
  }
}
