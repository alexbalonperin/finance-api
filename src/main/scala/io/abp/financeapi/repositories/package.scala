package io.abp.financeapi

package object repositories {
  type CompaniesRepository[F[_]] = repositories.companies.Repository[F]
  val CompaniesRepositories = repositories.companies.Repository
}
