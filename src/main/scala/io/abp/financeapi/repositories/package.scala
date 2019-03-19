package io.abp.financeapi

package object repositories {
  type CompanyRepository[F[_]] = repositories.companies.Repository[F]
  val CompanyRepository = repositories.companies.Repository
}
