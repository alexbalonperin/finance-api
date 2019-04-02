package io.abp.financeapi.api.healthz

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final case class Routes[F[_]: Sync]() extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / "healthz" => Ok()
  }
}
