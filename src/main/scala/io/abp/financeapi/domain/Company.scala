package io.abp.financeapi.domain

import Company._

case class Company(name: Name)

object Company {
  case class Name(value: String) extends AnyVal
}
