package io.abp.financeapi.modules

import io.abp.financeapi.programs._

final case class Programs[F[_]](
    companiesProgram: CompaniesProgram[F]
)

object Programs {
  def apply[F[_]](
      services: Services[F]
  ): Programs[F] =
    Programs[F](
      CompaniesPrograms(services.companiesService)
    )
}
