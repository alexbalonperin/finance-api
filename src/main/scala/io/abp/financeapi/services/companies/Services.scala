package io.abp.financeapi.services.companies

import cats.effect.Sync
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.domain.Company
import io.abp.financeapi.repositories.CompaniesRepository

import eu.timepit.refined.auto._

trait Algebra[F[_]] {
  def list(request: Services.CompanyListRequest): fs2.Stream[F, Company]
}

object Services {
  def default[F[_]: Sync](
      companiesRepository: CompaniesRepository[F]
  ): Algebra[F] =
    new Default[F](companiesRepository)

  def dummy[F[_]: Sync]()(
  ): Algebra[F] =
    new Dummy[F]()

  final case class CompanyListRequest(limit: NonNegInt, offset: NonNegInt)

  val DefaultLimit: NonNegInt = 10
  val DefaultOffset: NonNegInt = 0
}

final class Default[F[_]: Sync](
    companiesRepository: CompaniesRepository[F]
) extends Algebra[F] {
  def list(request: Services.CompanyListRequest): fs2.Stream[F, Company] =
    companiesRepository.list(request.limit, request.offset)
}

final class Dummy[F[_]](implicit F: Sync[F]) extends Algebra[F] {
  def list(request: Services.CompanyListRequest): fs2.Stream[F, Company] =
    fs2.Stream.eval(F.pure(Company(Company.Name("Apple"))))
}
