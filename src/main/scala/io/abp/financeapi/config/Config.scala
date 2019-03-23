package io.abp.financeapi.config

import scala.concurrent.duration.FiniteDuration
import ciris.envF
import ciris.loadConfig
import cats.MonadError
import cats.syntax.flatMap._
import cats.syntax.functor._
import ciris.cats.effect._
import ciris.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.types.net.UserPortNumber


final case class ApplicationConfig(
    api: ApiConfig,
    db: DBConfig
)
object ApplicationConfig {

  def apply[F[_]: MonadError[?[_], Throwable]]: F[ApplicationConfig] =
    for {
      requestLogsConfig <- HttpMessageLogsConfig.requestLogsConfig()
      responseLogsConfig <- HttpMessageLogsConfig.responseLogsConfig()
      apiConfig <- ApiConfig(requestLogsConfig, responseLogsConfig)
      dbConfig <- DBConfig()
    } yield ApplicationConfig(
      apiConfig,
      dbConfig
      )
}

final case class ApiConfig(
    host: ApiConfig.Host,
    port: UserPortNumber,
    timeout: FiniteDuration,
    requestLogs: HttpMessageLogsConfig,
    responseLogs: HttpMessageLogsConfig
)
object ApiConfig {
  type Host = String

  def apply[F[_]: MonadError[?[_], Throwable]](
    requestLogsConfig: HttpMessageLogsConfig,
    responseLogsConfig: HttpMessageLogsConfig
  ): F[ApiConfig] =
    (loadConfig(
      envF[F, Host]("API_HOST"),
      envF[F, UserPortNumber]("API_PORT"),
      envF[F, FiniteDuration]("API_TIMEOUT")
    ) { (host, port, timeout) =>
      ApiConfig(
        host,
        port,
        timeout,
        requestLogsConfig,
        responseLogsConfig
      )
    }).orRaiseThrowable
}


final case class HttpMessageLogsConfig(
    logHeaders: Boolean,
    logBody: Boolean
) {
  val log: Boolean = logHeaders || logBody
}
object HttpMessageLogsConfig {
  def requestLogsConfig[F[_]: MonadError[?[_], Throwable]](): F[HttpMessageLogsConfig] =
    loadConfig(
      envF[F, Boolean]("API_REQUEST_LOG_CONFIG_HEADER"),
      envF[F, Boolean]("API_REQUEST_LOG_CONFIG_BODY"),
     )(HttpMessageLogsConfig(_, _)).orRaiseThrowable

  def responseLogsConfig[F[_]: MonadError[?[_], Throwable]](): F[HttpMessageLogsConfig] =
    loadConfig(
      envF[F, Boolean]("API_RESPONSE_LOG_CONFIG_HEADER"),
      envF[F, Boolean]("API_RESPONSE_LOG_CONFIG_BODY"),
    )(HttpMessageLogsConfig(_, _)).orRaiseThrowable
}

final case class DBConfig(
    driver: String,
    url: String
)

object DBConfig {
  def apply[F[_]: MonadError[?[_], Throwable]](): F[DBConfig] =
    loadConfig(
      envF[F, String]("DATABASE_DRIVER"),
      envF[F, String]("DATABASE_URL")
    )(DBConfig(_, _)).orRaiseThrowable
}
