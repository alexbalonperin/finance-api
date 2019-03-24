package io.abp.financeapi

package object services {
  type CompaniesService[F[_]] = services.companies.Algebra[F]
  val CompaniesServices = services.companies.Services
}
