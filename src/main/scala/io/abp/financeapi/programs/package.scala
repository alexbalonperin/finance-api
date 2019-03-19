package io.abp.financeapi

package object programs {
  type CompanyProgram[F[_]] = programs.companies.Program[F]
  val CompanyProgram = programs.companies.Program
}
