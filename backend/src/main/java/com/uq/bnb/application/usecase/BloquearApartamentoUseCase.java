package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.service.GestionBloqueoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso: bloquear un apartamento en un rango de fechas (RN-07). Orquesta; la regla vive en GestionBloqueoService. */
@Service
public class BloquearApartamentoUseCase {

    private final ApartamentoRepository apartamentoRepository;
    private final GestionBloqueoService gestionBloqueoService;

    public BloquearApartamentoUseCase(ApartamentoRepository apartamentoRepository, GestionBloqueoService gestionBloqueoService) {
        this.apartamentoRepository = apartamentoRepository;
        this.gestionBloqueoService = gestionBloqueoService;
    }

    @Transactional
    public Bloqueo ejecutar(String identificacion, Bloqueo bloqueo) {
        Apartamento apartamento = apartamentoRepository.buscarPorId(identificacion)
                .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacion));
        gestionBloqueoService.registrarBloqueo(apartamento, bloqueo);
        apartamentoRepository.guardar(apartamento);
        return bloqueo;
    }
}
