package io.abp.financeapi.api

import cats.effect.Sync
import fs2.Stream
import io.abp.financeapi.domain.Company
import io.circe.fs2._
import io.circe.generic.extras.decoding.UnwrappedDecoder
import io.circe.generic.extras.encoding.UnwrappedEncoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object Protocol {

  implicit val encoder: Encoder[Company] = deriveEncoder[Company]

  implicit def jsonDecoder[F[_]: Sync, A <: Product: Decoder]
      : EntityDecoder[F, A] = jsonOf[F, A]
  implicit def jsonEncoder[F[_]: Sync, A <: Product: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf[F, A]
  implicit def valueClassEncoder[A: UnwrappedEncoder]: Encoder[A] = implicitly
  implicit def valueClassDecoder[A: UnwrappedDecoder]: Decoder[A] = implicitly

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
