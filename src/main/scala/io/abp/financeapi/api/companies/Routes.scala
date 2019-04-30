package io.abp.financeapi.api.companies

import cats.effect.Sync
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.api.Protocol._
import io.abp.financeapi.api._
import io.abp.financeapi.api.utils.CustomQueryParsers
import io.abp.financeapi.programs._
import org.http4s.rho.RhoRoutes
import io.abp.financeapi.domain.Company

import eu.timepit.refined.auto._

final case class Routes[F[_]: Sync](
    companiesProgram: CompaniesProgram[F]
) extends RhoRoutes[F] {
  import CustomQueryParsers._

  GET / "companies" +? param[NonNegInt]("limit", DefaultLimit) & param[NonNegInt]("offset", DefaultOffset) |>> { (limit: NonNegInt, offset: NonNegInt) =>
      val request = CompaniesPrograms.ListRequest(limit, offset)
      Ok(companiesProgram.list(request).map(toResponse))
  }

  GET / "companies" / pathVar[String]("id", "company identifier") |>> { (id: String) =>
      val request = CompaniesPrograms.GetRequest(Company.Id(id))
      Ok(companiesProgram.get(request).map(toResponse))
  }
}
