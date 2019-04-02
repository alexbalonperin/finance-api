package io.abp.financeapi.modules

import cats.effect.Sync

import io.abp.financeapi.api._

final case class Apis[F[_]](
    companies: CompanyRoutes[F],
    healthz: HealthzRoutes[F]
)

object Apis {
  def apply[F[_]: Sync](
      programs: Programs[F]
  ): Apis[F] =
    Apis(
      CompanyRoutes(programs.companiesProgram),
      HealthzRoutes()
    )
}
