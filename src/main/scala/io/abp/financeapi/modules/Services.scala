package io.abp.financeapi.modules

import cats.effect.Sync
import io.abp.financeapi.services._

final case class Services[F[_]: Sync](
    companiesService: CompaniesService[F]
)

object Services {
  def apply[F[_]: Sync](
      repositories: Repositories[F]
  ): Services[F] =
    Services(
      CompaniesServices.default(repositories.companiesRepository)
    )
}
