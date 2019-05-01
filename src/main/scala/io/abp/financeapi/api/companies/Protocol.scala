package io.abp.financeapi.api.companies

import fs2.Stream
import io.abp.financeapi.api.Protocol.valueClassEncoder
import io.abp.financeapi.domain.Company
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEncoder
import io.scalaland.chimney.dsl._
import java.time.LocalDate

object Protocol {
  import CompanyResponse._

  case class CompanyResponse(
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

  object CompanyResponse {
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

    implicit val config: Configuration =
      Configuration.default.withSnakeCaseMemberNames.withSnakeCaseConstructorNames

    implicit val encoder: Encoder[CompanyResponse] =
      deriveEncoder[CompanyResponse]

    implicit def toResponse[F[_]](
        company: Stream[F, Company]
    ): Stream[F, CompanyResponse] = company.map(toResponse)

    def toResponse[F[_]](company: Company): CompanyResponse =
      company.into[CompanyResponse].transform

  }
}
