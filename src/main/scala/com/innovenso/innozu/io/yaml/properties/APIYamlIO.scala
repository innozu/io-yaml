package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  API,
  ApiScope,
  ApiStyle,
  AuthenticationType,
  DDoSProtection,
  Frequency,
  HasAPI,
  HasThroughput,
  RateLimiting,
  Throughput,
  Volume
}

object APIYamlIO extends YamlPropertiesIO[API, HasAPI] {
  override def write(hasAPI: HasAPI, data: YamlData): Unit =
    if (hasAPI.api.nonEmpty) {
      debug(
        s"writing API ${hasAPI.api.get}"
      )
      data.put(
        "api",
        writeOne(hasAPI.api.get).get
      )
    }

  override def writeOne(property: API): Option[APIYamlIO.YamlJavaData] = Some(
    withMap { map =>
      debug(s"writing api $property")
      map.put(
        "style",
        writeTitleAndDescription(
          property.style.title,
          property.style.description
        )
      )
      map.put(
        "scope",
        writeTitleAndDescription(
          property.scope.title,
          property.scope.description
        )
      )
      map.put(
        "authentication",
        writeTitleAndDescription(
          property.authentication.title,
          property.authentication.description
        )
      )
      map.put(
        "ddos",
        writeTitleAndDescription(
          property.ddoSProtection.title,
          property.ddoSProtection.description
        )
      )
      map.put(
        "rateLimiting",
        writeTitleAndDescription(
          property.ddoSProtection.title,
          property.ddoSProtection.description
        )
      )
    }
  )

  def writeTitleAndDescription(
      title: String,
      description: Option[String]
  ): YamlJavaData = withMap { map =>
    map.put("title", title)
    description.foreach(d => map.put("description", d))
  }

  override def readOne(data: APIYamlIO.YamlJavaData): Option[API] =
    readMap(data, "api").flatMap(readApi)

  def readApi(data: APIYamlIO.YamlJavaData): Option[API] = for {
    authentication <- readTitleAndDescription(data, "authentication").orElse(
      Some(("", None))
    )
    scope <- readTitleAndDescription(data, "scope").orElse(Some(("", None)))
    style <- readTitleAndDescription(data, "style").orElse(Some(("", None)))
    ddos <- readTitleAndDescription(data, "ddos").orElse(Some(("", None)))
    rateLimiting <- readTitleAndDescription(data, "rateLimiting").orElse(
      Some(("", None))
    )
  } yield API(
    style = ApiStyle.fromString(style._1, style._2),
    authentication =
      AuthenticationType.fromString(authentication._1, authentication._2),
    scope = ApiScope.fromString(scope._1, scope._2),
    ddoSProtection = DDoSProtection.fromString(ddos._1, ddos._2),
    rateLimiting = RateLimiting.fromString(rateLimiting._1, rateLimiting._2)
  )

  def readTitleAndDescription(
      data: YamlJavaData,
      key: String
  ): Option[(String, Option[String])] = for {
    subMap <- readMap(data, key)
    title <- readString(subMap, "title")
    description <- Some(readString(subMap, "description"))
  } yield (title, description)

}
