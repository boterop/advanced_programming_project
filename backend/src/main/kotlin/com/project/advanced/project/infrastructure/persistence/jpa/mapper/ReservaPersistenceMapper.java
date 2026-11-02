package com.project.advanced.project.infrastructure.persistence.jpa.mapper;

import com.project.advanced.project.domain.entity.Ocupante;
import com.project.advanced.project.domain.entity.Reserva;
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad;
import com.project.advanced.project.domain.valueobject.ID;
import com.project.advanced.project.domain.valueobject.VersionPolitica;
import com.project.advanced.project.infrastructure.persistence.jpa.entity.OcupanteEmbeddable;
import com.project.advanced.project.infrastructure.persistence.jpa.entity.ReservaEntity;

@Mapper(componentModel = "spring")
public interface ReservaPersistenceMapper {



    //------Dominio a Entity ------//

    @Mapping(target = "id", ignore = true)
    @Mapping(target= "codigo", source = "codigo.valor")
    @Mapping(target= "apartamentoId", source = "apartamento.valor")
    @Mapping(target = "documentoTitular", source = "titular.documento.valor")
    @Mapping(target = "horaEstimadaLLegada", source = "HoraEstimadaLLegada.hora")
    ReservaEntity toReservaEntity(Reserva reserva);

    @Mapping(target = "documento", source = "documento.valor")
    OcupanteEmbeddable toOcupanteEmbeddable(Ocupante ocupante);

    //-----Entity a Dominio-----//

    default Ocupante toOcupanteDomain(OcupanteEmbeddable ocupanteEmbeddable){
        return new Ocupante(
         new DocumentoIdentidad(ocupanteEmbeddable.documento()),
         ocupanteEmbeddable.nombreCompleto(),
         ocupanteEmbeddable.fechaNacimiento());
    }

    default Reserva toReservaDomain(reservaEntity reservaEntity){

        //1. Reconstruir los ocupantes y el titular
        List<Ocupante> ocupantes = reservaEntity.getOcupantes().stream()
                .map(this::toOcupanteDomain).toList();

        Ocupante titular = ocupantes.Stream()
                .filter(Ocupante ocupante ->
                        ocupante.getDocumento().valor().equals(reservaEntity.getDocumentoTitular()))
                .findFirst().orElseThrow(() -> new IllegalStateException("El titular no esta dentro de los ocupantes"));

        //Realizar la reconstruccion de la reserva
        return new Reserva(
                new ID(reservaEntity.getcodigo()),
                new IdentificacionApartamento(reservaEntuty.getApartamentoID()),
                new Estancia(reservaEntity.getEstancia().fechaEntrada(),reservaEntity.getEstado()),
                reservaEntity.getEstado(),
                reservaEntity.getCanalOrigen,
                titular, ocupantes,
                new HoraEstimadaLLegada(reservaEntity.getHoraEstimadaLLegada()),
                new Dinero(reservaEntity.getValorCongelado()),
                new VersionPolitica(reservaEntity.getPoliticaCongelada().numero(), reservaEntity.getPoliticaCongelada())
                reservaEntity.getFechaCreacion(),
                reservaEntity.getMotivoCancelacion;
        )

    }

}
