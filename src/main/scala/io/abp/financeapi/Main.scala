package io.abp.financeapi.api

import cats.effect.{IO, ConcurrentEffect, IOApp, ContextShift, ExitCode, Timer}
import cats.syntax.functor._
import fs2.Stream
import io.abp.financeapi.config.ApplicationConfig
import io.abp.financeapi.modules._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    program[IO].compile.drain.as(ExitCode.Success)

  def program[F[_]: ConcurrentEffect: ContextShift: Timer](): Stream[F, Unit] =
    for {
      config <- Stream.eval(ApplicationConfig.apply)
      repositories <- Stream.emit(Repositories(config.db))
      services <- Stream.emit(Services(repositories))
      programs <- Stream.emit(Programs(services))
      apis ← Stream.emit(Apis(programs))
      server ← Stream.emit(Server(apis, config.api))
      _ ← server.program
    } yield ()
}
