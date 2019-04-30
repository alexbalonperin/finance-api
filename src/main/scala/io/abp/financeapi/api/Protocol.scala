package io.abp.financeapi.api

import java.time.LocalDate
import cats.effect.Sync
import fs2.Stream
import io.abp.financeapi.domain.Company
import io.circe.fs2._
import io.circe.generic.extras.decoding.UnwrappedDecoder
import io.circe.generic.extras.encoding.UnwrappedEncoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}
import io.scalaland.chimney.dsl._

object Protocol {

  implicit val encoder: Encoder[CompanyResponse] = deriveEncoder[CompanyResponse]
  implicit def toResponse[F[_]](company: Stream[F, Company]): Stream[F, CompanyResponse] = company.map(toResponse)
  def toResponse[F[_]](company: Company): CompanyResponse = company.into[CompanyResponse].transform

  implicit def jsonDecoder[F[_]: Sync, A <: Product: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
  implicit def jsonEncoder[F[_]: Sync, A <: Product: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]
  implicit def valueClassEncoder[A: UnwrappedEncoder]: Encoder[A] = implicitly
  implicit def valueClassDecoder[A: UnwrappedDecoder]: Decoder[A] = implicitly

  private def streamArrayToString[F[_]: Sync, A](
      stream: Stream[F, A]
  )(implicit E: Encoder[A]): Stream[F, String] =
    Stream("[")
      .append(stream.map(E.apply(_).spaces2).intersperse(", "))
      .append(Stream("]"))

  implicit def streamToJson[F[_]: Sync, A](
      stream: Stream[F, A]
  )(implicit E: Encoder[A]): Stream[F, Json] =
    streamArrayToString(stream).through(stringArrayParser)

  import CompanyResponse._

  case class CompanyResponse(
    id: Id,
    name: Name,
    symbol: Symbol,
    liquidated: Liquidated,
    delisted: Delisted,
    active: Active,
    lastTradeDate: Option[LastTradeDate],
    firstTradeDate: Option[FirstTradeDate]
  )

  object CompanyResponse {
    case class Id(asString: String) extends AnyVal
    case class Name(asString: String) extends AnyVal
    case class Symbol(asString: String) extends AnyVal
    case class Liquidated(asBool: Boolean) extends AnyVal
    case class Delisted(asBool: Boolean) extends AnyVal
    case class Active(asBool: Boolean) extends AnyVal
    case class LastTradeDate(asDate: LocalDate) extends AnyVal
    case class FirstTradeDate(asDate: LocalDate) extends AnyVal
  }

}
