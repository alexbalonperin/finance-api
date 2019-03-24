package io.abp.financeapi

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.auto._

package object api {
  type HelloRoutes[F[_]] = hello.Routes[F]
  val HelloRoutes = hello.Routes
  type CompanyRoutes[F[_]] = companies.Routes[F]
  val CompanyRoutes = companies.Routes

  val DefaultLimit: NonNegInt = 10
  val DefaultOffset: NonNegInt = 0
}
