package com.uq.bnb.infrastructure.rest.mapper;

import com.uq.bnb.application.dto.response.CargoResponse;
import com.uq.bnb.application.dto.response.FolioResponse;
import com.uq.bnb.application.dto.response.PagoResponse;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.domain.entity.Cargo;
import com.uq.bnb.domain.entity.Folio;
import com.uq.bnb.domain.entity.Pago;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.Dinero;
import java.math.BigDecimal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolioMapper {
  default CodigoReserva toCodigo(String valor) {
    try {
      return new CodigoReserva(valor);
    } catch (ReglaDominioException e) {
      throw new ReservaNoEncontradaException(valor);
    }
  }

  default Dinero toDinero(BigDecimal valor) {
    return Dinero.de(valor);
  }

  default CargoResponse toCargoResponse(Cargo cargo) {
    return new CargoResponse(
        java.util.UUID.fromString(cargo.id().valor()),
        cargo.tipo(),
        cargo.valor().monto().longValueExact());
  }

  default PagoResponse toPagoResponse(Pago pago) {
    return new PagoResponse(
        java.util.UUID.fromString(pago.id().valor()),
        pago.medio(),
        pago.fecha(),
        pago.valor().monto().longValueExact());
  }

  default FolioResponse toResponse(String codigoReserva, Folio folio) {
    return new FolioResponse(
        codigoReserva,
        folio.cargos().stream().map(this::toCargoResponse).toList(),
        folio.pagos().stream().map(this::toPagoResponse).toList(),
        folio.calcularTotalCargos().monto().longValueExact(),
        folio.calcularTotalPagos().monto().longValueExact(),
        folio.calcularSaldoPendiente().monto().longValueExact(),
        folio.calcularTotalCargos().moneda().name());
  }
}
