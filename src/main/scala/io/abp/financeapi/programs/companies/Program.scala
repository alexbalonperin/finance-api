package io.abp.financeapi.programs.companies

import cats.Monad
import eu.timepit.refined.types.numeric.NonNegInt
import io.abp.financeapi.domain.Company
import io.abp.financeapi.services._
import io.scalaland.chimney.dsl._

trait Algebra[F[_]] {
  def list(
      request: Program.ListRequest
  ): fs2.Stream[F, Company]

  def get(request: Program.GetRequest): fs2.Stream[F, Company]
}

final case class Program[F[_]](
    companiesService: CompaniesService[F]
) extends Algebra[F] {
  def list(
      request: Program.ListRequest
  ): fs2.Stream[F, Company] = {
    val servicesRequest =
      request.into[CompaniesServices.ListRequest].transform
    companiesService.list(servicesRequest)
  }

  def get(request: Program.GetRequest): fs2.Stream[F, Company] = {
    val servicesRequest = request.into[CompaniesServices.GetRequest].transform
    companiesService.get(servicesRequest)
  }
}


final case class Dummy[F[_]: Monad]() extends Algebra[F] {
  def list(
      request: Program.ListRequest
  ): fs2.Stream[F, Company] = fs2.Stream.eval(
    Monad[F].pure(
      Company.dummy
    )
  )

  def get(request: Program.GetRequest): fs2.Stream[F, Company] = fs2.Stream.eval(
    Monad[F].pure(Company.dummy.copy(id = request.id))
    )
}

object Program {
  final case class ListRequest(limit: NonNegInt, offset: NonNegInt)
  final case class GetRequest(id: Company.Id)

  def dummy[F[_]: Monad] = Dummy[F]()
}
