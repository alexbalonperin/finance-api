package io.abp.financeapi.services.companies

import cats.effect.Sync
import io.abp.financeapi.domain.Company
import io.abp.financeapi.repositories.CompanyRepository

trait Algebra[F[_]] {
  def list(): F[List[Company]]
}

object Services {
  def default[F[_]](
    companyRepository: CompanyRepository[F]
  )(
    implicit F: Sync[F]
  ): F[Algebra[F]] =
    F.pure(new Default[F](companyRepository))
}


final class Default[F[_]: Sync](
  companyRepository: CompanyRepository[F]
) extends Algebra[F] {
  def list(): F[List[Company]] = companyRepository.list()
}
