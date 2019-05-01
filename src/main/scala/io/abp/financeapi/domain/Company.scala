package io.abp.financeapi.domain

import java.time.LocalDate
import Company._

case class Company(
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

object Company {
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

  val dummy =
    Company(
      Id(123),
      Name("Apple"),
      Symbol("aapl"),
      Liquidated(false),
      Delisted(false),
      Active(true),
      Some(LastTradeDate(LocalDate.now)),
      Some(FirstTradeDate(LocalDate.now.minusYears(10))),
      Industry("Computer Software: Prepackaged Software"),
      Sector("Technology"),
      Market("NYSE")
    )
}
