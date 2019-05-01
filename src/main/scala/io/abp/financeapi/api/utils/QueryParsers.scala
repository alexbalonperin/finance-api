package io.abp.financeapi.api.utils

import cats.Monad
import scala.util.Try
import org.http4s.RhoDsl
import org.http4s.rho.bits._
import org.http4s.rho.bits.QueryParser.Params
import eu.timepit.refined.types.numeric.NonNegInt

object CustomQueryParsers {
  implicit def nonNetIntParser[F[_]: Monad] =
    new QueryParser[F, NonNegInt] with RhoDsl[F] {
      override def collect(
          name: String,
          params: Params,
          default: Option[NonNegInt]
      ): ResultResponse[F, NonNegInt] = {
        params.get(name) match {
          case Some(Seq(value: String, _*)) =>
            Try(value.toInt).toEither.flatMap(NonNegInt.from) match {
              case Right(n) => SuccessResponse(n)
              case Left(e) =>
                badRequest(s"Couldn't parse query parameter '$name'. Error: $e")
            }

          case Some(Seq()) =>
            default match {
              case Some(defaultValue) => SuccessResponse(defaultValue)
              case None =>
                badRequest(s"Value of query parameter '$name' missing")
            }
          case None =>
            default match {
              case Some(defaultValue) => SuccessResponse(defaultValue)
              case None               => badRequest(s"Missing query param: $name")
            }
        }
      }
    }

}
