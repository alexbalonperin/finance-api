package io.abp.financeapi.repositories

import java.time.LocalDate
import io.abp.financeapi.domain.Company
import io.scalaland.chimney.dsl._

object Protocol {
  import CompanyRow._

  case class CompanyRow(
      id: Id,
      name: Name,
      symbol: Symbol,
      liquidated: Liquidated,
      delisted: Delisted,
      active: Active,
      lastTradeDate: Option[LastTradeDate],
      firstTradeDate: Option[FirstTradeDate],
      industry: Industry,
      sector: Sector,
      market: Market
  )

  object CompanyRow {
    case class Id(asInt: Int) extends AnyVal
    case class Name(asString: String) extends AnyVal
    case class Symbol(asString: String) extends AnyVal
    case class Liquidated(asBool: Boolean) extends AnyVal
    case class Delisted(asBool: Boolean) extends AnyVal
    case class Active(asBool: Boolean) extends AnyVal
    case class LastTradeDate(asDate: LocalDate) extends AnyVal
    case class FirstTradeDate(asDate: LocalDate) extends AnyVal
    case class Industry(asString: String) extends AnyVal
    case class Sector(asString: String) extends AnyVal
    case class Market(asString: String) extends AnyVal
  }

  def toDomain(company: CompanyRow): Company = company.into[Company].transform
}
