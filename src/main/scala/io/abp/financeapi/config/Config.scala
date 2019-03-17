package io.abp.financeapi.config

import scala.concurrent.duration.FiniteDuration

final case class ApplicationConfig(
    api: ApiConfig,
    db: DBConfig
)

final case class ApiConfig(
    host: Host,
    port: Port,
    timeout: FiniteDuration,
    requestLogs: HttpMessageLogsConfig,
    responseLogs: HttpMessageLogsConfig
)

final case class HttpMessageLogsConfig(
    logHeaders: Boolean,
    logBody: Boolean
) {
  val log: Boolean = logHeaders || logBody
}

final case class DBConfig(
    host: Host,
    port: Port
)

final case class Port(value: Int) extends AnyVal
final case class Host(value: String) extends AnyVal
