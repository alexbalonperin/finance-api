package io.abp.financeapi.modules

import doobie._
import cats.effect.{Async, ContextShift}
import io.abp.financeapi.config.DBConfig

final case class PostgresConnections[F[_]](connection: Transactor.Aux[F, Unit])

object PostgresConnections {
  def apply[F[_]: Async: ContextShift](
      config: DBConfig
  ): PostgresConnections[F] = {
    PostgresConnections(
      Transactor.fromDriverManager[F](
        config.driver,
        config.url
      )
    )
  }
}
