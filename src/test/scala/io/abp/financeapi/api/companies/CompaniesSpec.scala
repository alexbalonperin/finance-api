package io.abp.financeapi.api.companies

import cats.effect.IO
import org.http4s.{Request, Response, Method, Status, Uri}
import org.http4s.syntax.kleisli._
import org.specs2.matcher.MatchResult
import fs2.Stream
import io.abp.financeapi.programs.CompaniesPrograms

class CompaniesSpec extends org.specs2.mutable.Specification {

  "Companies" >> {
    "return 200" >> {
      uriReturns200()
    }
  }

  private[this] val retCompanies: Response[IO] = {
    val getCompanies = Request[IO](Method.GET, Uri.uri("/companies"))
    val result = for {
      program <- Stream.emit(CompaniesPrograms.dummy[IO])
      route ← Stream.emit(Routes(program))
    } yield route.toRoutes().orNotFound(getCompanies)
    result.compile.toList.head.unsafeRunSync
  }

  private[this] def uriReturns200(): MatchResult[Status] =
    retCompanies.status must beEqualTo(Status.Ok)
}
