package com.innovenso.innozu.io.yaml.relationships

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.relationships.Relationship

object RelationshipBuffer {
  private var relationships: List[Relationship] = Nil

  def add(relationship: Relationship): Unit = relationships =
    relationship :: relationships

  def save()(implicit ea: EnterpriseArchitecture): Unit = {
    relationships.foreach(ea.hasRelationship)
    relationships = Nil
  }

  def clear()(implicit ea: EnterpriseArchitecture): Unit = relationships = Nil
}
