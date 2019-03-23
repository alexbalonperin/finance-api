package io.abp.financeapi.api

import cats.effect.{IO, ConcurrentEffect, IOApp, ContextShift, ExitCode, Timer}
import cats.syntax.functor._
import fs2.Stream
import io.abp.financeapi.config.ApplicationConfig
import io.abp.financeapi.modules.{Server, Apis}

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    program[IO].compile.drain.as(ExitCode.Success)

  def program[F[_]: ConcurrentEffect: ContextShift: Timer](): Stream[F, Unit] =
    for {
      config <- Stream.eval(ApplicationConfig.apply)
      apis ← Stream.emit(Apis())
      //connection <- Stream.emit(PostgresConnections(config.db))
      server ← Stream.emit(Server(apis, config.api))
      _ ← server.program
    } yield ()
}
