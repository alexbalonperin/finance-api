package io.abp.financeapi.modules

import cats.effect.Async
import io.abp.financeapi.services._
import io.abp.financeapi.programs._

object Programs {
  def company[F[_]](
      companyService: CompanyService[F]
  )(
      implicit F: Async[F]
  ): F[CompanyProgram[F]] =
    F.pure(CompanyProgram[F](companyService))
}
