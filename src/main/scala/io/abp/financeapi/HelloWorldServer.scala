package io.abp.financeapi

import cats.effect.{IO, IOApp, ContextShift}
import org.http4s.server.blaze.{BlazeServerBuilder}
import org.http4s.server.Router
import org.http4s.implicits._

import cats.effect._
import cats.implicits._
import org.http4s.implicits._
import org.http4s.server.blaze._

object HelloWorldServer extends IOApp {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val cs: ContextShift[IO] = IO.contextShift(global)


  def helloWorldService = new HelloWorldService().service
  val httpApp = Router("/" -> helloWorldService).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
