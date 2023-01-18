package com.innovenso.innozu.io.yaml

import com.innovenso.townplanner.model.concepts.properties.{HasLinks, Link}

import scala.jdk.CollectionConverters.SeqHasAsJava

object LinksYamlIO extends YamlIO {
  def write(hasLinks: HasLinks, data: YamlData): Unit = if (
    hasLinks.links.nonEmpty
  ) {
    debug(s"writing links ${hasLinks.links}")
    data.put("links", hasLinks.links.map(write).asJava)
  }

  def write(link: Link): YamlJavaData = withMap { map =>
    debug(s"writing link $link")
    map.put("type", link.name)
    map.put("url", link.url)
    map.put("title", link.title)
  }

  def readLinks(data: YamlJavaData): List[Link] =
    readMaps(data, "links").flatMap(readLink)

  def readLink(data: YamlJavaData): Option[Link] = for {
    linkType <- readString(data, "type").orElse(Some("Website"))
    url <- readString(data, "url")
    title <- readString(data, "title").orElse(readString(data, "url"))
  } yield Link.fromString(linkType, url, Some(title))

}
