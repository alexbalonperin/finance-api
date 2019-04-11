package io.abp.financeapi.api.healthz

import cats.effect.Sync
import org.http4s.rho.RhoRoutes

final case class Routes[F[_]: Sync]() extends RhoRoutes[F] {

  GET / "healthz" |>> Ok("")

}
