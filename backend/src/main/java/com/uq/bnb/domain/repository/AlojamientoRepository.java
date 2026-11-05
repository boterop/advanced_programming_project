package com.uq.bnb.domain.repository;

import com.uq.bnb.domain.entity.Alojamiento;

import java.util.Optional;

/**
 * Puerto del agregado Alojamiento. F-13: el sistema administra un único alojamiento, por eso se consulta sin
 * identificador. Temporada y PoliticaCancelacion viven dentro del agregado y se guardan por su raíz: no tienen
 * repositorio propio.
 */
public interface AlojamientoRepository {

    Optional<Alojamiento> obtener();

    void guardar(Alojamiento alojamiento);
}
