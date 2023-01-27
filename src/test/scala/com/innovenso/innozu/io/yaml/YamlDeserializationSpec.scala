package com.innovenso.innozu.io.yaml

import com.innovenso.innozu.io.yaml.concepts.{
  ActorYamlIO,
  ArchitectureBuildingBlockYamlIO,
  BusinessCapabilityYamlIO,
  DataObjectYamlIO,
  EnterpriseYamlIO,
  ItPlatformYamlIO,
  ItSystemIntegrationYamlIO,
  ItSystemYamlIO,
  OrganisationYamlIO,
  PersonYamlIO,
  TeamYamlIO
}
import com.innovenso.innozu.io.yaml.properties.TitleYamlIO.YamlJavaData
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
        |""".stripMargin
    val data: YamlJavaData = yaml.load(yml).asInstanceOf[YamlJavaData]
    println(data)
    ItSystemYamlIO.read(data)
    assert(townPlan.systems.size == 2)
    ItSystemIntegrationYamlIO.read(data)
    assert(townPlan.systemIntegrations.size == 1)
  }
}
