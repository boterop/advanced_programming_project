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
import org.mapstruct.Mapper;

import java.math.BigDecimal;

/** DTO <-> dominio del folio, sin lógica de negocio. */
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
        return new CargoResponse(cargo.id(), cargo.tipo(), cargo.valor().monto().longValueExact());
    }

    default PagoResponse toPagoResponse(Pago pago) {
        return new PagoResponse(pago.id(), pago.medio(), pago.fecha(), pago.valor().monto().longValueExact());
    }

    default FolioResponse toResponse(String codigoReserva, Folio folio) {
        return new FolioResponse(
                codigoReserva,
                folio.getCargos().stream().map(this::toCargoResponse).toList(),
                folio.getPagos().stream().map(this::toPagoResponse).toList(),
                folio.calcularTotalCargos().monto().longValueExact(),
                folio.calcularTotalPagos().monto().longValueExact(),
                folio.calcularSaldoPendiente().monto().longValueExact(),
                folio.calcularTotalCargos().moneda());
    }
}
