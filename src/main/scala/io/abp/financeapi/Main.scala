package io.abp.financeapi.api

import cats.effect.{IO, IOApp, ContextShift, ExitCode, ConcurrentEffect, Timer}
import io.abp.financeapi.config.ApplicationConfig
import pureconfig.generic.auto._
import io.abp.financeapi.modules.{Server, Apis}
import fs2.Stream

import cats.syntax.functor._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    program[IO].compile.drain.as(ExitCode.Success)

  def program[F[_]: ContextShift: Timer](
      implicit F: ConcurrentEffect[F]
  ): Stream[F, Unit] =
    for {
      config <- Stream.eval(
        F.pure(pureconfig.loadConfigOrThrow[ApplicationConfig])
      )
      apis ← Stream.emit(Apis())
      server ← Stream.emit(Server(apis, config.api))
      _ ← server.program
    } yield ()
}
