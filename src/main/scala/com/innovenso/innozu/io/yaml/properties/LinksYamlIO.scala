package com.innovenso.innozu.io.yaml.properties

import com.innovenso.innozu.io.yaml.YamlIO
import com.innovenso.townplanner.model.concepts.properties.{HasLinks, Link}

import scala.jdk.CollectionConverters.SeqHasAsJava

object LinksYamlIO extends YamlPropertiesIO[Link, HasLinks] {
  override def write(hasLinks: HasLinks, data: YamlData): Unit = if (
    hasLinks.links.nonEmpty
  ) {
    debug(s"writing links ${hasLinks.links}")
    data.put("links", hasLinks.links.flatMap(writeOne).asJava)
  }

  override def writeOne(link: Link): Option[YamlJavaData] = Some(
    withMap { map =>
      debug(s"writing link $link")
      map.put("type", link.name)
      map.put("url", link.url)
      map.put("title", link.title)
    }
  )

  override def readMany(data: YamlJavaData): List[Link] =
    readMaps(data, "links").flatMap(readOne)

  override def readOne(data: YamlJavaData): Option[Link] = for {
    linkType <- readString(data, "type").orElse(Some("Website"))
    url <- readString(data, "url")
    title <- readString(data, "title").orElse(readString(data, "url"))
  } yield Link.fromString(linkType, url, Some(title))

}
