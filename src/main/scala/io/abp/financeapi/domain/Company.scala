package io.abp.financeapi.domain

import Company._
import io.estatico.newtype.macros.newtype

case class Company(id: Id, name: Name)

object Company {
  @newtype case class Id(asString: String)
  @newtype case class Name(asString: String)
}
