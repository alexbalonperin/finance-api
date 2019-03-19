package io.abp.financeapi.programs.companies

import io.abp.financeapi.services._
import io.abp.financeapi.domain.Company

final case class Program[F[_]](
  companyService: CompanyService[F]
) {
  def list(): F[List[Company]] =
    companyService.list()
}
