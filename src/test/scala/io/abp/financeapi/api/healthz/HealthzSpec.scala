package io.abp.financeapi.api.healthz

import cats.effect.IO
import org.http4s.{Request, Response, Method, Status, Uri}
import org.specs2.matcher.MatchResult
import fs2.Stream
import org.http4s.syntax.kleisli._

class HealthzSpec extends org.specs2.mutable.Specification {

  "Healthz" >> {
    "return 200" >> {
      uriReturns200()
    }
  }

  private[this] val retHealthz: Response[IO] = {
    val get = Request[IO](Method.GET, Uri.uri("/healthz"))
    val result = for {
      route ← Stream.emit(Routes[IO]())
    } yield route.toRoutes().orNotFound(get)
    result.compile.toList.head.unsafeRunSync
  }

  private[this] def uriReturns200(): MatchResult[Status] =
    retHealthz.status must beEqualTo(Status.Ok)
}
