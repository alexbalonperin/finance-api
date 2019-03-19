package io.abp.financeapi.repositories.companies

import io.abp.financeapi.domain.Company

trait Repository[F[_]] {
  def list(): F[List[Company]]
}

object Repository {
}
