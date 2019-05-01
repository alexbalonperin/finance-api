package io.abp.financeapi.api

import cats.effect.Sync
import fs2.Stream
import io.circe.fs2._
import io.circe.generic.extras.encoding.UnwrappedEncoder
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object Protocol {

  implicit def jsonEncoder[F[_]: Sync, A <: Product: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf[F, A]
  implicit def valueClassEncoder[A: UnwrappedEncoder]: Encoder[A] = implicitly

  private def streamArrayToString[F[_]: Sync, A](
      stream: Stream[F, A]
  )(implicit E: Encoder[A]): Stream[F, String] =
    Stream("[")
      .append(stream.map(E.apply(_).spaces2).intersperse(", "))
      .append(Stream("]"))

  implicit def streamToJson[F[_]: Sync, A](
      stream: Stream[F, A]
  )(implicit E: Encoder[A]): Stream[F, Json] =
    streamArrayToString(stream).through(stringArrayParser)

}
