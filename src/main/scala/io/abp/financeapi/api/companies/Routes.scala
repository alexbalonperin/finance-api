package io.abp.financeapi.api.companies

import cats.effect.Sync
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.api.Protocol._
import io.abp.financeapi.api._
import io.abp.financeapi.programs._
import org.http4s.rho.RhoRoutes

import eu.timepit.refined.auto._

final case class Routes[F[_]: Sync](
    companiesProgram: CompaniesProgram[F]
) extends RhoRoutes[F] {
  import CustomQueryParsers._
  import CompaniesPrograms._

  GET / "companies" +? param[NonNegInt]("limit", DefaultLimit) & param[NonNegInt]("offset", DefaultOffset) |>> { (limit: NonNegInt, offset: NonNegInt) =>
      val request = CompanyListRequest(limit, offset)
      Ok(companiesProgram.list(request))
  }
}
