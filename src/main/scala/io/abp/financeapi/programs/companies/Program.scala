package io.abp.financeapi.programs.companies

import cats.Monad
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.domain.Company
import io.abp.financeapi.services._
import io.scalaland.chimney.dsl._

trait Algebra[F[_]] {
  def list(
      request: Program.CompanyListRequest
  ): fs2.Stream[F, Company]
}

final case class Program[F[_]](
    companiesService: CompaniesService[F]
) extends Algebra[F] {
  def list(
      request: Program.CompanyListRequest
  ): fs2.Stream[F, Company] = {
    val servicesRequest =
      request.into[CompaniesServices.CompanyListRequest].transform
    companiesService.list(servicesRequest)
  }
}


final case class Dummy[F[_]: Monad]() extends Algebra[F] {
  def list(
      request: Program.CompanyListRequest
  ): fs2.Stream[F, Company] = fs2.Stream.eval(Monad[F].pure(Company(Company.Name("Apple"))))
}

object Program {
  final case class CompanyListRequest(limit: NonNegInt, offset: NonNegInt)

  def dummy[F[_]: Monad] = Dummy[F]()
}
