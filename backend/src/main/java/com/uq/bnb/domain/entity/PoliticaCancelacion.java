package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.TramoAntelacion;
import com.uq.bnb.domain.valueobject.VersionPolitica;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.nio.charset.StandardCharsets;


@Getter
public final class PoliticaCancelacion {

    private static final int MINIMO_TRAMOS = 2;

    private final ID id;
    private final VersionPolitica numeroVersion;
    private final LocalDate vigenteDesde;
    private final List<TramoAntelacion> tramos;

    private PoliticaCancelacion(ID id, VersionPolitica numeroVersion, LocalDate vigenteDesde, List<TramoAntelacion> tramos) {
        this.id = id;
        this.numeroVersion = numeroVersion;
        this.vigenteDesde = vigenteDesde;
        this.tramos = tramos;
    }

    public static PoliticaCancelacion crear(ID id, int numeroVersion, LocalDate vigenteDesde, List<TramoAntelacion> tramos) {
        if (numeroVersion < 1) {
            throw new ReglaDominioException("El número de versión debe ser positivo");
        }
        Objects.requireNonNull(vigenteDesde, "La fecha de vigencia no puede ser nula");
        Objects.requireNonNull(tramos, "Los tramos de antelación no pueden ser nulos");
        if (tramos.size() < MINIMO_TRAMOS) {
            throw new ReglaDominioException("Una política de cancelación debe tener al menos " + MINIMO_TRAMOS + " tramos de antelación");
        }
        long antelacionesDistintas = tramos.stream().map(TramoAntelacion::antelacionMinimaHoras).distinct().count();
        if (antelacionesDistintas != tramos.size()) {
            throw new ReglaDominioException("Los tramos de la política no pueden repetir la misma antelación mínima");
        }
        if (tramos.stream().noneMatch(t -> t.antelacionMinimaHoras() == 0)) {
            throw new ReglaDominioException("La política debe tener un tramo de 0 horas de antelación para cubrir las cancelaciones más cercanas a la entrada");
        }
        return new PoliticaCancelacion(id, new VersionPolitica(numeroVersion, vigenteDesde), vigenteDesde, List.copyOf(tramos));
    }


    public static PoliticaCancelacion reconstruir(ID id, VersionPolitica version, List<TramoAntelacion> tramos) {
        return new PoliticaCancelacion(id, version, version.vigenteDesde(), List.copyOf(tramos));
    }


    public BigDecimal retencionAplicable(long horasDeAntelacion) {
        return tramos.stream()
                .filter(t -> t.aplicaPara(horasDeAntelacion))
                .max(Comparator.comparingInt(TramoAntelacion::antelacionMinimaHoras))
                .map(TramoAntelacion::porcentajeRetencion)
                .orElseThrow(() -> new ReglaDominioException(
                        "Ningún tramo de la política de versión " + numeroVersion + " cubre " + horasDeAntelacion + " horas de antelación"));
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof PoliticaCancelacion entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
