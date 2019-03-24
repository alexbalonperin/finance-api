package io.abp.financeapi.programs.companies

import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.domain.Company
import io.abp.financeapi.services._
import io.scalaland.chimney.dsl._

final case class Program[F[_]](
    companiesService: CompaniesService[F]
) {
  def list(
      request: Program.CompanyListRequest
  ): fs2.Stream[F, Company] = {
    val servicesRequest =
      request.into[CompaniesServices.CompanyListRequest].transform
    companiesService.list(servicesRequest)
  }
}

object Program {
  final case class CompanyListRequest(limit: NonNegInt, offset: NonNegInt)
}
