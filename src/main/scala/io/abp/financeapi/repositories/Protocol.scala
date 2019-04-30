package io.abp.financeapi.repositories

import io.abp.financeapi.domain.{ Company => DCompany }

object Protocol {
  import Company._

  case class Company(id: Id, name: Name)

  object Company {
    case class Id(asString: String) extends AnyVal
    case class Name(asString: String) extends AnyVal
  }

  def toDomain(company: Company): DCompany = DCompany(
    DCompany.Id(company.id.asString),
    DCompany.Name(company.name.asString)
  )
}
