package io.abp.financeapi.repositories.companies

import cats.effect.{Async, ContextShift}
import doobie._
import doobie.implicits._
import doobie.util.query.Query
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.config.DBConfig

import io.abp.financeapi.domain.Company
import io.abp.financeapi.repositories.Protocol.{ Company => PCompany, _ }

trait Repository[F[_]] {
  def list(limit: NonNegInt, offset: NonNegInt): fs2.Stream[F, Company]
  def get(id: Company.Id): fs2.Stream[F, Company]
}

object Repository {
  def postgres[F[_]: Async: ContextShift](
      connection: Repository.Connection[F]
  ): Repository[F] =
    PostgresRepository(connection)

  final case class Connection[F[_]: Async: ContextShift](
      value: Transactor.Aux[F, Unit]
  )

  object Connection {
    def apply[F[_]: Async: ContextShift](config: DBConfig): Connection[F] =
      Connection(
        Transactor.fromDriverManager[F](
          config.driver,
          config.url.value,
          config.user.value,
          config.password.value
        )
      )
  }
}

final case class PostgresRepository[F[_]: Async: ContextShift](
    connection: Repository.Connection[F]
) extends Repository[F] {

  implicit val NonNegIntComposite: Write[NonNegInt] =
    Write[Int].contramap(_.value)

  def list(limit: NonNegInt, offset: NonNegInt): fs2.Stream[F, Company] = {

    val sql = """select id, name from companies limit ? offset ?"""

    val query = Query[(NonNegInt, NonNegInt), PCompany](sql)
      .stream((limit, offset))
    query.transact(connection.value).map(toDomain)
  }

  def get(id: Company.Id): fs2.Stream[F, Company] = {

    val sql = """select id, name from companies where id = ?"""

    val query = Query[String, PCompany](sql)
      .stream(id.asString)
    query.transact(connection.value).map(toDomain)
  }
}
