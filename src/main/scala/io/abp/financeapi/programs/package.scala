package io.abp.financeapi

package object programs {
  type CompaniesProgram[F[_]] = programs.companies.Program[F]
  val CompaniesPrograms = programs.companies.Program
}
