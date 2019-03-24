package io.abp.financeapi.api.companies

import cats.effect.Sync
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.api.Protocol._
import io.abp.financeapi.api._
import io.abp.financeapi.programs._
import org.http4s.{HttpRoutes, QueryParamDecoder}
import org.http4s.dsl.Http4sDsl

import eu.timepit.refined.auto._

final case class Routes[F[_]: Sync](
    companiesProgram: CompaniesProgram[F]
) extends Http4sDsl[F] {
  import CompaniesPrograms._

  implicit val optionalNonNegIntQueryParamDecoder
      : QueryParamDecoder[NonNegInt] =
    QueryParamDecoder[Int].map(NonNegInt.from(_).right.get)

  object LimitQueryParamMatcher
      extends OptionalQueryParamDecoderMatcher[NonNegInt]("limit")
  object OffsetQueryParamMatcher
      extends OptionalQueryParamDecoderMatcher[NonNegInt]("offset")

  val routes: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / "companies" :? LimitQueryParamMatcher(optionLimit) +& OffsetQueryParamMatcher(
          optionOffset
        ) =>
      val limit = optionLimit.getOrElse(DefaultLimit)
      val offset = optionOffset.getOrElse(DefaultOffset)
      val request = CompanyListRequest(limit, offset)
      Ok(companiesProgram.list(request))
  }
}
