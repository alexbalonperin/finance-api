package io.abp.financeapi.repositories.companies

import cats.effect.{Async, ContextShift}
import doobie._
import doobie.implicits._
import doobie.util.query.Query
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.config.DBConfig

import io.abp.financeapi.domain.Company
import io.abp.financeapi.repositories.Protocol._

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

  val companyColumns = List(
    "id",
    "name",
    "symbol",
    "liquidated",
    "delisted",
    "active",
    "last_trade_date",
    "first_trade_date"
  )
  val industryColumns = List("name")
  val sectorColumns = List("name")

  val CompanyPrefix = "c"
  val IndustryPrefix = "i"
  val SectorPrefix = "s"
  val allColumns = companyColumns.map(addPrefix(CompanyPrefix)(_)) ++
    industryColumns.map(addPrefix(IndustryPrefix)(_)) ++
    sectorColumns.map(addPrefix(SectorPrefix)(_))
  val columnsAsString = allColumns.mkString(",")

  private def addPrefix(prefix: String): String => String =
    (column: String) => s"$prefix.$column"
}

final case class PostgresRepository[F[_]: Async: ContextShift](
    connection: Repository.Connection[F]
) extends Repository[F] {
  import Repository._

  implicit val NonNegIntComposite: Write[NonNegInt] =
    Write[Int].contramap(_.value)

  def list(limit: NonNegInt, offset: NonNegInt): fs2.Stream[F, Company] = {

    val sql = s"""
      select ${columnsAsString}
        from companies $CompanyPrefix
        join industries $IndustryPrefix on $IndustryPrefix.id = $CompanyPrefix.industry_id
        join sectors $SectorPrefix on $SectorPrefix.id = $IndustryPrefix.sector_id
       limit ?
      offset ?
    """

    val query = Query[(NonNegInt, NonNegInt), CompanyRow](sql)
      .stream((limit, offset))
    query.transact(connection.value).map(toDomain)
  }

  def get(id: Company.Id): fs2.Stream[F, Company] = {

    val sql = s"""
      select ${columnsAsString}
        from companies $CompanyPrefix
        join industries $IndustryPrefix on $IndustryPrefix.id = $CompanyPrefix.industry_id
        join sectors $SectorPrefix on $SectorPrefix.id = $IndustryPrefix.sector_id
       where $CompanyPrefix.id = ?
    """

    val query = Query[String, CompanyRow](sql)
      .stream(id.asString)
    query.transact(connection.value).map(toDomain)
  }
}
