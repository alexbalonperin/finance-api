package io.abp.financeapi.modules

import cats.effect.{Async, ContextShift}
import io.abp.financeapi.config.DBConfig
import io.abp.financeapi.repositories._

final case class Repositories[F[_]](
    companiesRepository: CompaniesRepository[F]
)

object Repositories {
  def apply[F[_]: Async: ContextShift](config: DBConfig): Repositories[F] = {
    val connection = CompaniesRepositories.Connection(config)
    Repositories(CompaniesRepositories.postgres(connection))
  }
}
