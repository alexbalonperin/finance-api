package io.abp.financeapi

package object api {
  type HelloRoutes[F[_]] = hello.Routes[F]
  val HelloRoutes = hello.Routes
  type CompanyRoutes[F[_]] = companies.Routes[F]
  val CompanyRoutes = companies.Routes
}
