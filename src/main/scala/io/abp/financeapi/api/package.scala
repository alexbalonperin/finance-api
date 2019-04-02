package io.abp.financeapi

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.auto._

package object api {
  type HealthzRoutes[F[_]] = healthz.Routes[F]
  val HealthzRoutes = healthz.Routes
  type CompanyRoutes[F[_]] = companies.Routes[F]
  val CompanyRoutes = companies.Routes

  val DefaultLimit: NonNegInt = 10
  val DefaultOffset: NonNegInt = 0
}
