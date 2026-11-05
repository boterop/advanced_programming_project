package com.uq.bnb.infrastructure.rest.mapper;
import com.uq.bnb.application.dto.request.ConfirmarReservaRequest;
import com.uq.bnb.application.dto.request.CrearReservaRequest;
import com.uq.bnb.application.dto.request.OcupanteRequest;
import com.uq.bnb.application.dto.response.OcupanteResponse;
import com.uq.bnb.application.dto.response.ReservaDetalleResponse;
import com.uq.bnb.application.dto.response.ReservaResumenResponse;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.entity.Titular;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Cancelacion;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.MedioPago;
import com.uq.bnb.domain.valueobject.SolicitudReserva;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
@Mapper(componentModel = "spring")
public abstract class ReservaMapper {
    @Autowired
    protected IDProvider idProvider;

    public SolicitudReserva toSolicitud(CrearReservaRequest request) {
        return new SolicitudReserva(
                toTitular(request.titular()),
                toAcompanantes(request.ocupantes()),
                new Estancia(request.fechaEntrada(), request.fechaSalida()),
                request.canalOrigen(),
                Boolean.TRUE.equals(request.conMascota()),
                request.horaEstimadaLlegada());
    }
    public Titular toTitular(OcupanteRequest request) {
        return Titular.crear(idProvider.obtener(), request.documento(), request.nombre(), request.fechaNacimiento());
    }
    public List<Ocupante> toAcompanantes(List<OcupanteRequest> requests) {
        return requests.stream()
                .map(r -> Ocupante.crear(idProvider.obtener(), r.documento(), r.nombre(), r.fechaNacimiento()))
                .toList();
    }
    public CodigoReserva toCodigo(String valor) {
        try {
            return new CodigoReserva(valor);
        } catch (ReglaDominioException e) {
            throw new ReservaNoEncontradaException(valor);
        }
    }
    public MedioPago toMedioPago(ConfirmarReservaRequest request) {
        return request == null ? null : request.medioPago();
    }
    public LocalTime toHoraEstimadaLlegada(ConfirmarReservaRequest request) {
        return request == null ? null : request.horaEstimadaLlegada();
    }
    public String toReferencia(ConfirmarReservaRequest request) {
        return request == null ? null : request.referencia();
    }
    public OcupanteResponse toOcupanteResponse(Ocupante ocupante) {
        return new OcupanteResponse(ocupante.documento(), ocupante.nombre(), ocupante.fechaNacimiento());
    }
    public ReservaResumenResponse toResumenResponse(Reserva reserva) {
        return new ReservaResumenResponse(
                reserva.codigo().valor(),
                reserva.estado(),
                reserva.apartamentoId(),
                reserva.estancia().entrada(),
                reserva.estancia().salida(),
                reserva.titular().nombre(),
                reserva.ocupantes().size(),
                reserva.cotizacion().valorTotal().monto().longValueExact());
    }
    public ReservaDetalleResponse toDetalleResponse(Reserva reserva) {
        return new ReservaDetalleResponse(
                reserva.codigo().valor(),
                reserva.estado(),
                reserva.canalOrigen(),
                reserva.apartamentoId(),
                reserva.estancia().entrada(),
                reserva.estancia().salida(),
                (int) reserva.estancia().noches(),
                reserva.horaEstimadaLlegada(),
                reserva.conMascota(),
                toOcupanteResponse(reserva.titular()),
                reserva.ocupantes().stream().map(this::toOcupanteResponse).toList(),
                reserva.ocupantes().size(),
                reserva.cotizacion().valorTotal().monto().longValueExact(),
                reserva.folio().calcularTotalCargos().monto().longValueExact(),
                reserva.cotizacion().valorTotal().moneda().name(),
                "v" + reserva.versionPolitica().numero(),
                reserva.fechaCreacion().toLocalDate(),
                Optional.ofNullable(reserva.cancelacion()).map(Cancelacion::motivo).orElse(null),
                Optional.ofNullable(reserva.cancelacion()).map(c -> c.retenido().monto().longValueExact()).orElse(null),
                Optional.ofNullable(reserva.cancelacion()).map(c -> c.devuelto().monto().longValueExact()).orElse(null));
    }
}
