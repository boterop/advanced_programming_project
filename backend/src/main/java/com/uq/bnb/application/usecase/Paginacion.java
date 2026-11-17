package com.uq.bnb.application.usecase;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

final class Paginacion {
  private Paginacion() {}

  static <T> Page<T> paginar(List<T> elementos, Pageable pageable) {
    if (pageable.isUnpaged()) {
      return new PageImpl<>(elementos);
    }
    int desde = (int) Math.min(pageable.getOffset(), elementos.size());
    int hasta = Math.min(desde + pageable.getPageSize(), elementos.size());
    return new PageImpl<>(elementos.subList(desde, hasta), pageable, elementos.size());
  }
}
