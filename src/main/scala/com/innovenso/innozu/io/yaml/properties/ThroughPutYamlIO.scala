package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties.{
  Criticality,
  Frequency,
  HasCriticality,
  HasThroughput,
  Throughput,
  UnknownCriticality,
  Volume
}

object ThroughPutYamlIO extends YamlPropertiesIO[Throughput, HasThroughput] {
  override def write(hasThroughput: HasThroughput, data: YamlData): Unit =
    if (hasThroughput.volume.nonEmpty || hasThroughput.frequency.nonEmpty) {
      debug(
        s"writing throughput ${hasThroughput.volume} & ${hasThroughput.frequency}"
      )
      data.put(
        "throughput",
        writeOne(hasThroughput.volume, hasThroughput.frequency).get
      )
    }

  def writeOne(
      volume: Option[Volume],
      frequency: Option[Frequency]
  ): Option[YamlJavaData] = Some(
    withMap { map =>
      volume.foreach(v => map.put("volume", v.description))
      frequency.foreach(f => map.put("frequency", f.description))
    }
  )

  def readFrequency(data: YamlJavaData): Option[Frequency] =
    readMap(data, "throughput")
      .flatMap(readOneFrequency)

  def readVolume(data: YamlJavaData): Option[Volume] =
    readMap(data, "throughput")
      .flatMap(readOneVolume)

  def readOneFrequency(data: YamlJavaData): Option[Frequency] =
    Throughput
      .fromString("frequency", readString(data, "frequency").getOrElse(""))
      .filter(_.isInstanceOf[Frequency])
      .map(_.asInstanceOf[Frequency])

  def readOneVolume(data: YamlJavaData): Option[Volume] =
    Throughput
      .fromString("volume", readString(data, "volume").getOrElse(""))
      .filter(_.isInstanceOf[Volume])
      .map(_.asInstanceOf[Volume])

}
