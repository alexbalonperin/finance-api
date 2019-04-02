package io.abp.financeapi

package object programs {
  type CompaniesProgram[F[_]] = programs.companies.Algebra[F]
  val CompaniesPrograms = programs.companies.Program
}
