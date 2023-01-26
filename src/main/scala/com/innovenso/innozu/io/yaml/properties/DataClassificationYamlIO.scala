package com.innovenso.innozu.io.yaml.properties

import com.innovenso.townplanner.model.concepts.properties._

object DataClassificationYamlIO
    extends YamlPropertiesIO[DataClassification, HasDataClassification] {
  override def write(
      hasDataClassification: HasDataClassification,
      data: YamlData
  ): Unit = {
    debug(
      s"writing data classification ${hasDataClassification.dataClassification}"
    )
    hasDataClassification.dataClassification.foreach(dc => {
      data.put(
        dc.level.title.toLowerCase,
        dc.description.getOrElse("")
      )
    })
  }

  def read(data: YamlJavaData): DataClassification =
    readOne(data).getOrElse(
      DataClassification(level = SensitiveData, description = None)
    )

  override def readOne(
      data: DataClassificationYamlIO.YamlJavaData
  ): Option[DataClassification] =
    readString(data, SensitiveData.title.toLowerCase)
      .map(description =>
        DataClassification(
          level = SensitiveData,
          description = Some(description)
        )
      )
      .orElse(
        readString(data, PersonalData.title.toLowerCase).map(description =>
          DataClassification(
            level = PersonalData,
            description = Some(description)
          )
        )
      )
      .orElse(
        readString(data, PublicData.title.toLowerCase)
          .map(description =>
            DataClassification(
              level = PublicData,
              description = Some(description)
            )
          )
      )
      .orElse(
        readString(data, ConfidentialData.title.toLowerCase).map(description =>
          DataClassification(
            level = ConfidentialData,
            description = Some(description)
          )
        )
      )
}
