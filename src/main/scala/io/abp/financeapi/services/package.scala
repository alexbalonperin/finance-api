package io.abp.financeapi

package object services {
  type CompanyService[F[_]] = services.companies.Algebra[F]
  val CompanyServices = services.companies.Services
}
