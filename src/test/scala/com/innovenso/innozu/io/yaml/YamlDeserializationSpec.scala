package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  ActorYamlIO,
  ArchitectureBuildingBlockYamlIO,
  BusinessCapabilityYamlIO,
  DataObjectYamlIO,
  EnterpriseYamlIO,
  ItContainerYamlIO,
  ItPlatformYamlIO,
  ItSystemIntegrationYamlIO,
  ItSystemYamlIO,
  OrganisationYamlIO,
  PersonYamlIO,
  TeamYamlIO
}
import com.innovenso.innozu.io.yaml.properties.TitleYamlIO.YamlJavaData
import com.innovenso.innozu.io.yaml.relationships.RelationshipBuffer
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.properties.PublicData
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
                |  attributes:
                |  - name: quaerendum
                |    description: dolor magnis disputationi expetendis libris gravida velit dis et
                |      eius aliquet autem sapien viris adipiscing maximus ne mediocrem ei ridiculus
                |      facilisis porta adhuc lacinia
                |    type: mediocritatem
                |    required: false
                |    multiple: true
                |  - name: himenaeos
                |    description: necessitatibus a ferri invenire parturient prompta deserunt gravida
                |      fusce eu platea ridiculus errem eirmod iudicabit noluisse discere tincidunt
                |      equidem qualisque sonet pharetra condimentum conclusionemque petentium constituto
                |      elitr euismod dictas convenire sententiae adipisci movet sociosqu non potenti
                |      dicant volutpat disputationi justo disputationi intellegebat dicta wisi adipiscing
                |      a nibh erat aliquip adipiscing dissentiunt eu finibus sodales nisi sale intellegat
                |      aliquip saepe solet natum commodo duo nisi maximus
                |    type: ferri
                |    required: true
                |    multiple: false
                |  - name: maecenas
                |    description: netus ea nulla arcu curabitur minim condimentum dicit legimus dolores
                |      adolescens principes doctus platonem habitant harum ultrices delicata eloquentiam
                |      explicari meliore montes luptatum ne equidem laoreet praesent ullamcorper senectus
                |      malesuada sem agam dicant purus harum melius libero ut
                |    type: delectus
                |    required: true
                |    multiple: true
                |  - name: maiestatis
                |    description: verear civibus mauris suas tibique senectus ligula ornare agam mutat
                |      utroque molestiae agam mauris electram himenaeos voluptatibus verterem veri
                |      impetus penatibus dictas justo mentitum viderer condimentum elementum definitiones
                |      reformidans quidam elitr verterem voluptatibus dicat nibh gubergren simul sapien
                |      dicit tempor accumsan condimentum ad nisl molestie numquam sociis postulant
                |      dicam adhuc ei tincidunt debet enim sollicitudin corrumpit porro rutrum meliore
                |      porta solet diam id imperdiet reque disputationi adversarium reprehendunt definitionem
                |    type: conclusionemque
                |    required: true
                |    multiple: true
                |  - name: natoque
                |    description: aperiri feugait pretium dissentiunt conceptam luctus curae augue
                |      equidem tale his venenatis deserunt appetere falli id dictum maluisset cum potenti
                |      putent persius dolorum vix animal suscipit minim lectus dictum eius postea elitr
                |      maecenas efficiantur tincidunt persecuti doming posidonium dignissim curae eleifend
                |      deserunt erroribus suas libero ridens saperet quot lobortis fuisset quisque
                |      ius sociosqu natum perpetua ferri possim persius altera menandri aliquam constituam
                |      aliquid tantas dolorem commune te efficitur ante quem mel netus melius taciti
                |      pro penatibus porro adipisci posse causae sociosqu comprehensam ea curabitur
                |      aliquid eripuit tractatos
                |    type: liber
                |    required: false
                |    multiple: false
                |  - name: dicat
                |    description: harum maiestatis conclusionemque vocibus quidam odio fugit interpretaris
                |      qui efficiantur volutpat numquam platea mnesarchum orci cu animal
                |    type: sale
                |    required: false
                |    multiple: false
                |  - name: donec
                |    description: lacinia veniam donec solum amet oratio ignota aliquid fusce quas
                |      neque constituto discere penatibus ignota tincidunt facilisi at theophrastus
                |      graeci dicam mazim definitionem latine dolores cum morbi feugiat velit graeci
                |      movet sale dicant eloquentiam accommodare dicunt convallis wisi hinc alia propriae
                |      vocibus atomorum simul laudem cursus mucius curabitur eloquentiam adolescens
                |      cetero quem saperet persecuti epicurei reprimique purus dolores interesset offendit
                |      vocent aptent audire eirmod civibus iusto interdum accumsan ornare causae harum
                |      morbi tamquam postulant bibendum dicat fabulas class conclusionemque himenaeos
                |      primis evertitur massa purus
                |    type: ei
                |    required: false
                |    multiple: true
                |  - name: audire
                |    description: mei tation eripuit vitae constituto imperdiet volutpat civibus posuere
                |      ubique deterruisset nulla montes conceptam definitionem habitasse sed graece
                |      vel vestibulum alienum iisque elitr prodesset intellegat ea aenean harum possit
                |      elementum deserunt porro no novum mediocrem pellentesque delenit dicant utroque
                |      mauris utamur brute volutpat platea ridens alterum accumsan constituto dicant
                |      purus honestatis adipiscing delicata dicant mollis definitiones imperdiet fames
                |      repudiandae perpetua interesset ridens vulputate quisque iusto senserit ubique
                |    type: quod
                |    required: false
                |    multiple: true
                |  - name: platonem
                |    description: morbi verear maximus aliquid praesent idque alienum alterum veritus
                |      corrumpit aliquip sanctus ius efficiantur inani habitasse placerat brute aptent
                |      oratio veritus detraxit gravida detracto iusto rhoncus elaboraret efficiantur
                |      perpetua sem recteque ludus dicunt fringilla usu mi habemus delicata nonumes
                |      conubia necessitatibus nibh his dictas periculis ad dico dico elaboraret nostrum
                |      tempus definitionem tristique principes epicurei magnis legere congue nulla
                |      mattis fringilla postea possim pri adipisci class delectus novum cum tritani
                |      vituperatoribus posidonium ultrices praesent ius
                |    type: ligula
                |    required: false
                |    multiple: true
                |  - name: justo
                |    description: dictumst eam quot adipisci amet diam comprehensam aptent maiorum
                |      maecenas interdum ancillae
                |    type: postulant
                |    required: true
                |    multiple: true
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
                |  public: This is public data
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
          d.dataObjectType == "entity" && d.title.value == "Delicata" && d.dataAttributes.nonEmpty && d.dataClassification
            .exists(c => c.level == PublicData)
        )
    )
  }

  it should "read platforms from yaml" in new EnterpriseArchitectureContext {
    val yml = """platforms:
                |- key: it_platform_1
                |  sortKey: '000000001'
                |  title: Scripta Tamquam
                |  description:
                |  - odio prompta euismod ea dictumst dictum iuvaret error elit vero offendit velit
                |    brute at volutpat recteque sed theophrastus noluisse doming usu fuisset vis fugit
                |    contentiones pellentesque similique inimicus contentiones splendide constituam
                |    veritus mediocritatem perpetua mediocrem ridens fugit hac latine ad populo hinc
                |    veniam ultrices mus saperet agam luctus sadipscing
                |  links:
                |  - type: Wiki
                |    url: https://www.google.com/#q=mutat
                |    title: Mei Voluptaria
                |  swot:
                |    strengths:
                |    - ac pertinax suscipit diam solum senectus dicit suscipiantur nunc iusto platonem
                |      volutpat quam deseruisse amet definitionem mucius oratio legere aeque dictumst
                |      civibus pericula gloriatur per deserunt justo eruditi
                |    weaknesses:
                |    - decore malesuada ornare expetendis ponderum novum appareat adipiscing aliquet
                |      tempor novum
                |    opportunities:
                |    - explicari verterem nullam postulant dolorem eruditi tacimates docendi class
                |      tale ludus nonumes rhoncus vulputate solum etiam augue tamquam placerat idque
                |      tamquam veniam ac epicuri vix sollicitudin vivendo malesuada laoreet brute curabitur
                |      congue persecuti wisi docendi eius erroribus fusce volumus quaerendum appareat
                |      ipsum laoreet comprehensam consul deseruisse maiorum ornatus movet utroque tractatos
                |      libris sapientem ridens massa molestiae accumsan elit sociis partiendo vivamus
                |      interdum autem vocent civibus indoctum veniam sollicitudin hac inimicus pertinacia
                |      reprimique felis class quaestio iriure vocibus quod
                |  eliminate: mea pri vivamus mandamus vidisse euripidis eum sem primis sit et nostrum
                |    ceteros evertitur conubia sollicitudin dolorem tortor tempus decore justo error
                |    massa docendi venenatis persius iisque disputationi voluptatum vocibus lobortis
                |    placerat mutat accusata varius noster pulvinar sea option dicta fusce libris ante
                |    invidunt conubia suavitate
                |  criticality:
                |    level: No Effect
                |    consequences: gloriatur utinam mauris tincidunt error an affert vel vulputate
                |      civibus egestas maiorum elementum cetero sententiae nulla cras nec mattis laudem
                |      tibique consectetur quas ocurreret dicam debet periculis harum sociis aliquid
                |      persius atomorum sonet has sem pretium tempus electram movet docendi maecenas
                |      vulputate deserunt assueverit noster detraxit pericula tractatos eu constituto
                |      falli quam epicuri bibendum instructior has posse expetendis
                |  externalIds:
                |  - system: affert
                |    id: eb45771e_d1f8_4b8a_a85b_3b731a07629d
                |  - system: mattis
                |    id: ce4a7ce3_3d18_4310_aa02_e219b76d7400
                |  fatherTime:
                |  - date: '1094-03-19'
                |    event: Conceived
                |    description: falli velit luctus agam elementum gubergren cursus ultricies perpetua
                |      porro mus sententiae placerat ex ut
                |  - date: '1696-05-26'
                |    event: Retired
                |    description: habitasse epicurei docendi elit postulant patrioque vocent cum errem
                |      facilisis eius non vehicula eloquentiam iudicabit himenaeos sollicitudin vituperatoribus
                |      persius
                |  - date: '2274-03-20'
                |    event: Active
                |    description: ornatus dui nunc elit porro class molestie iriure facilisi tibique
                |      ius pharetra petentium ridiculus sanctus enim volumus detraxit necessitatibus
                |      aenean viris evertitur faucibus consetetur vituperatoribus inimicus dicta vocent
                |      morbi sagittis atqui porttitor ponderum invenire enim delectus mediocrem vidisse
                |      epicuri pertinacia disputationi dicunt cum omnesque vocibus iriure augue mediocritatem
                |      metus civibus tota nobis nisi
                |  - date: '2603-10-22'
                |    event: Done
                |    description: vitae movet ex ornare intellegebat dicit reprehendunt affert signiferumque
                |      quot amet error dolor fermentum donec justo rutrum a dolores dolor intellegat
                |      praesent pulvinar dui numquam appetere civibus populo nonumes lacus pertinacia
                |      ignota wisi postea vero varius nulla utamur consequat laoreet agam nonumy theophrastus
                |      laudem aeque vim discere deseruisse nibh iusto intellegat luctus amet
                |  - date: '2646-06-20'
                |    event: Started
                |    description: utinam periculis consectetuer dolorem conclusionemque mattis justo
                |      inciderint maluisset agam ponderum prompta sententiae iriure pri ultrices docendi
                |      vestibulum pri pericula elaboraret magna dapibus donec rutrum egestas electram
                |      cetero dictas persequeris inani vim nominavi quem platonem novum lobortis oratio
                |      dignissim movet molestiae te dicit partiendo platea imperdiet
                |  - date: '0323-11-27'
                |    event: Decommissioned
                |    description: ubique has habitant orci error viderer sententiae reformidans class
                |      iuvaret causae dui inciderint montes eros doming nihil posidonium eum eam eget
                |      fastidii morbi nibh error consequat no quisque detracto condimentum noster consequat
                |      libero himenaeos varius no persecuti detracto conclusionemque tota omittantur
                |      auctor causae adhuc curae vidisse delectus turpis constituto nunc referrentur
                |      sea eu ridens integer feugiat idque facilisis comprehensam animal bibendum iudicabit
                |      ponderum adipiscing salutatus iriure mollis conclusionemque semper ultricies
                |      vis fermentum accumsan dolorem putent platea suas utamur noluisse interpretaris
                |      putent pro commune tristique
                |  - date: '0065-09-11'
                |    event: In Preproduction
                |    description: doming aptent contentiones imperdiet ludus vulputate adipisci tibique
                |      quot splendide viverra urbanitas praesent urna sale fabellas laudem nisi dissentiunt
                |      reque fastidii ne ligula tincidunt ponderum litora equidem tamquam constituam
                |      fuisset finibus dolore risus nec quam adipiscing posuere dicat
                |  - date: '0821-09-26'
                |    event: In Development
                |    description: fuisset wisi semper quisque aliquid hac accusata impetus dictumst
                |      mi consectetuer ultrices mediocrem elit arcu convenire varius cetero consectetuer
                |      eum natoque equidem sociis dicta integer minim idque gubergren nonumy vivamus
                |      nulla sale per bibendum tibique vivendo pericula eloquentiam conubia signiferumque
                |      tincidunt nihil vocibus gubergren interpretaris contentiones curae pertinacia
                |      magnis errem tamquam adipiscing tristique mauris tristique vocibus fermentum
                |      hac litora sagittis consetetur tellus saperet veri risus natoque wisi sea referrentur
                |      accommodare disputationi libris theophrastus ante in accommodare nibh
                |  - date: '0969-07-25'
                |    event: Life Event
                |    description: putent penatibus libris error moderatius maximus platonem quot duis
                |      consectetuer nonumy idque unum erroribus ut audire populo an atomorum eu nisl
                |      commune recteque velit augue errem viverra fastidii laudem repudiandae ornatus
                |      expetenda nec harum pri veri vivendo ex ignota voluptatibus adolescens nunc
                |      melius sagittis persius leo quaeque facilisi dapibus diam metus lacinia pulvinar
                |      sagittis possit torquent noster invenire urbanitas debet cursus condimentum
                |      donec graecis cetero habitasse postulant has mattis litora suas dis necessitatibus
                |      quis
                |  - date: '0972-03-02'
                |    event: Due
                |    description: intellegat inimicus tamquam ante odio mei tation viderer constituto
                |      aenean evertitur suspendisse sea contentiones ludus consectetur inceptos sed
                |      mutat suscipiantur rhoncus suscipit nam reprehendunt homero quod dolorem sapien
                |      magna splendide mei idque civibus has antiopam mollis potenti ne atomorum melius
                |      posse arcu nisl varius volumus erroribus eget etiam porta appareat proin quod
                |      dicam phasellus purus vivendo non malorum morbi quo duo at praesent dignissim
                |      viverra nihil utroque auctor homero utroque falli saperet persecuti nisi sanctus
                |      vestibulum mediocrem posse ornare
                |  - date: '0988-08-12'
                |    event: In Production
                |    description: morbi aliquam eloquentiam mea mazim appareat omittantur habitasse
                |      consectetuer agam oporteat putent qualisque aliquid nec reque etiam tempor sollicitudin
                |      nostrum scripta praesent dignissim explicari consequat reformidans dolor meliore
                |      pretium voluptatibus habemus mi possit ultrices verear libero convenire donec
                |  resilience:
                |  - class definiebas et quaeque numquam mucius pretium duo sodales proin erat audire
                |    persecuti interpretaris urna falli sociis semper posidonium aptent in tincidunt
                |    suscipit consul dolor porro proin libris purus curae solum referrentur volumus
                |    elaboraret vel errem tamquam eloquentiam possit habeo inani euismod adversarium
                |    justo sapien ea aliquip corrumpit splendide sale massa rutrum felis intellegebat
                |    prodesset lobortis metus sociis ocurreret viris condimentum viris commodo inani
                |    faucibus est curabitur vituperatoribus enim auctor suscipiantur causae instructior
                |    invidunt vituperata tale elaboraret noluisse lobortis contentiones veri
                |- key: it_platform_2
                |  sortKey: '000000015'
                |  title: Montes Expetenda
                |  description:
                |  - nullam eloquentiam duis partiendo principes delenit sagittis potenti nulla deseruisse
                |    ullamcorper massa laoreet legimus urna iriure eleifend idque tantas elementum
                |    senectus hendrerit contentiones tristique liber animal utroque metus
                |  - platonem principes nec porro porttitor mnesarchum interpretaris gubergren tempor
                |    principes
                |  - sagittis tale noster mollis sadipscing pro doctus tale corrumpit elitr inceptos
                |    voluptatibus est feugiat at vim adipisci dicunt phasellus quisque facilisi sea
                |    accusata accumsan imperdiet est postea orci utamur augue homero condimentum hac
                |    congue interpretaris unum facilis magnis aliquet feugiat nonumes salutatus dicit
                |    theophrastus graeco vivamus minim cursus ea suavitate fugit homero erat tellus
                |    autem class fabulas duis tortor impetus fuisset impetus facilis electram massa
                |    intellegebat assueverit graeci accommodare
                |  - justo tincidunt ludus ut iaculis nibh mei justo tibique numquam natoque dicunt
                |    maiorum himenaeos est aliquet referrentur dignissim semper mnesarchum senserit
                |    alterum fringilla consetetur mucius maiestatis inceptos conubia volutpat conceptam
                |    maluisset utroque saperet mei ridens scripta maiorum efficitur detracto te purus
                |    natum augue deterruisset dolorum voluptatum debet primis tale volutpat saperet
                |    commune mollis posidonium tractatos turpis possim torquent idque ornare velit
                |    utamur intellegebat cras posidonium persecuti suspendisse evertitur tantas consectetur
                |    justo iusto nunc efficiantur dictumst fugit porta lobortis mazim assueverit vestibulum
                |    quaeque atqui vocent lorem novum fringilla mauris mauris
                |  links:
                |  - type: Architecture Documentation
                |    url: http://www.bing.com/search?q=eleifend
                |    title: Eius Hinc
                |  - type: Technical Documentation
                |    url: https://www.google.com/#q=posidonium
                |    title: Dicit Parturient
                |  - type: API Documentation
                |    url: https://www.google.com/#q=eam
                |    title: Possit
                |  - type: Technical Documentation
                |    url: https://search.yahoo.com/search?p=massa
                |    title: Eleifend
                |  swot:
                |    strengths:
                |    - sumo dictum urbanitas ridiculus diam appetere nascetur fusce viris posse quisque
                |      congue maiestatis ridiculus placerat tale suscipit solum non partiendo definiebas
                |      finibus consul inceptos vestibulum felis posidonium finibus congue dolore nec
                |      an interesset intellegebat inceptos dicunt torquent impetus aperiri nec iuvaret
                |      efficiantur iriure vivendo platea duo singulis tation est nonumes eam moderatius
                |      petentium viverra sapien dico dis omnesque doming cum iaculis consul alterum
                |      fringilla convenire congue dicta saperet himenaeos atqui lectus ipsum similique
                |      elitr dicit gravida
                |    - senectus elit nullam pulvinar ponderum dicit ocurreret fermentum dissentiunt
                |      mediocrem justo eum decore maecenas duo unum eget velit convallis integer lacus
                |      movet suas alienum has neque nostra elementum verear fringilla euismod pretium
                |      discere interpretaris deseruisse homero sit sapien detraxit adolescens blandit
                |      sed atomorum sit sodales maiorum mediocrem quaeque pretium nihil brute auctor
                |      vivamus imperdiet fusce eum splendide error mus corrumpit porro eius alterum
                |      laudem purus ridiculus habitant te maecenas evertitur hendrerit dicunt aliquid
                |      unum vel noster enim viderer dicunt sea singulis
                |    - idque primis vehicula definiebas pro intellegat adolescens urbanitas labores
                |      pretium dolorum perpetua dico vivamus rutrum nisi decore nisi eget nisi finibus
                |      mollis rhoncus fuisset scripta primis dolores his ante qualisque et latine falli
                |      tota duis habitant urbanitas persequeris labores ignota conubia offendit
                |    weaknesses:
                |    - corrumpit mollis maluisset saepe omittantur dictumst tation praesent sale ipsum
                |      ubique habeo elitr referrentur brute tractatos prodesset scripta quem nominavi
                |      invenire elit pro dignissim liber harum suspendisse neglegentur vidisse altera
                |      proin mei mandamus definitionem phasellus corrumpit eius posidonium magna platonem
                |      adolescens novum senectus laoreet constituto doctus nostrum definiebas curabitur
                |      suas odio orci varius epicuri finibus aenean libris deseruisse persecuti discere
                |      nibh interdum perpetua prodesset nominavi alterum consetetur referrentur quisque
                |      id omittantur graecis splendide fringilla atomorum varius dictas inceptos metus
                |      dapibus petentium accumsan contentiones risus eripuit sollicitudin te dicta
                |      augue mei augue pri hendrerit nonumes salutatus tortor mei
                |    - solet quot magnis elaboraret affert alterum dolore sit adversarium turpis utinam
                |      doming qui
                |    opportunities:
                |    - venenatis inimicus prompta wisi persius instructior elit dictum tale evertitur
                |      dis est at aperiri auctor rhoncus mandamus alterum consetetur sit suavitate
                |      blandit eirmod oporteat condimentum fabulas id scelerisque amet accusata partiendo
                |      augue porta donec netus semper duis postea eos gubergren aliquip veri verear
                |      dis accommodare perpetua dolorum epicuri conubia cetero tation causae tempus
                |      convenire ultricies reprehendunt risus ludus dicit epicuri odio consetetur massa
                |      verear magna noluisse phasellus novum lectus voluptatum duis habemus turpis
                |      ex eius nisi adversarium
                |    - expetenda contentiones interdum suas ornatus deserunt gloriatur maluisset sodales
                |      omittantur sale interpretaris curae menandri patrioque nibh iaculis sanctus
                |      vero curabitur ante mus potenti
                |    threats:
                |    - torquent maiorum saperet tacimates sagittis alia posidonium tempus magnis aptent
                |      possit vulputate volutpat tellus parturient tation decore atqui eleifend fastidii
                |      justo libris tempor ornare ex instructior eleifend interdum phasellus ea evertitur
                |      movet mandamus magna ea et has sadipscing neque persius causae gravida periculis
                |      urbanitas conclusionemque error qui agam perpetua venenatis mea mollis consul
                |      adhuc facilisis prompta cum tincidunt petentium equidem pretium feugait sanctus
                |      congue an dicunt fringilla mei sanctus similique aliquip detracto interesset
                |      non nulla sagittis appetere similique duis verterem dapibus posse felis alia
                |      platea
                |    - elit delectus eos in malorum possim deterruisset dicam dignissim elitr intellegebat
                |      neglegentur euismod delectus ei suspendisse tamquam proin accumsan quaerendum
                |      persecuti noluisse dicat nostrum consequat nihil vocent dolor vivamus sagittis
                |      quaeque sale efficitur duis alienum tincidunt hendrerit dictumst aliquid sagittis
                |      veri te phasellus honestatis curabitur luctus potenti solet aliquip conubia
                |      quisque sollicitudin pharetra evertitur dapibus quaerendum ubique posuere eirmod
                |      salutatus veritus honestatis prodesset iusto mea varius propriae diam altera
                |      venenatis intellegebat mutat sapientem commune enim graecis reque vel intellegat
                |      inciderint nostrum
                |    - qui tellus senectus quot quod aliquid civibus legimus tale senserit expetendis
                |      laoreet vix nisl odio dictas reprimique dolorum nisl mandamus novum eloquentiam
                |      curabitur maecenas pretium urna dolor ridens suscipiantur sonet eloquentiam
                |      dui his oporteat an
                |  eliminate: tellus elit lacinia malesuada referrentur orci ridiculus dicit voluptatum
                |    nonumes audire interpretaris libris vulputate platea conubia gloriatur populo
                |    vituperata commodo posidonium delectus felis sadipscing dolor magna quisque persequeris
                |    populo viverra iriure saperet maecenas malorum eum pertinax vulputate libero aliquip
                |    falli maecenas montes torquent minim maximus fames theophrastus gravida gubergren
                |    persecuti nobis ut neglegentur pertinacia nam rhoncus periculis affert justo urbanitas
                |    splendide repudiare nascetur conubia inimicus quam verterem fabulas percipit quis
                |    intellegebat suspendisse sed convallis dis oporteat eros splendide gubergren postea
                |    reprimique ei nonumes option ultrices sem enim ante docendi aliquet nonumy dicit
                |    quod habitant eleifend
                |  criticality:
                |    level: Catastrophic
                |    consequences: disputationi ocurreret explicari referrentur vis postea erat faucibus
                |      efficiantur pri evertitur aliquet placerat laoreet deseruisse diam honestatis
                |      saperet scripta torquent ponderum sale contentiones natoque verear alterum postulant
                |      ancillae esse voluptaria ferri minim vestibulum eleifend vis assueverit
                |  externalIds:
                |  - system: sociosqu
                |    id: df58c1d5_405d_4bd7_a3b1_1ad43a43976b
                |  - system: saepe
                |    id: 8408d876_3891_4817_865e_aef85deccd72
                |  - system: alienum
                |    id: ad373be3_dbf6_49a9_bca6_41a959457ecc
                |  fatherTime:
                |  - date: '1631-11-06'
                |    event: In Development
                |    description: velit expetendis euripidis audire laoreet cubilia wisi partiendo
                |      vituperatoribus neque curae feugait erroribus moderatius viris latine pri mediocrem
                |      nisi hinc ridiculus dictum wisi elaboraret lacinia aliquid tincidunt elitr nominavi
                |      odio pro reque principes semper mauris facilisis platea mutat pulvinar cetero
                |      graeco posidonium id mei penatibus epicuri ocurreret senserit elementum morbi
                |      nec fusce rhoncus nascetur constituam dicant
                |  - date: '1914-06-23'
                |    event: Retired
                |    description: et ridiculus aliquid lacus latine constituam ocurreret tractatos
                |      deseruisse natum sed viderer mazim dictas ligula an nisl vocent option elaboraret
                |      senserit lacus equidem epicurei voluptatibus quaeque ligula adhuc ipsum explicari
                |      possit urna liber minim cras voluptatibus id vivendo faucibus neque tacimates
                |      mattis mediocritatem cu numquam vehicula melius his sagittis et tellus omnesque
                |      quidam deterruisset purus aptent cras magna tristique libris posuere sollicitudin
                |      delenit possim repudiandae vitae nec
                |  - date: '2225-02-16'
                |    event: In Production
                |    description: pertinacia eleifend movet discere suas ridens solet necessitatibus
                |      tristique nec autem necessitatibus phasellus recteque alterum dolor nullam imperdiet
                |      convenire vituperata mollis ex iudicabit dicat ipsum torquent rhoncus interdum
                |      sem quot aperiri etiam molestie consectetuer idque ea magnis sea singulis dicat
                |      felis quod ea constituam tristique ubique rhoncus vocent augue epicuri dolore
                |      sociosqu sodales dicat postea aliquid habeo hac offendit vulputate donec quam
                |      autem at vocibus intellegat deterruisset sociosqu numquam in iuvaret vocibus
                |      homero nam sed parturient tota quem sem
                |  - date: '2644-10-06'
                |    event: Conceived
                |    description: sale discere posidonium dicat alterum detraxit sociosqu sem neglegentur
                |      dis cras vivendo iusto ponderum vim populo dis principes pertinacia option antiopam
                |      iaculis rutrum scripserit omittantur propriae ultricies euismod nihil sollicitudin
                |      inceptos
                |  - date: '2841-07-13'
                |    event: Life Event
                |    description: ad signiferumque convallis sociis mus melius
                |  - date: '0424-09-01'
                |    event: Due
                |    description: invidunt mucius mea debet quas partiendo per interdum quisque interdum
                |      sale dico pertinax facilis adipisci viderer quo atomorum massa mediocrem ullamcorper
                |      ancillae dolores fastidii dicta vivamus fabulas sem causae constituam nisl vivamus
                |      ut fugit pericula dicat meliore explicari vero sale aliquid an doctus fabellas
                |      theophrastus labores appareat lobortis tractatos putent neque deterruisset suscipiantur
                |      consectetuer ultrices facilis dictumst voluptaria semper facilis at pri aptent
                |      reque vulputate ius sociosqu id alienum expetenda debet sapien quaeque nonumy
                |      sea deseruisse aliquam harum patrioque offendit integer euismod graecis massa
                |      reformidans conceptam dolore primis
                |  - date: '0658-04-03'
                |    event: Active
                |    description: est solum aeque evertitur regione tincidunt ferri ea sodales venenatis
                |      consul iisque definitiones graecis aperiri prodesset decore veri sodales quam
                |      omnesque semper phasellus mattis has facilisi ignota prompta urna moderatius
                |      fastidii per tale molestie eum no elaboraret elitr a per eu cursus enim gravida
                |      hinc convenire dictumst evertitur tellus ius mucius solum quam omittantur cu
                |      dicta vivamus latine consetetur pro inciderint solet atqui dissentiunt explicari
                |      expetendis usu varius efficiantur velit voluptatibus eam explicari eros meliore
                |      habitant elaboraret libero atqui egestas mentitum expetendis petentium tincidunt
                |      comprehensam wisi vivamus natum dicat graeco duis omnesque magna gloriatur animal
                |  - date: '0742-09-20'
                |    event: In Preproduction
                |    description: natum adhuc metus constituto quo partiendo theophrastus decore consul
                |      etiam principes constituto ullamcorper ius animal homero mnesarchum honestatis
                |      eripuit natoque vocibus tellus scripserit
                |  - date: '0775-02-02'
                |    event: Decommissioned
                |    description: inceptos partiendo fabulas tation eripuit persius gloriatur propriae
                |      dui nostrum
                |  - date: '0819-09-18'
                |    event: Done
                |    description: hinc intellegat omittam faucibus efficitur definitiones sale gravida
                |      ac nibh nostra bibendum homero placerat mus diam fringilla alterum ea curae
                |      tempor affert inani tamquam bibendum leo dictumst sadipscing tacimates vituperatoribus
                |      nec dicunt suscipit constituam dicta detraxit maiestatis quaestio necessitatibus
                |      dolor natoque eros ne expetendis labores praesent melius singulis nulla veri
                |  - date: '0852-09-07'
                |    event: Started
                |    description: eros sanctus inciderint vocent dicunt utinam dictum aptent faucibus
                |      sanctus molestiae quo persius vehicula percipit similique aliquet noluisse civibus
                |      eius dicunt honestatis feugiat diam inciderint sanctus eget at vidisse elitr
                |      assueverit pertinax curabitur viverra et graeci augue harum utroque molestiae
                |      neque a altera libris pericula idque non est vivendo sumo agam eum urna sodales
                |      nam detraxit te nullam causae
                |  resilience:
                |  - elitr prodesset eloquentiam graeco discere cubilia porro nisl deterruisset praesent
                |    dissentiunt mei dicat moderatius ius iusto ante dolore voluptatibus proin expetenda
                |    aliquid doctus vivendo qualisque metus quam iudicabit voluptaria alienum nisl
                |    offendit has condimentum disputationi definiebas consetetur purus noster falli
                |    affert utinam tantas ponderum mnesarchum dicit audire vehicula utamur senserit
                |    amet autem et agam curabitur
                |""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    ItPlatformYamlIO.read(data)
    println(townPlan)
    assert(townPlan.platforms.nonEmpty)
    assert(townPlan.platforms.exists(p => p.resilienceMeasures.nonEmpty))
  }

  it should "read system integrations from YAML" in new EnterpriseArchitectureContext {
    val yml =
      """systems:
        |- key: system_1
        |  title: System 1
        |- key: system_2
        |  title: System 2
        |integrations:
        |- key: integration_1
        |  sortKey: '000000037'
        |  system1: it_system_1
        |  system2: it_system_2
        |  title: hello
        |  throughput:
        |    volume: millions of records
        |    frequency: realtime, every second
        |""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    ItSystemYamlIO.read(data)
    assert(townPlan.systems.size == 2)
    ItSystemIntegrationYamlIO.read(data)
    assert(townPlan.systemIntegrations.size == 1)
  }

  it should "read containers from YAML" in new EnterpriseArchitectureContext {
    val yml = """containers:
                |- key: database_1
                |  sortKey: '000000035'
                |  type: database
                |  title: Omittam leo
                |  description:
                |  - mandamus efficiantur porta has mnesarchum vituperata quas ea aliquet invenire
                |    viverra gloriatur nibh partiendo leo hinc quaestio dolorum repudiandae regione
                |    oratio montes simul mattis alia repudiare harum explicari ocurreret altera aperiri
                |    tractatos principes vitae justo eleifend magnis ullamcorper dissentiunt harum
                |    patrioque postea cubilia docendi interesset conceptam epicuri ornatus inani noster
                |    atomorum tacimates volumus mattis deseruisse mandamus phasellus quidam signiferumque
                |    dicit arcu propriae perpetua justo habitasse maiestatis constituto reformidans
                |    offendit ea condimentum ligula ex dicit ornare lectus turpis inciderint velit
                |    definitionem suspendisse ultricies noster consul
                |  - disputationi deterruisset leo mazim adhuc sonet pertinax eum atomorum consetetur
                |    dolor sed bibendum numquam populo justo cursus fames errem luctus magna salutatus
                |    populo fames et expetendis platonem doming mediocritatem recteque constituam iriure
                |    hendrerit vix iaculis discere mandamus pericula similique alienum platea sententiae
                |    alterum ei eros pertinacia gubergren mel atqui amet per decore latine offendit
                |    sapien semper mediocrem labores detraxit gravida quis feugiat iriure perpetua
                |    posse nascetur dicta mattis inani luctus theophrastus semper aliquip intellegat
                |    eu falli sollicitudin vituperata quam ex
                |  links:
                |  - type: Architecture Documentation
                |    url: https://search.yahoo.com/search?p=nominavi
                |    title: Qualisque
                |  - type: Development URL
                |    url: https://search.yahoo.com/search?p=nullam
                |    title: Libero Sadipscing
                |  - type: Website
                |    url: https://www.google.com/#q=primis
                |    title: Dicat vim
                |  swot:
                |    strengths:
                |    - duo periculis tantas consetetur novum est lorem dico mediocrem cras blandit
                |      platea docendi
                |    - tincidunt definitionem dui accommodare tellus graecis auctor sale elit sociosqu
                |      iusto inceptos equidem lectus deterruisset tristique turpis malorum mediocrem
                |      pertinacia commodo nisl himenaeos corrumpit dolorum has esse postulant invidunt
                |      enim splendide a deseruisse esse vix menandri discere aliquid cu latine dicunt
                |      conubia egestas odio veniam metus aliquam dico sententiae postulant ei porro
                |      corrumpit tibique aptent
                |    - movet nominavi putent wisi mediocritatem percipit dicta vero contentiones ponderum
                |      vivamus augue quas tincidunt prodesset vis
                |    - porttitor constituto audire legimus ligula dico sapientem nibh vivendo nisl
                |      id turpis tantas referrentur harum persequeris aliquet mentitum consetetur alterum
                |      convallis ocurreret corrumpit fabulas adversarium option lectus cursus vel utamur
                |      deserunt efficitur propriae electram eget nulla lacinia honestatis definitionem
                |      minim justo sententiae malesuada dico dolore nonumes ante maecenas ius quisque
                |      adhuc veniam has delicata pericula perpetua imperdiet commune eget lectus aliquid
                |      fugit
                |    - iaculis duis impetus fabulas sapientem mediocritatem eam mel dolore arcu mentitum
                |      gravida graeci veniam corrumpit class omnesque curabitur delicata ludus ullamcorper
                |      curabitur sadipscing comprehensam curae sanctus harum vulputate sociis leo id
                |      tellus fugit platonem in voluptaria eruditi mei pro accommodare iriure taciti
                |      epicuri odio et venenatis ponderum tortor
                |    weaknesses:
                |    - nonumes inciderint feugiat decore posse malorum repudiandae corrumpit molestiae
                |      cras quam curabitur tempus donec te meliore cu disputationi accusata qualisque
                |      eloquentiam et mattis inani eget adipisci eius iriure eu justo ei repudiare
                |      utamur iaculis dico mel vestibulum ponderum sadipscing diam sanctus convallis
                |      phasellus suavitate interdum habitasse placerat imperdiet harum curabitur repudiandae
                |      natum deseruisse propriae corrumpit agam sumo consectetuer ultrices viverra
                |      aptent saperet numquam audire graeco omnesque elit sumo potenti mattis intellegebat
                |      civibus etiam mollis docendi senserit donec velit lacinia autem eleifend altera
                |      vocibus vel
                |    opportunities:
                |    - invenire mollis causae montes pro nam fugit erat sollicitudin hinc eu veritus
                |      nisl veritus ultricies purus interesset expetenda primis intellegat saepe adipisci
                |      disputationi natoque dictumst mutat varius duo tota ultrices meliore senectus
                |      definiebas atomorum vel aptent integer ultricies dicunt sapientem interdum maiestatis
                |      melius eam posse habitasse dicit adipisci comprehensam cursus vis cubilia erroribus
                |      disputationi quaestio delenit menandri nascetur mediocrem efficitur dicunt invidunt
                |      utroque epicuri voluptatibus netus arcu voluptatibus porro vehicula leo enim
                |      delenit accusata tempor porttitor audire vivamus tantas non gubergren
                |    - placerat utinam dicam inani dictum tortor quidam animal ullamcorper eum nostrum
                |      eripuit accommodare urna odio fringilla cursus liber accusata auctor turpis
                |      maximus ludus nihil interdum error erat erroribus penatibus mentitum honestatis
                |      accusata fringilla ipsum dissentiunt quas vocent eget ut eirmod delicata graeco
                |      epicuri pro recteque porttitor pellentesque ad offendit quaeque quot doming
                |      solum ubique commodo amet dictumst habitasse imperdiet vero partiendo graeco
                |      eruditi tincidunt massa accumsan persecuti referrentur maecenas hendrerit dapibus
                |      platea pericula cubilia ius te possit no aliquid nostrum appareat utinam
                |    threats:
                |    - te sagittis viderer cursus postulant vero veniam pri reque sed interdum regione
                |      menandri pellentesque recteque commune tellus ridens audire neque pericula utinam
                |      propriae alienum gravida erroribus mentitum ad alienum mutat dolore noster feugiat
                |      legimus porta facilisi dolorum amet intellegat potenti mediocritatem utamur
                |      nisl tractatos fermentum ubique laoreet deseruisse constituam vituperata efficiantur
                |      fuisset quot ancillae alterum vim omittam sadipscing donec referrentur sea ius
                |      ponderum iuvaret cras saepe veri vituperata error dico mucius metus integer
                |      risus porro libris ante senectus impetus aptent reque aptent elit sonet maluisset
                |      viris consequat suas autem partiendo atqui rhoncus posse
                |    - efficiantur ponderum mnesarchum dolore aperiri sea singulis constituam montes
                |      perpetua sagittis ponderum fuisset volutpat libris vehicula
                |  migrate: eum propriae honestatis primis partiendo tale feugiat instructior epicuri
                |    mediocrem instructior viderer sociis epicurei tibique vis nisl solum mucius nullam
                |    postulant antiopam efficiantur vidisse ornare qui sale sit senectus cursus elementum
                |    oratio esse prompta possit ridens affert erroribus molestie magnis hac tempor
                |    usu faucibus assueverit at maiestatis
                |  criticality:
                |    level: Catastrophic
                |    consequences: metus vulputate elementum te maiorum viris auctor penatibus alia
                |      quisque ex libero nec tale vitae justo patrioque quisque dissentiunt dictumst
                |      dolorem docendi quaestio posse potenti magna fusce falli elit iaculis vituperata
                |      recteque suas repudiare consul curabitur dictumst neglegentur deseruisse gloriatur
                |      lobortis verterem dolor malesuada pretium aenean ad diam ferri audire evertitur
                |      hac vocibus quem persequeris prodesset ceteros efficiantur explicari antiopam
                |      lobortis etiam necessitatibus eam a theophrastus ornatus ne tale percipit dictumst
                |      urna sumo repudiare ferri no evertitur senserit volumus quidam ut deserunt electram
                |      theophrastus enim
                |  externalIds:
                |  - system: an
                |    id: fa101090_e9ba_4c39_996c_3566c2cbddbe
                |  - system: delectus
                |    id: 8c45e88b_5e74_4f33_b174_6056f9db455c
                |  - system: urna
                |    id: 6164e680_3bee_4591_b7ee_dff85b402eb9
                |  - system: vivamus
                |    id: a22cdb91_ebbf_47dc_8c71_81c2af671ecd
                |  fatherTime:
                |  - date: '1008-09-12'
                |    event: In Preproduction
                |    description: comprehensam mel usu quisque eam ridiculus ne viverra noster vocibus
                |      cum voluptatibus placerat iriure scelerisque labores elit aliquet possit sumo
                |      qui vivendo oratio neglegentur consequat delectus natoque platonem ut labores
                |      delicata quidam theophrastus mel habitasse ullamcorper neque maiorum maximus
                |      suas fabellas petentium eget ludus dignissim platonem tristique atomorum electram
                |      graeci interpretaris elitr pertinacia duo cras errem harum fringilla
                |  - date: '1165-02-08'
                |    event: Done
                |    description: sociis suavitate lorem dicunt accusata maiestatis posuere maximus
                |      facilis inimicus amet utamur efficiantur dignissim morbi duis debet persius
                |      equidem solum gubergren sonet aliquip altera tale lorem saepe dicunt populo
                |      postea detraxit petentium mediocrem doming regione augue possim tamquam dolore
                |      suas oratio agam interesset voluptatibus intellegat mazim intellegebat tacimates
                |      no bibendum ut magnis a litora melius menandri movet viris iisque luptatum
                |  - date: '1367-05-27'
                |    event: In Production
                |    description: omittantur equidem invenire graece falli libris error platea populo
                |      fringilla iuvaret nam efficitur nascetur deserunt urna lacinia sanctus voluptatibus
                |      civibus sanctus malesuada fuisset
                |  - date: '1571-02-03'
                |    event: Started
                |    description: convenire conubia tibique habitant dui detracto ignota suscipiantur
                |      volumus eros veritus alienum causae habitant accumsan leo aliquip tristique
                |      interesset falli tortor senectus dicunt delenit vocent inciderint populo delenit
                |      ridens tellus magna ea quaestio adolescens veritus odio duis sonet epicuri solet
                |      persequeris turpis imperdiet commodo equidem fuisset integer ea platonem prompta
                |      vocibus epicurei graeci dicant libero venenatis elaboraret primis suas id impetus
                |      neglegentur consectetur ad molestiae ex iisque augue dictum quem dictumst fermentum
                |      quaeque nibh verear cum mea in vero prodesset aeque legere dui lacinia definiebas
                |      volutpat aliquet dis neglegentur mattis aeque qualisque rhoncus ceteros urna
                |      vix dolores netus
                |  - date: '2145-06-09'
                |    event: Due
                |    description: mnesarchum posse mus vel posuere class nostra litora docendi nulla
                |      noster quis appareat dignissim detracto tale natum putent aliquip molestiae
                |      sea himenaeos adipiscing viris aliquid hac sadipscing intellegat ocurreret nam
                |      solet fringilla dui blandit harum rhoncus habemus definitionem justo posuere
                |      recteque malorum et his eius finibus delenit ligula habitant sale
                |  - date: '2356-11-02'
                |    event: Life Event
                |    description: an utinam quidam amet persequeris platonem nobis error
                |  - date: '2401-12-07'
                |    event: Conceived
                |    description: reformidans semper rutrum causae offendit vulputate accumsan interesset
                |      ullamcorper odio reformidans vituperatoribus saepe wisi ei impetus finibus maximus
                |      feugiat movet agam sem
                |  - date: '2503-04-04'
                |    event: Active
                |    description: fermentum disputationi pri malesuada magnis nisl vivamus nullam lorem
                |      et debet verear offendit vehicula molestiae postea rhoncus fugit verterem
                |  - date: '2575-03-08'
                |    event: Retired
                |    description: mus repudiare ancillae congue netus convallis quam corrumpit voluptatibus
                |      postulant
                |  - date: '0537-11-21'
                |    event: In Development
                |    description: eripuit maximus eam referrentur aeque lectus malesuada eam pulvinar
                |      honestatis posuere ceteros eos no dicat expetenda instructior quaestio commune
                |      novum
                |  - date: '0609-09-25'
                |    event: Decommissioned
                |    description: vel pertinax rutrum interdum praesent conceptam iriure vestibulum
                |      solet congue reque ridens posse definitionem est vivendo ornare lobortis dis
                |      montes tamquam iudicabit sumo solet vix felis mollis affert nostra idque dicant
                |      vivamus sollicitudin malesuada odio nobis periculis vidisse posidonium appetere
                |      mandamus et oratio vivamus quot saperet rhoncus penatibus an ipsum finibus postulant
                |      scelerisque ea adhuc an viverra euripidis porttitor neque tempus efficitur quis
                |      vocibus pertinax ridens persecuti senserit dictumst donec expetendis sanctus
                |      magna ac dicunt honOAestatis pellentesque placerat graeco magnis dicant est parturient
                |      idque sed deserunt maximus maximus inceptos mnesarchum risus quot novum tota
                |      donec
                |  resilience:
                |  - facilis suas doctus vituperata fuisset phasellus eruditi quam justo alia dissentiunt
                |    errem disputationi noluisse definiebas commodo ubique est invenire pro enim mollis
                |    scelerisque his morbi theophrastus necessitatibus tempus suavitate iusto eleifend
                |    diam dico eleifend graecis quis arcu sanctus reprehendunt sanctus fames invidunt
                |    eleifend finibus volumus et nascetur neque conceptam aliquet tation turpis litora
                |    an simul ipsum tale latine non solum commune usu suscipiantur elementum pertinacia
                |    posuere tristique has consul populo pri malesuada voluptatum laoreet evertitur
                |    graeci consectetur pertinax oporteat adhuc deseruisse oratio sociosqu novum singulis
                |    his velit
                |  api:
                |    style:
                |      title: REST
                |      description: Typical REST API
                |    scope:
                |      title: VPN
                |      description: Only accessible through VPN
                |    authentication:
                |      title: OAuth
                |      description: protected with Auth0
                |    ddos:
                |      title: CloudFlare
                |      description: Using CloudFlare shield
                |    rateLimiting:
                |      title: through API Gateway
                |      description: AWS API Gateway to be exact
                |""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    ItContainerYamlIO.read(data)
    assert(townPlan.containers.size == 1)
    println(townPlan.container(Key.fromString("database_1")).map(_.api))
    assert(
      townPlan
        .container(Key.fromString("database_1"))
        .exists(d =>
          d.api.exists(a =>
            a.ddoSProtection.title.toLowerCase == "cloudflare" && a.rateLimiting.title.toLowerCase == "through api gateway" && a.authentication.title.toLowerCase == "oauth" && a.scope.title.toLowerCase == "vpn" && a.style.title.toLowerCase == "rest"
          )
        )
    )

  }

  it should "read IT Systems from YAML" in new EnterpriseArchitectureContext {
    val yml = """data:
                |- key: entity_1
                |  sortKey: '000000001'
                |  type: entity
                |  title: Patrioque
                |  description:
                |  - singulis consequat unum ea posse dolores persius velit tation civibus comprehensam
                |    labores reformidans litora epicurei rhoncus risus contentiones libris curabitur
                |    vituperatoribus leo ei offendit augue corrumpit ligula porttitor fames hendrerit
                |    mazim in sententiae nihil cetero aenean an duis euismod mazim legimus magna evertitur
                |    tincidunt suscipit appetere persius facilisis delectus condimentum cursus error
                |    venenatis dolor curabitur vel vituperatoribus signiferumque ancillae accumsan
                |    eget atomorum appetere graeco equidem conclusionemque praesent nostrum mauris
                |    tritani dicta aliquet maluisset movet consectetuer animal voluptatum mi ridens
                |    omnesque percipit consul legere fastidii autem ornare quod repudiare melius mel
                |    invenire litora
                |  - fugit nonumes interdum scripta vero tortor mel sit tractatos in nonumes dui quis
                |    iusto tation scripta nostra hac qualisque dissentiunt prodesset ridiculus platonem
                |    commune vidisse proin utinam constituam tantas habeo assueverit minim no hendrerit
                |    etiam menandri inceptos sollicitudin ligula numquam elementum graecis adolescens
                |    molestie causae comprehensam iriure melius noluisse quidam in taciti conceptam
                |    causae suavitate dictum finibus deseruisse ferri iudicabit sodales ipsum montes
                |    quod libero equidem fames hendrerit fames augue delenit mazim putent dignissim
                |    ornare voluptaria expetendis felis decore pro
                |  links:
                |  - type: Functional Documentation
                |    url: https://search.yahoo.com/search?p=eruditi
                |    title: Mnesarchum Cetero
                |  - type: Website
                |    url: https://search.yahoo.com/search?p=ac
                |    title: Invenire
                |  - type: Pre-Production URL
                |    url: https://duckduckgo.com/?q=doctus
                |    title: Iisque Offendit
                |  externalIds:
                |  - system: 'no'
                |    id: 5d6d1a5c_16b9_4e11_8ea6_ffadffe8f2dc
                |  fatherTime:
                |  - date: '1085-10-22'
                |    event: Life Event
                |    description: qualisque perpetua pretium liber ullamcorper ridens cum elementum
                |      pulvinar senserit tritani labores solum nec inceptos eleifend nisl option vel
                |      pulvinar indoctum enim cu conceptam constituam ut arcu eruditi omnesque cubilia
                |      definiebas massa discere fugit proin ullamcorper fames fabellas voluptaria perpetua
                |      ridens dico malesuada parturient pulvinar leo referrentur per vestibulum reformidans
                |      cursus qualisque te mauris tortor fabellas porttitor debet eripuit animal scripserit
                |      potenti simul dolores repudiare neglegentur himenaeos quot ubique mattis epicuri
                |      molestie definiebas imperdiet eu suas ponderum scelerisque legere fames persecuti
                |      populo augue sollicitudin convenire velit quis eloquentiam veri explicari graecis
                |  - date: '1120-06-05'
                |    event: Done
                |    description: egestas offendit no morbi quaestio quot eleifend falli populo non
                |      primis reformidans scripserit doctus dignissim erroribus atqui commune posse
                |      vitae persequeris quaestio percipit luptatum possim postea ante adolescens inciderint
                |      ignota primis ne efficitur primis his impetus fabulas quaestio verear theophrastus
                |      liber epicuri definitionem esse delectus suscipiantur cursus conclusionemque
                |      omittam ei luptatum vix aliquet scripserit reprimique patrioque inceptos quem
                |      ultricies iisque explicari cum recteque similique dissentiunt nullam alienum
                |      feugait mea vocibus ridens noluisse accusata falli aliquid antiopam menandri
                |      sapientem brute fabulas amet augue graeci class tale impetus audire
                |  - date: '1259-02-03'
                |    event: Due
                |    description: scripserit fringilla dolor augue maecenas doctus cursus dicta his
                |      qui similique lacus vituperatoribus integer audire graecis verear his luptatum
                |      ligula impetus alia putent accusata
                |  - date: '1951-01-28'
                |    event: In Development
                |    description: inciderint porta eget iisque tota dolorem molestie
                |  - date: '2459-08-21'
                |    event: Conceived
                |    description: prompta sagittis molestiae penatibus molestie animal volumus sed
                |      ex maluisset vel praesent wisi no vix regione fugit equidem natoque suspendisse
                |      auctor brute solum aeque recteque quod consul recteque vis quam option vel referrentur
                |      adversarium sociis indoctum aliquip hac dictas appareat sollicitudin duo amet
                |      aptent mazim fabellas tincidunt tale dignissim curabitur persecuti tritani tristique
                |      efficitur pellentesque conceptam ipsum
                |  - date: '2654-07-06'
                |    event: Decommissioned
                |    description: non odio ornatus feugiat commune rutrum tellus justo referrentur
                |      wisi accommodare auctor at mauris similique aliquip sapien eum gravida persequeris
                |      verear docendi consul dolorum erat convenire varius scelerisque in vero verterem
                |      dico instructior mollis nullam latine quo mediocrem labores theophrastus civibus
                |  - date: '2698-05-02'
                |    event: Active
                |    description: latine delenit iisque ante interdum duo sapien pericula
                |  - date: '2875-10-20'
                |    event: In Production
                |    description: noluisse ultricies reformidans adipiscing conceptam eius brute varius
                |      volumus necessitatibus porttitor invenire fabulas sapien ut sociis oporteat
                |      fabulas turpis aliquet pertinax deterruisset imperdiet elaboraret at fabellas
                |      elitr neglegentur dolorum eam intellegat doctus sonet odio sem sonet et velit
                |      odio iisque gloriatur contentiones lectus ligula persequeris suspendisse ad
                |      efficiantur
                |  - date: '0576-02-22'
                |    event: Retired
                |    description: volutpat ullamcorper harum class offendit delenit id nominavi dictas
                |      sententiae constituto fames definiebas proin dicta mauris orci duo tota audire
                |      detraxit reprehendunt elaboraret consectetur molestie pretium facilisi vidisse
                |      etiam imperdiet dolore nostrum persecuti rhoncus vim aliquip euripidis gravida
                |      eruditi vituperatoribus mucius decore nascetur iaculis aliquip vivendo suscipiantur
                |      vocibus aliquam persius facilisi menandri tibique semper sadipscing graece eam
                |      ornare pertinax persecuti dapibus numquam pertinacia duo sollicitudin doctus
                |  - date: '0064-12-04'
                |    event: In Preproduction
                |    description: vitae lacinia sed delectus maximus aliquip per petentium inani quaerendum
                |      interpretaris an in explicari pertinax ac neque ex veri definiebas solum tamquam
                |      inciderint verterem cetero nobis suscipiantur laoreet sed eros etiam instructior
                |      quidam disputationi delectus cursus litora vehicula moderatius autem netus convenire
                |      dui magnis eam vituperata errem suspendisse honestatis singulis
                |  - date: '0764-07-24'
                |    event: Started
                |    description: antiopam noluisse mazim consetetur auctor tamquam dicta erroribus
                |      vituperatoribus dis adipiscing aliquam fames nonumy graeci cu periculis cubilia
                |      oratio molestiae libero iudicabit vituperata tota amet deterruisset ponderum
                |      sonet populo aeque referrentur nisl dicta ad ignota tritani reque quod simul
                |      unum vero has hinc dico inceptos menandri
                |  attributes:
                |  - name: quo
                |    description: class causae definitionem scripserit utroque consequat
                |    type: feugiat
                |    required: false
                |    multiple: false
                |  - name: dolorum
                |    description: ancillae petentium sit nihil finibus volutpat ius menandri vivamus
                |      pertinax nonumes has massa simul reprimique mel simul voluptaria pharetra viderer
                |      nonumes sumo debet pro deterruisset semper vix delectus proin singulis efficiantur
                |    type: vero
                |    required: true
                |    multiple: false
                |  - name: oratio
                |    description: neque et solum suspendisse mediocritatem sanctus conclusionemque
                |      delenit vulputate dolor risus id veri impetus signiferumque
                |    type: definitionem
                |    required: true
                |    multiple: true
                |  - name: putent
                |    description: facilisi vitae nostrum platonem animal homero nominavi gravida alienum
                |      vituperata enim dicat invenire laoreet expetendis adolescens justo platea atqui
                |      decore quo conceptam facilisi dapibus placerat fabellas porttitor felis necessitatibus
                |      platonem venenatis tamquam noster ferri epicuri aliquet no salutatus voluptaria
                |      ferri elementum fastidii luptatum constituam consul voluptaria viderer te nominavi
                |      saepe consul interdum saperet ea vocibus agam
                |    type: dicta
                |    required: false
                |    multiple: false
                |  - name: vocibus
                |    description: maecenas idque cu contentiones iaculis ornatus amet sententiae petentium
                |      oporteat laoreet doctus quem convallis lorem nisl brute inani consetetur sumo
                |      scelerisque ligula
                |    type: dicant
                |    required: true
                |    multiple: true
                |  - name: doming
                |    description: veniam nostra conclusionemque error pri tortor dictas evertitur mus
                |      mucius dicunt electram delenit sententiae decore possim vulputate eleifend vitae
                |      graece invidunt mnesarchum quaeque ponderum dicant tractatos accommodare comprehensam
                |      sapientem at mediocrem graece mea mattis appareat egestas sit dicit maiestatis
                |      oporteat conclusionemque pertinacia rhoncus quot dicunt
                |    type: taciti
                |    required: false
                |    multiple: true
                |  - name: qui
                |    description: verear electram etiam ei reformidans porttitor nunc quot ponderum
                |      graecis mazim expetenda semper suscipiantur dissentiunt solet dolores erat qualisque
                |      consectetuer fastidii omittantur varius finibus fugit discere ut
                |    type: error
                |    required: false
                |    multiple: true
                |  - name: deserunt
                |    description: accommodare metus quaestio quod sententiae graeco atomorum posse
                |      facilisis phasellus pharetra melius vituperata prodesset esse
                |    type: adversarium
                |    required: true
                |    multiple: true
                |  sensitive: antiopam laudem gloriatur dicit ferri ponderum habemus dapibus nunc senectus
                |    saepe possim mandamus semper delectus alia intellegebat aliquet movet possim mi
                |    homero mei saperet rutrum pro verear litora simul quisque maiorum ridens nostra
                |    fusce repudiare eu interdum tamquam eruditi maiorum proin felis accumsan brute
                |    alterum purus noluisse corrumpit definiebas nonumy molestiae eum nunc indoctum
                |    autem lacus est putent feugiat vitae iisque vel etiam turpis vituperata movet
                |    cursus reformidans quaerendum mollis error eos hinc harum movet petentium mei
                |    accusata inciderint litora amet salutatus vim decore nominavi ceteros ridiculus
                |    cursus harum tibique ludus vix sapien
                |- key: value_object_1
                |  sortKey: '000000019'
                |  type: value object
                |  title: Magnis Utinam
                |  description:
                |  - velit malorum intellegebat docendi et sententiae phasellus posuere dolores fusce
                |    omittantur instructior repudiandae nascetur mediocrem sociosqu tota similique
                |    dignissim honestatis volumus convallis imperdiet vocent epicurei adolescens ad
                |    scripserit dignissim definitionem necessitatibus idque senserit semper vituperatoribus
                |    commodo graecis ultricies repudiandae viderer mel sumo quaeque mediocrem commodo
                |    ponderum iusto conubia commune vocibus eum falli utamur principes consetetur leo
                |    causae veniam iudicabit purus harum causae pretium partiendo quisque laudem labores
                |    postea vivendo natum epicuri vituperata contentiones reprehendunt numquam semper
                |    tritani nominavi singulis tritani fabulas solum ridens natum
                |  - laudem commodo habemus nominavi velit ius numquam dicant moderatius unum vitae
                |    ac aliquam habeo solet disputationi vidisse facilis legere vivamus maiorum expetenda
                |    fastidii nibh invidunt primis meliore sale mei
                |  - honestatis cum iuvaret platonem commodo aliquip melius laoreet utamur sociis vocibus
                |    mea purus eos voluptatibus bibendum erroribus persequeris malorum tation alterum
                |    mi mus scripserit luctus saepe vivamus lacinia detracto tale libero eius homero
                |    class posse mauris definiebas mattis dicunt unum elit epicurei sapien blandit
                |    ligula dico partiendo viris faucibus solet repudiandae suas theophrastus platea
                |    mei habitant vocent
                |  links:
                |  - type: Technical Documentation
                |    url: https://search.yahoo.com/search?p=instructior
                |    title: Doming Noster
                |  - type: API Documentation
                |    url: http://www.bing.com/search?q=assueverit
                |    title: Luctus Mediocritatem
                |  - type: Website
                |    url: https://www.google.com/#q=ludus
                |    title: Class Melius
                |  - type: Website
                |    url: https://search.yahoo.com/search?p=turpis
                |    title: Menandri
                |  externalIds:
                |  - system: eirmod
                |    id: 796fb4ff_b2c5_4f10_ac15_dd39e81032cf
                |  - system: mea
                |    id: c862f3d4_d3b1_4801_9a63_d925d73b158b
                |  fatherTime:
                |  - date: '1124-08-10'
                |    event: Started
                |    description: sociis accumsan vivamus cursus quo causae condimentum iisque sagittis
                |      ubique maximus dolores ei tincidunt adipiscing alterum tota volumus maiorum
                |      inani veniam rutrum reprehendunt mediocritatem natoque auctor sapientem instructior
                |      quidam fames populo offendit vituperata decore ridens quidam vero repudiare
                |      sumo tincidunt novum fermentum tacimates tation habitasse sed neque mei dicit
                |      dissentiunt duo graeci evertitur semper mi gravida civibus ceteros tantas ocurreret
                |      his saepe ei repudiare theophrastus definitiones quo antiopam volutpat euripidis
                |      no sanctus corrumpit docendi mentitum noluisse quisque intellegat quaeque melius
                |      cras cu sollicitudin discere placerat tation interdum egestas harum theophrastus
                |      fusce comprehensam tempus vivendo neque mea tale wisi
                |  - date: '2068-02-12'
                |    event: Decommissioned
                |    description: dolorum equidem sociosqu urna falli delicata
                |  - date: '2083-07-20'
                |    event: Due
                |    description: periculis duis tacimates melius habitant simul commodo nonumes tacimates
                |      quas minim dictas ex facilisis deseruisse debet has mei sapientem alienum nascetur
                |      risus definitiones habemus tantas consectetur alterum fuisset putent efficitur
                |      eos vel constituto maiorum ridiculus facilisi ultricies vel eleifend
                |  - date: '0219-06-25'
                |    event: In Development
                |    description: solet oratio fames venenatis instructior eripuit pro ubique lorem
                |      detraxit eloquentiam simul ius salutatus quisque aperiri proin mel malesuada
                |      dico noluisse fusce populo contentiones cubilia intellegebat iusto nec consequat
                |      quem efficiantur elaboraret animal efficiantur disputationi parturient eruditi
                |      curabitur velit periculis disputationi curae ipsum regione facilisi platea persecuti
                |      atqui necessitatibus pertinax in lacus explicari taciti utroque eros netus reprimique
                |      utroque adipisci
                |  - date: '2294-07-01'
                |    event: In Preproduction
                |    description: fastidii vero oporteat iudicabit pellentesque singulis ornatus indoctum
                |      duo nec honestatis noster dico blandit percipit quis himenaeos volumus fames
                |      dolor viderer
                |  - date: '2601-01-24'
                |    event: Done
                |    description: propriae appetere omittantur neglegentur dicta finibus viverra animal
                |      condimentum dicant turpis scelerisque aeque appareat tale interpretaris adhuc
                |      maiorum idque explicari molestie integer nisi qualisque errem ultrices elit
                |      at habeo ne an legere commune cetero imperdiet graece no nisl molestie quod
                |      mattis viris sed tacimates voluptaria audire autem dolorum mazim mattis sollicitudin
                |      curae per civibus mnesarchum pretium signiferumque volutpat pertinacia felis
                |      in sociosqu deterruisset dico parturient
                |  - date: '2751-05-13'
                |    event: Retired
                |    description: consequat splendide netus adipiscing homero voluptaria fermentum
                |      nobis vis dicant putent vituperata magnis elaboraret aliquip reformidans iusto
                |      facilis iusto qualisque ullamcorper ignota idque alienum vehicula facilis vero
                |      ac evertitur odio efficitur class agam sed aenean viderer in gravida ultricies
                |      disputationi quidam viverra luctus parturient a molestiae sociosqu erat scripserit
                |      a aeque fabulas civibus mazim appetere
                |  - date: '2872-03-05'
                |    event: Active
                |    description: an vix porttitor consetetur vidisse adversarium quaerendum imperdiet
                |      dicta definitionem convallis fames quas vim pericula tellus nisi maluisset accommodare
                |      euripidis maximus sententiae
                |  - date: '2875-10-09'
                |    event: In Production
                |    description: vim explicari ea posidonium offendit sadipscing ubique has homero
                |      instructior deseruisse nominavi enim euripidis assueverit postea quisque altera
                |      convallis habitant volutpat convenire nulla rhoncus maiorum gravida homero posidonium
                |      novum suspendisse persius omittam euismod erat iusto reprimique atomorum hinc
                |      labores sagittis mei mediocritatem aliquet prodesset omittam cetero invidunt
                |      varius maiestatis homero sadipscing ferri arcu ius scelerisque lorem primis
                |      id idque taciti mollis pro sociis nibh eos viverra adhuc sodales adhuc electram
                |      aliquam nisi nominavi ignota vis graecis ius ultricies inciderint molestiae
                |      mediocrem ocurreret
                |  - date: '0334-09-12'
                |    event: Life Event
                |    description: hendrerit praesent viverra vestibulum rutrum detraxit eruditi natoque
                |      assueverit elit tation hendrerit graeci cursus natum conceptam alterum debet
                |      reque mazim fermentum mutat posidonium eros possit eos epicuri singulis vitae
                |      autem fringilla facilisis inimicus dicat dicam aliquam persecuti consetetur
                |      option eum faucibus porro mei laoreet sociosqu erroribus bibendum ubique ut
                |      idque vocent animal natoque facilis moderatius iudicabit possim commune accusata
                |      felis corrumpit dicat
                |  - date: '0847-03-03'
                |    event: Conceived
                |    description: litora decore mel urbanitas class utamur magna massa causae duo velit
                |      integer perpetua invenire suavitate curae suas electram verterem senserit fringilla
                |      interdum ultricies indoctum conubia harum vituperatoribus phasellus appetere
                |      assueverit idque tibique finibus ponderum duo persius scripta nam lacus senserit
                |      option cubilia netus justo velit curabitur gloriatur posse commune primis facilisis
                |      legere gloriatur partiendo ne fugit viverra iriure
                |  attributes:
                |  - name: donec
                |    description: saperet scripserit facilisis similique id docendi periculis nisl
                |      quem option repudiandae errem
                |    type: scripserit
                |    required: true
                |    multiple: true
                |  - name: feugiat
                |    description: eros natoque vulputate efficitur electram omnesque non feugiat quem
                |      sanctus graecis tibique oporteat
                |    type: idque
                |    required: false
                |    multiple: false
                |  - name: penatibus
                |    description: doctus te sociis pharetra suscipiantur eget tantas tempus ultricies
                |      curabitur agam a delectus mediocritatem convallis suas efficitur turpis taciti
                |      habeo putent dolorem ut ullamcorper equidem deterruisset nisi diam habitant
                |      mediocritatem interpretaris eam class mucius efficiantur aliquid graecis fusce
                |      delenit sociis aperiri quidam platonem utinam vocent ius mel doctus congue eos
                |      proin prompta tractatos augue meliore quisque ea atomorum splendide porta constituto
                |      reformidans tation bibendum neque semper luctus vis eu lobortis atomorum mentitum
                |      elit litora docendi id parturient suas blandit ludus quaerendum torquent quem
                |      ius felis alterum equidem ocurreret
                |    type: iuvaret
                |    required: true
                |    multiple: false
                |  - name: repudiandae
                |    description: ad ea tortor mutat tellus facilisi morbi maiestatis curabitur assueverit
                |      mandamus vestibulum maluisset sociis mea intellegat ipsum vidisse numquam propriae
                |      consul repudiare utroque contentiones ultricies salutatus rhoncus maiorum repudiandae
                |      sententiae viverra curabitur nihil explicari cras litora vim eos consectetur
                |      senectus deserunt veritus vix vulputate neque torquent cetero curabitur duis
                |      eleifend auctor vestibulum moderatius cras dicat non dapibus fastidii deseruisse
                |      ridiculus
                |    type: reque
                |    required: false
                |    multiple: false
                |  public: qualisque voluptatibus option nostrum voluptaria dolor convallis fabellas
                |    sodales ea vitae offendit ligula an diam posse aptent habitasse te mauris postulant
                |    assueverit mea arcu definiebas definitionem nostra per adipiscing natoque eos
                |    tibique fugit vulputate mus dolor convenire dictumst facilisi ipsum tortor vituperata
                |    tellus taciti fusce quas cu adipisci turpis explicari veritus senectus quaeque
                |    eripuit lectus evertitur ut neque gloriatur fabellas sagittis mandamus ius ac
                |    alia efficitur eleifend persius luctus voluptaria convallis placerat similique
                |    mazim ac aliquid intellegebat inimicus accusata aptent pulvinar nominavi velit
                |    proin aenean possim dicant harum constituam fastidii augue iisque esse impetus
                |    dictum dicunt
                |systems:
                |- key: it_system_1
                |  sortKey: '000000025'
                |  title: Adipisci Cras
                |  description:
                |  - urna debet docendi postea adipisci urbanitas sententiae gravida patrioque moderatius
                |    mandamus dignissim dolore civibus tortor nascetur scelerisque
                |  - ne agam perpetua bibendum wisi moderatius contentiones erroribus regione signiferumque
                |    numquam lobortis sea fabellas vocent alia fuisset intellegebat dolores eruditi
                |    aliquip magna varius omittam class iusto intellegat patrioque eget harum conceptam
                |    vocibus esse ridens
                |  - facilisis ipsum nostrum ocurreret constituam risus latine cras dicit recteque
                |    condimentum detraxit mediocrem pellentesque sollicitudin legimus an falli adversarium
                |    tristique doctus vituperatoribus molestie ac recteque discere ipsum assueverit
                |    legimus error fames et vituperata usu consequat conceptam diam dolorem fringilla
                |    fermentum quisque sea dictumst malorum pertinacia adipisci urbanitas salutatus
                |    risus vivendo regione mucius mucius delenit vocent contentiones vidisse tritani
                |    quaestio sapien veritus postea efficitur elaboraret quod eum interesset tation
                |    sollicitudin quo nam pellentesque malesuada velit magna sapien ullamcorper ac
                |    comprehensam legimus vero augue vidisse verear fugit expetendis aptent noluisse
                |    natoque dicit expetendis novum percipit tantas
                |  - adipiscing mel laudem ancillae mattis accumsan tale iaculis meliore dicta risus
                |    torquent iaculis laoreet adolescens affert risus quem honestatis vulputate assueverit
                |    brute malesuada malorum patrioque altera viverra eros porro te adolescens consul
                |    has rutrum quas maecenas ac eripuit no qui dui tritani iudicabit tamquam euripidis
                |    mei amet ac sit deserunt posse ridiculus eu constituto tempor electram docendi
                |    iaculis pretium propriae quidam a intellegebat himenaeos mediocritatem
                |  - ipsum urbanitas corrumpit pericula dictumst impetus malesuada ut inimicus mattis
                |    senectus malorum veritus constituto luptatum dolorum viris imperdiet mediocritatem
                |    liber feugait feugait definitiones ut legimus hendrerit mollis mus veri graece
                |    quas sententiae dico option aliquip id fabulas placerat accommodare facilis tota
                |    oporteat proin taciti lobortis te vitae elitr id quas aptent potenti alterum regione
                |    decore suspendisse inimicus delenit pretium viris oratio interesset atqui massa
                |    eos causae eu molestie sodales convenire aliquam
                |  links:
                |  - type: Production URL
                |    url: http://www.bing.com/search?q=utroque
                |    title: Pulvinar Decore
                |  - type: Development URL
                |    url: https://www.google.com/#q=hac
                |    title: Oporteat Consetetur
                |  - type: Architecture Documentation
                |    url: https://www.google.com/#q=propriae
                |    title: Dicat
                |  - type: Website
                |    url: https://duckduckgo.com/?q=sociis
                |    title: Fabulas Magnis
                |  - type: Functional Documentation
                |    url: https://search.yahoo.com/search?p=inimicus
                |    title: Graeci Iaculis
                |  - type: Functional Documentation
                |    url: http://www.bing.com/search?q=quaestio
                |    title: Salutatus has
                |  - type: Website
                |    url: https://duckduckgo.com/?q=imperdiet
                |    title: Viris
                |  - type: Development URL
                |    url: http://www.bing.com/search?q=efficitur
                |    title: Pertinax Veri
                |  - type: Functional Documentation
                |    url: https://search.yahoo.com/search?p=reprimique
                |    title: Debet
                |  swot:
                |    opportunities:
                |    - eius repudiare vocent augue errem cu aenean persius tincidunt eius splendide
                |      vestibulum dolorum ridens qui luptatum mauris maluisset nam
                |    - iriure veritus principes nisl platea iusto elitr non ridens novum pertinacia
                |      atqui ornare gubergren definitionem offendit molestie vivendo platea suavitate
                |      affert veniam theophrastus eam dolores nec quis eum imperdiet detracto expetenda
                |      equidem interpretaris definitiones dis consul iriure voluptaria fringilla persequeris
                |      nam penatibus porta iusto legere no urbanitas at habitant maiestatis option
                |      eripuit
                |    threats:
                |    - assueverit alterum propriae consectetuer idque aenean eos aliquam fastidii nominavi
                |      potenti persequeris quis himenaeos luctus
                |  invest: graeci repudiandae tibique viderer persius amet sumo vocibus movet senserit
                |    dictumst fabulas vero eirmod appetere harum sapientem massa constituto repudiandae
                |    mauris antiopam quis cum nullam facilisis suas inimicus prompta iriure cu mnesarchum
                |    a dictas libero ludus euripidis dolore ea dolores contentiones quam quaestio prodesset
                |    sem cu omittam voluptatum reformidans nec
                |  criticality:
                |    level: Major
                |    consequences: meliore consequat malorum nulla posse persequeris constituam arcu
                |      ubique legimus porro his honestatis posidonium cras fringilla suscipiantur id
                |      duo purus dolore ante hac omittantur salutatus atomorum harum conubia tractatos
                |      gloriatur penatibus quaestio facilisis his scripta adhuc patrioque animal nulla
                |      instructior putent usu debet equidem honestatis morbi litora sanctus postulant
                |      persecuti antiopam ludus civibus voluptaria perpetua periculis disputationi
                |      dolor reformidans epicuri laudem delenit tritani torquent magna congue conceptam
                |      iuvaret verterem tantas alterum mutat idque unum atomorum scripta assueverit
                |      consequat dicam impetus tota dolorem omnesque eirmod luctus comprehensam aliquet
                |      ea
                |  externalIds:
                |  - system: ut
                |    id: b7fc53a3_5eea_4336_aefe_285f3fcfe3a5
                |  - system: voluptatum
                |    id: 56fca8b1_7900_4567_a8d2_7a8bc8aa6d97
                |  fatherTime:
                |  - date: '1555-10-23'
                |    event: Decommissioned
                |    description: fames curae eius fugit pericula interpretaris habitasse eleifend
                |      elaboraret pretium magnis doming voluptatibus qualisque varius alia vel viderer
                |      massa maiorum option efficiantur alterum option platea eius ferri quod mi cursus
                |      iusto eloquentiam proin ornatus condimentum mauris harum nam lectus necessitatibus
                |      gubergren mi commune litora senserit sadipscing quaeque ac mediocritatem noluisse
                |      saperet tempus viderer adipisci morbi cubilia ornatus sagittis nulla litora
                |      atomorum quam quaestio voluptatum ea equidem voluptatum habeo platea voluptatibus
                |      malesuada sed prodesset aenean dapibus sagittis decore tempor
                |  - date: '1638-10-08'
                |    event: In Production
                |    description: suscipit faucibus quisque torquent alienum dicat quis ea mea latine
                |      latine atomorum eam elaboraret constituto taciti autem epicurei cubilia alterum
                |      dictum animal vivendo elementum populo oporteat referrentur liber senserit pellentesque
                |      adipisci vehicula viderer graece porro class odio offendit putent quas comprehensam
                |      pertinax dolores malorum felis consequat orci imperdiet persequeris laudem definitiones
                |      elaboraret novum ad augue cursus eu lorem latine iuvaret veritus eripuit nominavi
                |      sodales homero mei reformidans lacinia necessitatibus nominavi mea possim intellegat
                |      fames etiam hac honestatis erat postea pertinax antiopam persecuti habeo decore
                |      malorum explicari aliquet invenire nostrum per
                |  - date: '1653-08-27'
                |    event: Done
                |    description: habemus duis quisque antiopam dolorem tortor utroque persius fermentum
                |      est oratio utamur aptent discere ac conubia an adipisci malorum sonet aliquip
                |      pharetra viris platea pharetra mazim arcu nascetur maximus cursus natoque mel
                |      eripuit nascetur ut invidunt cu homero reprimique commodo montes inimicus hinc
                |      falli
                |  - date: '1806-02-15'
                |    event: Active
                |    description: primis an nisl facilisis mutat elaboraret sem tacimates netus vulputate
                |      utamur vestibulum definitionem mazim et suas tempor nullam bibendum sanctus
                |      atomorum suas suas turpis urbanitas finibus ferri definitiones porro atqui decore
                |  - date: '1948-01-01'
                |    event: Started
                |    description: montes persequeris adolescens congue velit fringilla elaboraret disputationi
                |      magna accommodare posidonium habitant rhoncus fabellas sollicitudin id nunc
                |      elementum partiendo liber tation iisque tibique pulvinar brute natoque consectetur
                |      interdum amet veri mollis posidonium tempus dignissim mus mauris malesuada quaeque
                |      adipisci duo taciti tractatos quis vehicula dicat urbanitas cetero tibique posidonium
                |      quidam nonumes vivamus reprehendunt litora option pretium disputationi appetere
                |      sententiae duo aliquet torquent sit partiendo eleifend ludus voluptatibus
                |  - date: '2145-11-14'
                |    event: Retired
                |    description: sale aliquip dicat litora vehicula viderer te commodo accusata mus
                |      posuere orci ludus egestas venenatis pro sapien voluptatibus duo graeci putent
                |      viderer melius oporteat mentitum consectetuer et ignota eos vitae sapien fastidii
                |      erroribus feugait aliquet senectus reformidans arcu graecis iriure singulis
                |      montes nisi an commodo moderatius fusce phasellus sapien neque porttitor salutatus
                |      morbi cursus inceptos varius qui suspendisse nullam iusto lectus alienum mentitum
                |      elementum
                |  - date: '2362-11-06'
                |    event: In Development
                |    description: sale periculis scelerisque scelerisque delicata tacimates consequat
                |      senectus salutatus liber iaculis inimicus delectus tortor iisque tamquam aliquip
                |      ultricies maiorum ea possim veritus altera a amet ex vix netus ante disputationi
                |      morbi mucius eius dictas commodo consul labores iudicabit hinc idque graece
                |      animal maluisset an hac finibus ne cubilia justo urbanitas eius persequeris
                |      dico dictumst adipisci iudicabit integer eruditi ridiculus ei appareat nonumes
                |      deserunt expetenda potenti
                |  - date: '0245-02-26'
                |    event: Due
                |    description: conceptam augue decore indoctum noluisse corrumpit delenit vidisse
                |      postulant maecenas duis massa porttitor possit vituperatoribus causae noster
                |      tractatos porttitor suspendisse pri sale egestas fabulas no mi affert ius mus
                |      fermentum quem sit debet constituto
                |  - date: '0068-03-22'
                |    event: Life Event
                |    description: iudicabit definiebas doctus vel platea facilisis delectus numquam
                |      his gloriatur sententiae antiopam augue mucius persecuti bibendum regione possit
                |      primis persequeris nisl necessitatibus vocent dicunt parturient dicam percipit
                |      sea lectus dolore tellus hinc nam
                |  - date: '0814-01-16'
                |    event: Conceived
                |    description: convenire magnis appareat legimus qui sapien blandit eu eius ullamcorper
                |      per aenean consul dissentiunt commodo reque a fugit maximus epicuri
                |  - date: '0985-05-22'
                |    event: In Preproduction
                |    description: consectetur duo luctus cubilia molestie scripta vero mediocrem persequeris
                |      mucius iusto deseruisse elementum euripidis definitionem convallis taciti movet
                |      suscipit suavitate metus tellus commodo cetero adhuc corrumpit civibus eam mattis
                |      adhuc constituam est audire
                |  resilience:
                |  - habeo voluptatibus saperet vituperata velit inciderint neglegentur turpis doctus
                |    sententiae sem menandri mazim ludus reprimique lacus altera quam tota facilisis
                |    sem periculis egestas diam vis dicat elit adolescens porttitor verterem dignissim
                |    dis etiam pericula ullamcorper volutpat ullamcorper fringilla constituam noster
                |    sale sea disputationi sociosqu liber in populo nisl duo dolorum quisque ius mutat
                |    praesent sale condimentum
                |  accessing:
                |  - key: accessing_1
                |    target: entity_1
                |    title: accesses""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    DataObjectYamlIO.read(data)
    ItSystemYamlIO.read(data)
    RelationshipBuffer.save()
    println(townPlan.relationships)
  }
}
