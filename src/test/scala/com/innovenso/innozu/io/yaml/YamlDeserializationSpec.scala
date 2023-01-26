package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  ActorYamlIO,
  ArchitectureBuildingBlockYamlIO,
  BusinessCapabilityYamlIO,
  DataObjectYamlIO,
  EnterpriseYamlIO,
  OrganisationYamlIO,
  PersonYamlIO,
  TeamYamlIO
}
import com.innovenso.innozu.io.yaml.properties.TitleYamlIO.YamlJavaData
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.meta.Key
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

  it should "read architecture building blocks from YAML" in new EnterpriseArchitectureContext {
    val yml: String =
      """buildingBlocks:
        |- key: building_block_1
        |  title: Building Block 1
        |  criticality:
        |    level: hazardous
        |    consequences: Bad things happen
        |  fatherTime:
        |    - date: '1143-09-09'
        |      event: In Development
        |      description: started development
        |    - date: '1151-12-15'
        |      event: In Production
        |      description: gone to production
        |""".stripMargin

    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    ArchitectureBuildingBlockYamlIO.read(data)
    println(townPlan)
    assert(
      townPlan.architectureBuildingBlocks.exists(abb =>
        abb.isHazardousCriticality && abb.criticality.consequences == "Bad things happen" && abb.lifeEvents.size == 2
      )
    )
  }

  it should "read business actors from YAML" in new EnterpriseArchitectureContext {
    val yml: String =
      """people:
        |- key: person_1
        |  title: Jurgen Lust
        |  description:
        |  - ligula pretium tincidunt legimus maluisset sapien persequeris elit varius oporteat
        |    eius sollicitudin suavitate cursus adversarium malorum eirmod
        |teams:
        |- key: team_1
        |  title: Team 1
        |organisations:
        |- key: org_1
        |  title: Organisation 1
        |actors:
        |- key: actor_1
        |  title: Actor 1
        |""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    PersonYamlIO.read(data)
    ActorYamlIO.read(data)
    TeamYamlIO.read(data)
    OrganisationYamlIO.read(data)
    println(townPlan)
    assert(
      townPlan.businessActors.exists(ba => ba.title.value == "Jurgen Lust")
    )

  }

  it should "read data objects from YAML" in new EnterpriseArchitectureContext {
    val yml = """data:
                |- key: entity_1
                |  sortKey: '000000001'
                |  type: entity
                |  title: Delicata
                |  description:
                |  - solum etiam porttitor antiopam sagittis menandri ornatus errem dolor nam mi mentitum
                |  - ornare accumsan periculis recteque eius omittam dissentiunt volumus ludus vero
                |    inceptos ipsum adversarium oporteat evertitur ac habitasse urbanitas commune phasellus
                |    ponderum signiferumque menandri quo petentium
                |  - et aliquid suspendisse ornatus signiferumque mutat ridens viris quot blandit vero
                |    unum efficitur elementum liber doctus pertinacia vestibulum tritani arcu postulant
                |    sapientem graeco quisque saperet mutat gubergren interpretaris aliquet delicata
                |    donec accusata placerat etiam legere brute
                |  - quod commodo populo nam ornare ponderum vitae ubique harum utroque referrentur
                |    habitant erat assueverit verterem montes detraxit alienum liber quem interdum
                |    lobortis mauris atqui wisi qualisque dapibus eius suavitate duis menandri pellentesque
                |    tibique utroque dolorem condimentum adversarium a adipisci quaestio quis suscipit
                |    error atqui dicit adhuc corrumpit offendit urna ad persequeris dapibus lobortis
                |    egestas elitr dictum melius dissentiunt causae mel
                |  links:
                |  - type: Development URL
                |    url: https://duckduckgo.com/?q=mazim
                |    title: Nam Inceptos
                |  - type: Functional Documentation
                |    url: https://search.yahoo.com/search?p=diam
                |    title: Persecuti
                |  externalIds:
                |  - system: vituperatoribus
                |    id: 1101f928_9076_4d18_a132_48691905d157
                |  - system: doctus
                |    id: d3cc4a63_7dbf_4eb1_9f3e_c1eea7ef1971
                |  - system: iisque
                |    id: 6227ea5d_5836_4ba5_ac8d_ebc94ec52b7a
                |- key: value_object_1
                |  sortKey: '000000025'
                |  type: value object
                |  title: Possit Elitr
                |  description:
                |  - fuisset petentium option elaboraret nihil alienum alia doctus eam error porta
                |    oporteat scripta autem finibus mollis impetus morbi reformidans condimentum tota
                |    sagittis natoque sapientem sociosqu
                |  - fringilla vehicula eleifend quo aptent interpretaris tritani erat definitiones
                |    aliquip auctor veri voluptatum luctus mattis nostrum
                |  - ante at consectetuer quaerendum intellegat neque eripuit accommodare delicata
                |    animal enim donec platea auctor iisque et qui omittantur vim pellentesque falli
                |    ignota eos laudem moderatius suscipit massa donec ligula elitr himenaeos maluisset
                |    curae senserit dicam ultricies numquam has utinam theophrastus impetus corrumpit
                |    nibh detracto delectus dictumst expetenda neque odio non simul adhuc dicit singulis
                |    falli sea amet arcu ignota volutpat molestie sanctus sociosqu
                |  - mazim class has pro meliore mnesarchum enim laudem eirmod in idque est tractatos
                |    ceteros sapientem quaestio urbanitas euismod ut magnis potenti ceteros invidunt
                |    sed contentiones causae graecis dico volutpat nostra explicari vivendo theophrastus
                |    euismod qualisque viverra semper nulla aliquip ipsum omnesque eget prodesset delicata
                |    sed maecenas expetenda id eirmod patrioque iriure amet ultrices audire mediocrem
                |    malorum elit mandamus sumo omittam accommodare at et mediocritatem has odio fusce
                |    lorem nam sanctus lacinia perpetua natum elementum postea dicat nobis detracto
                |    mediocrem vocent adipiscing urna lacus vulputate metus hac alia utamur maiestatis
                |    necessitatibus dico mnesarchum atqui ultricies persequeris
                |  - ligula ac platea sapien splendide vivendo maximus eius disputationi instructior
                |    tantas ultrices omittantur pretium dolore diam dicant suavitate delicata libero
                |    vis persius duo disputationi ad invenire ea elit option postea leo bibendum aliquid
                |    id lorem falli ius curabitur dicam fabulas movet quod explicari partiendo eros
                |    vero honestatis iusto habitant graeco amet aliquid aliquam donec sententiae pellentesque
                |    commodo nulla graece inceptos ultricies ocurreret etiam euripidis risus posse
                |    lobortis appareat prompta graeco imperdiet porta laoreet torquent discere necessitatibus
                |    mei regione aeque errem aperiri ornare indoctum qualisque falli ex fabulas quem
                |    hinc duis vivamus quo facilisi praesent nisl urbanitas risus
                |  links:
                |  - type: Website
                |    url: https://duckduckgo.com/?q=risus
                |    title: Delicata Euripidis
                |  externalIds:
                |  - system: numquam
                |    id: e5ab2b86_6c81_4b90_b4af_c27e8f56beb7""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    DataObjectYamlIO.read(data)
    println(townPlan)
    assert(
      townPlan
        .dataObject(Key.fromString("value_object_1"))
        .exists(d => d.dataObjectType == "value object")
    )
    assert(
      townPlan
        .dataObject(Key.fromString("entity_1"))
        .exists(d =>
          d.dataObjectType == "entity" && d.title.value == "Delicata"
        )
    )
  }
}
