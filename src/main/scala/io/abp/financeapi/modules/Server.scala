package io.abp.financeapi.modules

import cats.effect.{ConcurrentEffect, ExitCode, Timer}
import cats.arrow.FunctionK
import cats.syntax.semigroupk._
import fs2.Stream
import io.abp.financeapi.config._
import org.http4s.server.DefaultServiceErrorHandler
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.{
  AutoSlash,
  RequestLogger,
  ResponseLogger,
  Timeout
}
import org.http4s.syntax.kleisli._
import org.http4s.{HttpApp, HttpRoutes}
import scala.concurrent.duration.FiniteDuration

final case class Server[F[_]: ConcurrentEffect: Timer](
    apis: Apis[F],
    config: ApiConfig
) {
  val routes: HttpRoutes[F] =
    apis.hello.routes <+> apis.companies.routes

  val program: Stream[F, ExitCode] =
    BlazeServerBuilder[F]
      .bindHttp(config.port.value, config.host)
      .withHttpApp(
        middleware(
          config.timeout,
          config.requestLogs,
          config.responseLogs
        )(routes)
      )
      .withServiceErrorHandler(DefaultServiceErrorHandler[F])
      .serve

  private def middleware(
      timeout: FiniteDuration,
      requestLogsConfig: HttpMessageLogsConfig,
      responseLogsConfig: HttpMessageLogsConfig
  )(
      httpRoutes: HttpRoutes[F]
  ): HttpApp[F] = {
    val httpApp = ({ routes: HttpRoutes[F] ⇒
      AutoSlash(routes)
    } andThen { routes ⇒
      Timeout(timeout)(routes)
    } andThen { routes ⇒
      routes.orNotFound
    })(httpRoutes)

    List[(Boolean, HttpApp[F] ⇒ HttpApp[F])](
      requestLogsConfig.log -> RequestLogger(
        requestLogsConfig.logHeaders,
        requestLogsConfig.logBody,
        FunctionK.id[F]
      ),
      responseLogsConfig.log -> ResponseLogger(
        responseLogsConfig.logHeaders,
        responseLogsConfig.logBody,
        FunctionK.id[F]
      )
    ).foldLeft(httpApp) {
      case (app, (true, loggerMiddleware)) ⇒ loggerMiddleware(app)
      case (app, (false, _)) ⇒ app
    }
  }
}
